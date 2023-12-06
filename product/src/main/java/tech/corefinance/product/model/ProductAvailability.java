package tech.corefinance.product.model;

import lombok.Data;
import tech.corefinance.product.enums.ProductAvailabilityMode;

import java.util.List;

@Data
public class ProductAvailability {

    private ProductAvailabilityMode availabilityMode;
    private List<String> modeInfo;
}
