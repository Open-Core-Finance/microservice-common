package tech.corefinance.common.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Sort.Order;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

@Component
public class OrderListConverter implements Converter<String, List<Order>>, Formatter<List<Order>> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<Order> convert(String value) {
        List<Order> orders = new LinkedList<>();
        try {
            List<LinkedHashMap<String, String>> orderMap =
                    objectMapper.readValue(value, new TypeReference<>() {});
            logger.debug("Order map {}", orderMap);
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
        } catch (IOException | IllegalArgumentException e) {
            logger.error("Error", e);
        }
        return orders;
    }

    @Override
    public String print(List<Order> object, Locale locale) {
        try {
            logger.info("Calling custom formatter for List<Order> {}! Locale ignored!", object);
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error("Error", e);
            return "[]";
        }
    }

    @Override
    public List<Order> parse(String text, Locale locale) throws ParseException {
        logger.info("Calling custom formatter to parse {] to List<Order>! Locale ignored!", text);
        return convert(text);
    }

}
