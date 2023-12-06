package tech.corefinance.common.jpa.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import tech.corefinance.common.model.GenericModel;
import tech.corefinance.common.service.SimpleSearchSupport;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Simple search support for all JPA entities.
 */
@Repository
@Slf4j
@ConditionalOnProperty(prefix = "tech.corefinance.common.enabled", name = "default-simple-search", havingValue = "true",
    matchIfMissing = true)
public class EntitySimpleSearchSupport implements SimpleSearchSupport<GenericModel<?>> {

    private Map<EntityType<?>, Set<SingularAttribute<?, ?>>> supportedAttributes;
    @Autowired
    private EntityManager entityManager;

    /**
     * Constructor will query all mapped entity and create list attribute that can we search by string.
     * @param entityManager JPA EntityManager object.
     */
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
        return supportedAttributes.entrySet().stream().filter(e -> e.getKey().getJavaType()
                .isAssignableFrom(clzz)).findFirst().isPresent();
    }

    @Override
    public Page<GenericModel<?>> searchByTextAndPage(Class<? extends GenericModel<?>> clzz, String searchText,
                                                     Pageable pageable) {
        var sql = buildSearchSql(clzz);
        var countSql = "SELECT count(o) " + sql;
        log.debug("Count SQL [{}]", countSql);
        sql = appendSortToSql(sql, pageable.getSort());
        log.debug("Search SQL [{}]", sql);
        var countQuery = entityManager.createQuery(countSql, Long.class);
        countQuery.setParameter("searchText", "%" + searchText + "%");
        long count = countQuery.getSingleResult();

        var query = entityManager.createQuery(sql, clzz);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        query.setParameter("searchText", "%" + searchText + "%");
        var list = (List<GenericModel<?>>) query.getResultList();
        return new PageImpl<>(list, pageable, count);
    }

    @Override
    public List<GenericModel<?>> searchByTextAndSort(Class<? extends GenericModel<?>> clzz, String searchText,
                                                     Sort sort) {
        var sql = buildSearchSql(clzz);
        sql = appendSortToSql(sql, sort);
        log.debug("Search SQL [{}]", sql);
        var query = entityManager.createQuery(sql, clzz);
        query.setParameter("searchText", "%" + searchText + "%");
        return (List<GenericModel<?>>) query.getResultList();
    }

    private boolean isEnumSupportSearch(Class<?> entityClass, SingularAttribute<?, ?> attribute) {
        var attributeName = attribute.getName();
        log.debug("Enum Property [{}]", attributeName);

        Field field;
        Method getterMethod;
        try {
            getterMethod = entityClass.getMethod(
                    "get" + attribute.getName().substring(0, 1).toUpperCase() + attribute.getName().substring(1));

            if (getterMethod.isAnnotationPresent(Enumerated.class)) {
                Enumerated enumeratedAnnotation = getterMethod.getAnnotation(Enumerated.class);
                if (enumeratedAnnotation.value() == EnumType.STRING) {
                    log.debug(
                            "Method [{}] of class [{}] has annotation Enumerated with EnumType.STRING! This field support search!",
                            getterMethod.getName(), entityClass.getName());
                    return true;
                }
            }

            try {
                field = entityClass.getDeclaredField(attribute.getName());
                if (field.isAnnotationPresent(Enumerated.class)) {
                    Enumerated enumeratedAnnotation = field.getAnnotation(Enumerated.class);
                    if (enumeratedAnnotation.value() == EnumType.STRING) {
                        log.debug(
                                "Field [{}] of class [{}] has annotation Enumerated with EnumType.STRING!" +
                                        "This field support search!", field.getName(), entityClass.getName());
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

    private String buildSearchSql(Class<?> clzz) {
        StringBuilder sqlBuilder = new StringBuilder("FROM ").append(clzz.getSimpleName()).append(" o where ");
        var optional =
                supportedAttributes.entrySet().stream().filter(et -> et.getKey().getJavaType().isAssignableFrom(clzz))
                        .findFirst();
        var et = optional.get();

        var attributes = et.getValue();
        int index = 0;
        for (var a : attributes) {
            if (index > 0) {
                sqlBuilder.append(" OR ");
            }
            sqlBuilder.append("lower(").append(a.getName()).append(") like lower(:searchText)");
            index++;
        }
        return sqlBuilder.toString();
    }

    private String appendSortToSql(String sql, Sort sort) {
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
}
