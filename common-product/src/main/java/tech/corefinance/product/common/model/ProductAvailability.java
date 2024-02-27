package tech.corefinance.product.common.model;

import lombok.Data;
import tech.corefinance.product.common.enums.ProductAvailabilityMode;

import java.util.List;

@Data
public class ProductAvailability {

    private ProductAvailabilityMode availabilityMode;
    private List<String> modeInfo;
}
