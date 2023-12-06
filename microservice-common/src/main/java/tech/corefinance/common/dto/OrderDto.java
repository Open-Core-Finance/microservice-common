package tech.corefinance.common.dto;

import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
public class OrderDto {
    private Sort.Direction direction;
    private String property;
    private boolean ignoreCase;

    public static OrderDto fromOrder(Sort.Order order) {
        var result = new OrderDto();
        result.direction = order.getDirection();
        result.ignoreCase = order.isIgnoreCase();
        result.property = order.getProperty();;
        return result;
    }
}
