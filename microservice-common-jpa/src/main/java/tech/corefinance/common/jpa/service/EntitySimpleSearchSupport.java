package tech.corefinance.common.jpa.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import tech.corefinance.common.ex.ServiceProcessingException;
import tech.corefinance.common.model.GenericModel;
import tech.corefinance.common.service.SimpleSearchSupport;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Simple search support for all JPA entities.
 */
@Repository
@Slf4j
@ConditionalOnProperty(prefix = "tech.corefinance.common.enabled", name = "default-simple-search", havingValue = "true", matchIfMissing = true)
public class EntitySimpleSearchSupport implements SimpleSearchSupport<GenericModel<?>> {

    protected final Map<EntityType<?>, Set<SingularAttribute<?, ?>>> supportedAttributes;
    @Autowired
    protected EntityManager entityManager;
    @Autowired
    protected JsonMapper jsonMapper;

    /**
     * Constructor will query all mapped entity and create list attribute that can we search by string.
     *
     * @param entityManager JPA EntityManager object.
     */
    @SuppressWarnings("unchecked")
    public EntitySimpleSearchSupport(@Autowired EntityManager entityManager) {
        supportedAttributes = new HashMap<>();
        var entitiesTypes = entityManager.getMetamodel().getEntities();
        entitiesTypes.forEach(et -> {
            Class<?> entityClass = et.getJavaType();
            log.debug("Scanning attribute of entity class [{}] for SimpleSearchSupport...", entityClass.getName());
            Set<SingularAttribute<?, ?>> attributes = (Set<SingularAttribute<?, ?>>) et.getSingularAttributes();
            log.debug("Attributes {}", attributes);
            Set<SingularAttribute<?, ?>> attributesSet = new HashSet<>();
            for (SingularAttribute<?, ?> attribute : attributes) {
                var attributeType = attribute.getJavaType();
                if (attributeType.isEnum() && isEnumSupportSearch(entityClass, attribute)) {
                    attributesSet.add(attribute);
                } else if (String.class.isAssignableFrom(attributeType)) {
                    attributesSet.add(attribute);
                }
            }
            if (!attributesSet.isEmpty()) {
                log.debug("Entity [{}] enabled search for {}", entityClass.getName(), attributesSet);
                this.supportedAttributes.put(et, attributesSet);
            }
        });
    }

    @Override
    public boolean isSupported(Class<?> clzz) {
        return supportedAttributes.entrySet().stream().anyMatch(e -> e.getKey().getJavaType().isAssignableFrom(clzz));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Page<GenericModel<?>> searchByTextAndPage(Class<? extends GenericModel<?>> clzz, String searchText, Pageable pageable) {
        Map<String, Object> paramMap = buildMapParam(searchText);
        var criteriaSql = buildCriteriaClause(clzz, paramMap);
        var sql = buildSelectPrefix(clzz, paramMap) + " " + criteriaSql;
        var countSql = buildCountPrefix(clzz, paramMap) + " " + criteriaSql;
        sql = appendSortToSql(sql, pageable.getSort());
        var countQuery = entityManager.createQuery(countSql, Long.class);
        setQueryParams(countQuery, searchText, paramMap);
        long count = countQuery.getSingleResult();

        var query = entityManager.createQuery(sql, clzz);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        setQueryParams(query, searchText, paramMap);
        var list = (List<GenericModel<?>>) query.getResultList();
        return new PageImpl<>(list, pageable, count);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<GenericModel<?>> searchByTextAndSort(Class<? extends GenericModel<?>> clzz, String searchText, Sort sort) {
        Map<String, Object> paramMap = buildMapParam(searchText);
        var sql = buildSelectPrefix(clzz, paramMap) + " " + buildCriteriaClause(clzz, paramMap);
        sql = appendSortToSql(sql, sort);
        var query = entityManager.createQuery(sql, clzz);
        setQueryParams(query, searchText, paramMap);
        return (List<GenericModel<?>>) query.getResultList();
    }

    @Override
    public List<GenericModel<?>> findAll(Class<? extends GenericModel<?>> clzz, Sort sort) {
        Map<String, Object> paramMap = new HashMap<>();
        var sql = buildSelectPrefix(clzz, paramMap);
        // Add sort
        sql = appendSortToSql(sql, sort);
        // Create query
        var query = entityManager.createQuery(sql, clzz);
        //noinspection unchecked
        return (List<GenericModel<?>>) query.getResultList();
    }

    @Override
    public List<GenericModel<?>> findAll(Class<? extends GenericModel<?>> clzz) {
        return findAll(clzz, Sort.unsorted());
    }

    protected String buildCountPrefix(Class<? extends GenericModel<?>> clzz, Map<String, Object> paramMap) {
        var countSql = "SELECT count(o) FROM " + clzz.getSimpleName() + " o";
        log.debug("Count SQL [{}]", countSql);
        return countSql;
    }

    protected boolean isEnumSupportSearch(Class<?> entityClass, SingularAttribute<?, ?> attribute) {
        var attributeName = attribute.getName();
        log.debug("Enum Property [{}]", attributeName);

        Field field;
        Method getterMethod;
        try {
            getterMethod =
                    entityClass.getMethod("get" + attribute.getName().substring(0, 1).toUpperCase() + attribute.getName().substring(1));

            if (getterMethod.isAnnotationPresent(Enumerated.class)) {
                Enumerated enumeratedAnnotation = getterMethod.getAnnotation(Enumerated.class);
                if (enumeratedAnnotation.value() == EnumType.STRING) {
                    log.debug("Method [{}] of class [{}] has annotation Enumerated with EnumType.STRING! This field support search!",
                              getterMethod.getName(), entityClass.getName());
                    return true;
                }
            }

            try {
                field = entityClass.getDeclaredField(attribute.getName());
                if (field.isAnnotationPresent(Enumerated.class)) {
                    Enumerated enumeratedAnnotation = field.getAnnotation(Enumerated.class);
                    if (enumeratedAnnotation.value() == EnumType.STRING) {
                        log.debug("Field [{}] of class [{}] has annotation Enumerated with EnumType.STRING!" + "This field support search!",
                                  field.getName(), entityClass.getName());
                        return true;
                    }
                }
            } catch (NoSuchFieldException e1) {
                log.error("Skipped missing field [{}] from class [{}]!", attributeName, entityClass.getName());
            }
        } catch (NoSuchMethodException e) {
            log.error("Error scanning field [{}] from class [{}]", attributeName, entityClass.getName(), e);
        }
        return false;
    }

    protected String buildSelectPrefix(Class<? extends GenericModel<?>> clzz, Map<String, Object> paramMap) {
        return "FROM " + clzz.getSimpleName() + " o";
    }

    protected String buildCriteriaClause(Class<?> clzz, Map<String, Object> paramMap) {
        StringBuilder sqlBuilder = new StringBuilder("WHERE ");
        int index = 0;
        if (!paramMap.isEmpty()) {
            for (Map.Entry<String, Object> param : paramMap.entrySet()) {
                var key = param.getKey();
                var value = param.getValue();
                if (index > 0) {
                    sqlBuilder.append(" AND ");
                }
                index++;
                if (value instanceof String) {
                    sqlBuilder.append("lower(").append(key).append(") like lower(:").append(key).append(")");
                } else {
                    sqlBuilder.append(key).append(" = :").append(key);
                }
            }
        } else {
            var attributes = getEntitySearchableAttr(clzz);
            for (var a : attributes) {
                if (index > 0) {
                    sqlBuilder.append(" AND ");
                }
                sqlBuilder.append("lower(").append(a.getName()).append(") like lower(:searchText)");
                index++;
            }
        }
        var sql = sqlBuilder.toString();
        log.debug("Search SQL [{}]", sql);
        return sql;
    }

    protected String appendSortToSql(String sql, Sort sort) {
        StringBuilder sqlBuilder = new StringBuilder(sql);
        if (sort != null && sort.isSorted()) {
            sqlBuilder.append(" ORDER BY ");
            boolean firstSort = true;
            for (Sort.Order order : sort) {
                if (!firstSort) {
                    sqlBuilder.append(",");
                } else {
                    sqlBuilder.append(" ");
                }
                sqlBuilder.append(order.getProperty()).append(" ").append(order.getDirection());
                firstSort = false;
            }
        }
        return sqlBuilder.toString();
    }

    protected Map<String, Object> buildMapParam(String searchText) {
        if (searchText.startsWith("{") && searchText.endsWith("}")) {
            var typeRef = new TypeReference<LinkedHashMap<String, Object>>() {};
            return jsonMapper.readValue(searchText, typeRef);
        } else {
            return new HashMap<>();
        }
    }

    protected void setQueryParams(TypedQuery<?> query, String searchText, Map<String, Object> paramMap) {
        if (!paramMap.isEmpty()) {
            for (Map.Entry<String, Object> param : paramMap.entrySet()) {
                var key = param.getKey();
                var value = param.getValue();
                if (value instanceof String) {
                    query.setParameter(key, "%" + value + "%");
                } else {
                    query.setParameter(key, value);
                }
            }
        } else {
            query.setParameter("searchText", "%" + searchText + "%");
        }
    }

    protected Set<SingularAttribute<?, ?>> getEntitySearchableAttr(Class<?> clzz) {
        var optional = supportedAttributes.entrySet().stream().filter(et -> et.getKey().getJavaType().isAssignableFrom(clzz)).findFirst();
        if (optional.isEmpty()) {
            throw new ServiceProcessingException("Cannot find entity class!");
        }
        var et = optional.get();
        return et.getValue();
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
