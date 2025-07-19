package tech.corefinance.common.service;

import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.util.StringUtils;
import tech.corefinance.common.context.ApplicationContextHolder;
import tech.corefinance.common.ex.ResourceNotFound;
import tech.corefinance.common.ex.ServiceProcessingException;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.common.model.GenericModel;
import tech.corefinance.common.repository.CommonResourceRepository;
import tech.corefinance.common.util.CoreFinanceUtil;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

public interface CommonService<I extends Serializable, T extends GenericModel<I>, R extends CommonResourceRepository<T, I>> {

    /**
     * Get repository.
     *
     * @return Repository object that this service manage.
     */
    R getRepository();

    /**
     * Create new entity object.
     *
     * @return new created entity object
     */
    default T createEntityObject() {
        try {
            return findEntityClass().getConstructor().newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new ServiceProcessingException(e.getMessage(), e);
        }
    }

    /**
     * This method support for create or update entity. All properties which are matched name and type already copied. <br />
     * This on only do addition copy for custom field from DTO which is not matched property name with entity.
     *
     * @param source DTO object
     * @param dest   Entity object
     * @param <D>    DTO type
     */
    default <D extends CreateUpdateDto<I>> T copyAdditionalPropertiesFromDtoToEntity(D source, T dest) {
        return dest;
    }

    /**
     * This method support for create or update entity. Validate entities object.<br />
     *
     * @param source DTO object
     * @param dest   Entity object
     * @param <D>    DTO type
     */
    default <D extends CreateUpdateDto<I>> T customEntityValidation(D source, T dest) {
        return dest;
    }

    /**
     * Delete an entity from database.
     *
     * @param itemId Entity ID
     * @return True only if delete successfully.
     */
    default boolean deleteEntity(I itemId) {
        R repository = getRepository();
        Optional<T> optional = repository.findById(itemId);
        if (optional.isPresent()) {
            T item = optional.get();
            Object dataToKeep = beforeItemDeleted(item);
            repository.delete(item);
            afterItemDeleted(item, dataToKeep);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Create or update database entity. Create when dto's ID is null, otherwise call update.
     *
     * @param dto DTO object.
     * @param <D> DTO type
     * @return Create entity.
     */
    default <D extends CreateUpdateDto<I>> T createOrUpdateEntity(D dto) {
        var logger = LoggerFactory.getLogger(getClass());
        logger.info("Entering createOrUpdateEntity...");
        T entity;
        var repository = getRepository();
        Object keptData = null;
        if (dto.getId() != null) {
            logger.info("Entity ID not empty! Checking if existed in DB or not...");
            Optional<T> optional = repository.findById(dto.getId());
            if (optional.isPresent()) {
                logger.info("Entity found!");
                entity = optional.get();
                keptData = beforeEditEntityAttributes(entity);
            } else {
                logger.info("Entity not found! Creating new entity...");
                entity = createEntityObject();
                entity.setId(dto.getId());
            }
        } else {
            logger.info("Entity ID empty! Creating new entity...");
            entity = createEntityObject();
        }
        // Copy matched properties
        logger.info("Copying matched properties from DTO to entity...");
        BeanUtils.copyProperties(dto, entity);
        logger.info("Calling copyAdditionalPropertiesFromDtoToEntity...");
        entity = copyAdditionalPropertiesFromDtoToEntity(dto, entity);
        logger.info("Calling customEntityValidation...");
        entity = customEntityValidation(dto, entity);
        logger.info("Save entity to DB...");
        entity = repository.save(entity);
        logger.info("Call afterEntitySaved and then response");
        afterEntitySaved(entity, keptData);
        // Response
        return entity;
    }

    /**
     * Search entities with search text, paging and sort orders.
     *
     * @param searchText Text to search. If empty, will not search by text.
     * @param pageSize   Page size. Skip paging if page size less than 1.
     * @param pageIndex  Page index (start from 0). Skip paging if page index les than 0.
     * @param orders     Sorting orders. Pass empty list if you don't want to sort.
     * @return Page of items.
     */
    default Page<T> searchData(String searchText, int pageSize, int pageIndex, List<Sort.Order> orders) {
        var logger = LoggerFactory.getLogger(getClass());
        logger.info("Search entities");
        var repository = getRepository();
        if (pageSize > 0 && pageIndex >= 0) {
            PageRequest pageRequest = PageRequest.of(pageIndex, pageSize, Sort.by(orders));
            if (StringUtils.hasText(searchText)) {
                logger.info("Search by page {} and page index {} and search text [{}]", pageSize, pageIndex, searchText);
                return searchByTextAndPage(searchText, pageRequest);
            } else {
                logger.info("Load by page {} and page index {} without search text.", pageSize, pageIndex);
                return repository.findAll(pageRequest);
            }
        } else {
            List<T> entities;
            Sort sort = Sort.by(orders);
            if (StringUtils.hasText(searchText)) {
                logger.info("Search by search text [{}] and order {}", searchText, orders);
                entities = searchByTextAndSort(searchText, sort);

            } else {
                logger.info("Load without search text and order {}", orders);
                entities = repository.findAll(sort);
            }
            return new PageImpl<>(entities);
        }
    }

    /**
     * Search by text and Pageable.
     *
     * @param searchText Search text for internal use. Do not need to check empty for this case.
     * @param pageable   Page info for internal use. Do not need to check null for this case.
     * @return Page of items.
     * @throws UnsupportedOperationException if the service does not support search by text (Mean subclasses does not override this method).
     */
    default Page<T> searchByTextAndPage(String searchText, Pageable pageable) {
        var context = ApplicationContextHolder.getInstance().getApplicationContext();
        var map = context.getBeansOfType(SimpleSearchSupport.class);
        Class<?> entityType = findEntityClass();
        for (var entry : map.entrySet()) {
            var searchSupport = entry.getValue();
            //noinspection unchecked
            if (searchSupport.isSupported(entityType)) {
                //noinspection unchecked
                return searchSupport.searchByTextAndPage(entityType, searchText, pageable);
            }
        }
        throw new UnsupportedOperationException("Not support search by text! Please override searchByTextAndPage and searchByTextAndSort");
    }

    /**
     * Search by text and Pageable.
     *
     * @param searchText Search text for internal use. Do not need to check empty for this case.
     * @param sort       Sort info for internal use. Do not need to check null for this case.
     * @return List of items.
     * @throws UnsupportedOperationException if the service does not support search by text (Mean subclasses does not override this method).
     */
    default List<T> searchByTextAndSort(String searchText, Sort sort) {
        var context = ApplicationContextHolder.getInstance().getApplicationContext();
        var map = context.getBeansOfType(SimpleSearchSupport.class);
        Class<?> entityType = findEntityClass();
        for (var entry : map.entrySet()) {
            var searchSupport = entry.getValue();
            //noinspection unchecked
            if (searchSupport.isSupported(entityType)) {
                //noinspection unchecked
                return searchSupport.searchByTextAndSort(entityType, searchText, sort);
            }
        }
        throw new UnsupportedOperationException("Not support search by text! Please override searchByTextAndPage and searchByTextAndSort");
    }

    /**
     * Get entity details.
     *
     * @param entityId Entity ID.
     * @return Entity object.
     */
    default T getEntityDetails(I entityId) {
        return getRepository().findById(entityId).orElseThrow(() -> new ResourceNotFound("Cannot find entity for ID " + entityId));
    }

    default Class<T> findEntityClass() {
        var context = ApplicationContextHolder.getInstance().getApplicationContext();
        //noinspection unchecked
        return (Class<T>) context.getBean(CoreFinanceUtil.class).findEntityTypeFromCommonService(getClass());
    }

    /**
     * This method will be called after persist entity to database (both create and edit).<br />
     *
     * @param entity Entity object
     * @param data   Object received from beforeEditEntityAttributes method call. This always null for create case. For edit case can be null or have custom data.
     */
    default void afterEntitySaved(T entity, Object data) {
    }

    /**
     * This method will be called when entify existing in database, and we're trying to update it in createOrUpdateEntity method.<br />
     * It runs right before copying attributes. <br/>
     *
     * @param entity Entity object
     * @return Object data that you want to keep to pass to afterEntitySaved.
     */
    default Object beforeEditEntityAttributes(T entity) {
        return null;
    }

    /**
     * This method will be called before delete entity in the database.<br />
     *
     * @param item Entity object
     * @return Object data that you want to keep to pass to afterItemDeleted.
     */
    default Object beforeItemDeleted(T item) {
        return null;
    }

    /**
     * This method will be called after delete entity in the database.<br />
     *
     * @param item Entity object
     * @param data Object received from beforeItemDeleted method call. This can be null or have custom data.
     */
    default void afterItemDeleted(T item, Object data) {
    }

    default List<T> loadEAllByIds(List<I> ids) {
        return getRepository().findAllById(ids);
    }
}
