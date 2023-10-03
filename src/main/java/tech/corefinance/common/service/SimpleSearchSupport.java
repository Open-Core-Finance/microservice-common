package tech.corefinance.common.service;

import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public interface SimpleSearchSupport<T> {

    default boolean isSupported(Class<?> clzz) {
        var log = LoggerFactory.getLogger(getClass());
        var type = getClass().getGenericSuperclass();
        log.info("Generic supper class [{}]", type);
        if (type instanceof ParameterizedType) {
            Class<?> param = (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0];
            var matched = param.isAssignableFrom(clzz);
            log.info("Generic runtime type [{}] is match with input type [{}] = {}", type, clzz, matched);
            return matched;
        }
        log.info("Generic supper class [{}] is not ParameterizedType!!!", type);
        return false;
    }

    /**
     * Search by text and Pageable.
     * @param searchText Search text for internal use. Do not need to check empty for this case.
     * @param pageable Page info for internal use. Do not need to check null for this case.
     * @return Page of items.
     * @throws UnsupportedOperationException if the service does not support search by text (Mean subclasses does not override this method).
     */
    Page<T> searchByTextAndPage(Class<? extends T> clzz, String searchText, Pageable pageable);

    /**
     * Search by text and Pageable.
     * @param searchText Search text for internal use. Do not need to check empty for this case.
     * @param sort Sort info for internal use. Do not need to check null for this case.
     * @return List of items.
     * @throws UnsupportedOperationException if the service does not support search by text (Mean subclasses does not override this method).
     */
    List<T> searchByTextAndSort(Class<? extends T> clzz, String searchText, Sort sort);
}
