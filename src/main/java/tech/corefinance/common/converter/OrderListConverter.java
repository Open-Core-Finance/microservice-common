package tech.corefinance.common.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Sort.Order;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class OrderListConverter implements CommonCustomConverter<String, List<Order>>, Formatter<List<Order>> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @SneakyThrows(JsonProcessingException.class)
    public List<Order> convert(String value) {
        List<Order> orders = new LinkedList<>();
        List<LinkedHashMap<String, String>> orderMap =
                objectMapper.readValue(value, new TypeReference<>() {});
        log.debug("Order map {}", orderMap);
        orderMap.forEach(map -> {
            Order order = null;
            String property = map.get("property");
            if ("ASC".equalsIgnoreCase(map.get("direction"))) {
                order = Order.asc(property);
            } else {
                order = Order.desc(property);
            }
            orders.add(order);
        });
        return orders;
    }

    @Override
    @SneakyThrows(JsonProcessingException.class)
    public String print(List<Order> object, Locale locale) {
        log.info("Calling custom formatter for List<Order> {}! Locale ignored!", object);
        return objectMapper.writeValueAsString(object);
    }

    @Override
    public List<Order> parse(String text, Locale locale) throws ParseException {
        log.info("Calling custom formatter to parse {] to List<Order>! Locale ignored!", text);
        return convert(text);
    }

}
