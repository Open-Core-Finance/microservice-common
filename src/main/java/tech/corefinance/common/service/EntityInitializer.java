package tech.corefinance.common.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.Resource;
import tech.corefinance.common.context.ApplicationContextHolder;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@FunctionalInterface
public interface EntityInitializer<T> {

    default List<T> initializeEntities(List<Resource> resources, boolean overrideIfExisted) throws IOException {
        var context = ApplicationContextHolder.getInstance().getApplicationContext();
        var objectMapper = context.getBean(ObjectMapper.class);
        List<T> result = new LinkedList<>();
        var referenceType = new TypeReference<List<T>>() {};
        for (var resource : resources) {
            var entities = objectMapper.readValue(resource.getInputStream(), referenceType);
            for (var entity : entities) {
                result.add(initializeEntity(entity, overrideIfExisted));
            }
        }
        return result;
    }

    T initializeEntity(T entity, boolean overrideIfExisted);
}
