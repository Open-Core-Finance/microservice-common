package tech.corefinance.common.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import tech.corefinance.common.context.ApplicationContextHolder;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Entity Initializer.
 * @param <T> Entity type.
 */
public interface EntityInitializer<T> {

    /**
     * Initialie entity from resources.
     * @param resources Resources
     * @param overrideIfExisted Override if entity existed in DB.
     * @return List of entities.
     * @throws IOException If cannot read from resources.
     */
    default List<T> initializeEntities(List<Resource> resources, boolean overrideIfExisted) throws IOException {
        var log = LoggerFactory.getLogger(getClass());
        var context = ApplicationContextHolder.getInstance().getApplicationContext();
        var objectMapper = context.getBean(ObjectMapper.class);
        List<T> result = new LinkedList<>();
        for (var resource : resources) {
            var entities = objectMapper.readValue(resource.getInputStream(), getJsonReferenceType());
            for (var entity : entities) {
                log.info("Entity: {}", entity);
                var initResult = initializeEntity(entity, overrideIfExisted);
                log.info("Init result: {}", entity);
                result.add(initResult);
            }
        }
        return result;
    }

    /**
     * Check and save entity from resource.
     * @param entity Entity
     * @param overrideIfExisted Override is existed in DB.
     * @return Saved entity.
     */
    T initializeEntity(T entity, boolean overrideIfExisted);

    /**
     * Get TypeReference for JSON Parsing.
     * @return TypeReference&lt;List&lt;T&gt;&gt;.
     */
    TypeReference<List<T>> getJsonReferenceType();
}
