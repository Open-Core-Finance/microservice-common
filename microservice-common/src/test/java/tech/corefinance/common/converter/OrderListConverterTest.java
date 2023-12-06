package tech.corefinance.common.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.data.domain.Sort;

import java.text.ParseException;
import java.util.List;

public class OrderListConverterTest {

    private OrderListConverter orderListConverter;

    @BeforeEach
    public void setup() {
        orderListConverter = new OrderListConverter();
    }

    @Test
    public void test_parse_print() throws ParseException {
        String input = "[{\"direction\": \"DESC\", \"property\": \"id\"}, {\"direction\": \"ASC\", \"property\": \"name\"}]";
        List<Sort.Order> orders = orderListConverter.parse(input, null);
        assertEquals(2, orders.size());
        var order = orders.get(0);
        assertEquals("id", order.getProperty());
        assertEquals(Sort.Direction.DESC, order.getDirection());
        var output = orderListConverter.print(orders, null);
        assertTrue(output.contains("\"property\":\"id\""));
    }
}
