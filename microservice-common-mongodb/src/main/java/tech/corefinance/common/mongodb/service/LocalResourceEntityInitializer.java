package tech.corefinance.common.mongodb.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import tech.corefinance.common.context.ApplicationContextHolder;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Entity Initializer.
 * @param <T> Entity type.
 */
@Slf4j
@AllArgsConstructor
public class LocalResourceEntityInitializer<T> {

    private TypeReference<List<T>> jsonConvertType;
    private EntityInitializerHandler<T> handler;

    /**
     * Initialie entity from resources.
     * @param resources Resources
     * @param overrideIfExisted Override if entity existed in DB.
     * @return List of entities.
     * @throws IOException If cannot read from resources.
     */
    public List<T> initializeEntities(List<Resource> resources, boolean overrideIfExisted) throws IOException {
        var context = ApplicationContextHolder.getInstance().getApplicationContext();
        var objectMapper = context.getBean(ObjectMapper.class);
        List<T> result = new LinkedList<>();
        for (var resource : resources) {
            log.info("Parsing data from resource file [{}]", resource.getFilename());
            var entities = objectMapper.readValue(resource.getInputStream(), jsonConvertType);
            for (var entity : entities) {
                log.info("Entity: {}", entity);
                var initResult = handler.initializeEntity(entity, overrideIfExisted);
                log.info("Init result: {}", entity);
                result.add(initResult);
            }
        }
        return result;
    }
}
