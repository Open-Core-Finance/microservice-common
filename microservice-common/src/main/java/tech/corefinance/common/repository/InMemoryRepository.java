package tech.corefinance.common.repository;

import org.springframework.data.domain.*;
import org.springframework.data.repository.query.FluentQuery;
import tech.corefinance.common.ex.ServiceProcessingException;
import tech.corefinance.common.model.GenericModel;
import tech.corefinance.common.util.CoreFinanceUtil;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

public class InMemoryRepository<T extends GenericModel<ID>, ID extends Serializable> implements CommonResourceRepository<T, ID> {

    private static final String VALIDATION_FAIL_MESSAGE = "entity_validation_fail";

    protected Map<ID, T> data = new HashMap<>();
    protected Long maxGeneratedId = 0L;
    protected Class<T> entityClass;
    protected Class<ID> idClass;
    protected final Object lock = new Object();
    protected final CoreFinanceUtil coreFinanceUtil;

    public InMemoryRepository(Class<T> entityClass, Class<ID> idClass, CoreFinanceUtil coreFinanceUtil) {
        this.entityClass = entityClass;
        this.idClass = idClass;
        this.coreFinanceUtil = coreFinanceUtil;
    }

    public boolean validateEntity(T entity) {
        return true;
    }

    @Override
    public <S extends T> S save(S entity) {
        if (!validateEntity(entity)) {
            throw new ServiceProcessingException(VALIDATION_FAIL_MESSAGE);
        }
        var id = entity.getId();
        if (id == null) {
            id = generateId();
            entity.setId(id);
        } else {
            var oldData = data.get(id);
            if (oldData != null) {
                data.remove(id);
            }
            data.put(id, entity);
        }
        return entity;
    }

    @SuppressWarnings("unchecked")
    protected ID generateId() {
        InMemoryIdGenerator<ID> generator = null;
        if (idClass.equals(UUID.class)) {
            generator = (InMemoryIdGenerator<ID>) new InMemoryUuidGenerator();
        } else if (idClass.equals(String.class)) {
            generator = (InMemoryIdGenerator<ID>) new InMemoryStringIdGenerator();
        }
        if (generator != null) {
            return generator.generateId();
        }
        if (idClass.equals(Long.class) || idClass.equals(long.class) ||
                idClass.equals(int.class) || idClass.equals(Integer.class)) {
            synchronized (lock) {
                maxGeneratedId++;
            }
            return (ID) maxGeneratedId;
        }
        throw new ServiceProcessingException("unsupported_id_type");
    }

    @Override
    public <S extends T> List<S> saveAll(Iterable<S> entities) {
        List<S> result = new LinkedList<>();
        entities.forEach(e -> {
            result.add(save(e));
        });
        return result;
    }

    @Override
    public Optional<T> findById(ID id) {
        var value = data.get(id);
        if (value != null) {
            return Optional.of(value);
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(ID id) {
        return data.containsKey(id);
    }

    @Override
    public List<T> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public List<T> findAllById(Iterable<ID> ids) {
        List<T> result = new LinkedList<>();
        ids.forEach( id -> result.add(data.get(id)));
        return result;
    }

    @Override
    public long count() {
        return data.size();
    }

    @Override
    public void deleteById(ID id) {
        data.remove(id);
    }

    @Override
    public void delete(T entity) {
        data.remove(entity.getId());
    }

    @Override
    public void deleteAllById(Iterable<? extends ID> ids) {
        ids.forEach(data::remove);
    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {
        entities.forEach(e -> data.remove(e.getId()));
    }

    @Override
    public void deleteAll() {
        data.clear();
    }

    protected List<T> sort(List<T> entities, Sort sort) {
        if (sort != null && sort.isSorted()) {
            entities.sort((o1, o2) -> {
                for (var o : sort) {
                    try {
                        var field1Access = coreFinanceUtil.accessField(o1, o1.getClass(), o.getProperty());
                        field1Access.setAccessible(true);
                        var field2Access = coreFinanceUtil.accessField(o2, o2.getClass(), o.getProperty());
                        field2Access.setAccessible(true);
                        var field1Val = coreFinanceUtil.triggerGetFieldValue(field1Access, o1, o1.getClass());
                        var field2Val = coreFinanceUtil.triggerGetFieldValue(field1Access, o2, o2.getClass());
                        int result = compare(field1Val, field2Val, o.isAscending());
                        if (result == 0) {
                            continue;
                        } else {
                            return result;
                        }
                    } catch (Exception ex) {
                        throw new ServiceProcessingException(ex.getMessage(), ex);
                    }
                }
                return 0;
            });
        }
        return entities;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected int compare(Object val1, Object val2, boolean isAscending) {
        if (val1 == null || val2 == null) {
            if (val1 == val2) {
                return 0;
            } else if (val1 == null) {
                return isAscending ? -1 : 1;
            } else {
                return isAscending ? 1 : -1;
            }
        } else {
            int comparedVal;
            if (val1 instanceof Comparable v1) {
                comparedVal = v1.compareTo(val2);
            } else {
                comparedVal = val1.toString().compareTo(val2.toString());
            }
            if (comparedVal == 0) {
                return 0;
            } else {
                return isAscending ? comparedVal : (-1 * comparedVal);
            }
        }
    }

    @Override
    public List<T> findAll(Sort sort) {
        return sort(findAll(), sort);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        var result = findAll();
        var pageSize = pageable.getPageSize();
        var pageNumber = pageable.getPageNumber();
        var count = result.size();
        if (pageable.isPaged() && pageSize > 0 && pageNumber >= 0 && count > 0) {
            var numberOfPage = (count / pageSize) + ((count % pageSize) > 0 ? 1 : 0);
            if (numberOfPage <= 0) {
                return new PageImpl<>(sort(result, pageable.getSort()));
            }
            if (pageNumber >= numberOfPage) {
                pageNumber = numberOfPage - 1;
            }
            var offset = pageNumber * pageSize;
            var newPageable = PageRequest.of(pageNumber, pageSize);
            var toIndex = offset + pageSize;
            if (toIndex >= count) {
                toIndex = count - 1;
            }
            return new PageImpl<>(sort(result.subList(offset, toIndex), pageable.getSort()), newPageable, count);
        }
        return new PageImpl<>(sort(result, pageable.getSort()));
    }

    @Override
    public <S extends T> Optional<S> findOne(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends T> Iterable<S> findAll(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends T> Iterable<S> findAll(Example<S> example, Sort sort) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends T> long count(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends T> boolean exists(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends T, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        throw new UnsupportedOperationException();
    }
}
