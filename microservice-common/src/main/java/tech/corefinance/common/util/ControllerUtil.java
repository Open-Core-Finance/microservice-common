package tech.corefinance.common.util;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import tech.corefinance.common.converter.ControllerOutputConverter.DetailsOutputConverter;
import tech.corefinance.common.converter.ControllerOutputConverter.ListOutputConverter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class ControllerUtil {
    private List<ListOutputConverter<?, ?>> listOutputConverters;
    private List<DetailsOutputConverter<?, ?>> detailsOutputConverters;

    public <S> ListOutputConverter<S, ?> findListOutputConverter(Class<S> sourceType) {
        log.debug("List output controller {}", listOutputConverters);
        for (ListOutputConverter<?, ?> c : listOutputConverters) {
            Class<?> actualSourceType = extractSourceType(c, ListOutputConverter.class);
            log.debug("Converter {}", c);
            log.debug("Actual source type {}", actualSourceType);
            log.debug("Input source type {}", sourceType);
            if (actualSourceType != null && actualSourceType.equals(sourceType)) {
                log.debug("matched [true]");
                //noinspection unchecked
                return (ListOutputConverter<S, ?>) c;
            } else {
                log.debug("matched [false]");
            }
        }
        return null;
    }

    public <S> DetailsOutputConverter<S, ?> findDetailsOutputConverter(Class<S> sourceType) {
        for (DetailsOutputConverter<?, ?> c : detailsOutputConverters) {
            Class<?> actualSourceType = extractSourceType(c, DetailsOutputConverter.class);
            if (actualSourceType != null && actualSourceType.equals(sourceType)) {
                //noinspection unchecked
                return (DetailsOutputConverter<S, ?>) c;
            }
        }
        return null;
    }

    public <T> ListOutputConverter<T, ?> findListOutputConverter(List<T> list) {
        if (list.isEmpty()) {
            return null;
        }
        T firstItem = null;
        var iterator = list.iterator();
        while (firstItem == null && iterator.hasNext()) {
            firstItem = iterator.next();
        }
        if (firstItem == null) {
            return null;
        }
        //noinspection unchecked
        return findListOutputConverter((Class<T>) firstItem.getClass());
    }

    public <T> DetailsOutputConverter<T, ?> findDetailsOutputConverter(T element) {
        if (element == null) {
            return null;
        }
        //noinspection unchecked
        return findDetailsOutputConverter((Class<T>) element.getClass());
    }

    private static Class<?> extractSourceType(Converter<?, ?> converter, Class<?> expectedInterface) {
        Class<?> clazz = converter.getClass();

        // Step 1: Check if the class directly implements Converter<S, ?>
        for (Type iface : clazz.getGenericInterfaces()) {
            if (iface instanceof ParameterizedType pt && pt.getRawType() == expectedInterface) {
                Type sourceType = pt.getActualTypeArguments()[0];
                if (sourceType instanceof Class<?> st) {
                    return st;
                }
            }
        }

        // Step 2: Handle anonymous classes or inheritance (deep check)
        Type genericSuperclass = clazz.getGenericSuperclass();
        while (genericSuperclass != null) {
            if (genericSuperclass instanceof ParameterizedType pt && pt.getRawType() == expectedInterface) {
                Type sourceType = pt.getActualTypeArguments()[0];
                if (sourceType instanceof Class<?> st) {
                    return st;
                }
            }

            if (genericSuperclass instanceof Class<?> superClass) {
                genericSuperclass = superClass.getGenericSuperclass();
            } else {
                break;
            }
        }

        // Could not determine generic type
        return null;
    }
}
