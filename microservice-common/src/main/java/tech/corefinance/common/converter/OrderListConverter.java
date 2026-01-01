package tech.corefinance.common.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort.Order;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class OrderListConverter implements CommonCustomConverter<String, List<Order>>, Formatter<List<Order>> {

    private JsonMapper jsonMapper = new JsonMapper();

    @Override
    public List<Order> convert(String value) {
        List<Order> orders = new LinkedList<>();
        List<LinkedHashMap<String, String>> orderMap =
                jsonMapper.readValue(value, new TypeReference<>() {});
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
    public String print(List<Order> object, Locale locale) {
        log.info("Calling custom formatter for List<Order> {}! Locale ignored!", object);
        return jsonMapper.writeValueAsString(object);
    }

    @Override
    public List<Order> parse(String text, Locale locale) throws ParseException {
        log.info("Calling custom formatter to parse {] to List<Order>! Locale ignored!", text);
        return convert(text);
    }

}
