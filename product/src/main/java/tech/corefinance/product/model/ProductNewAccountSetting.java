package tech.corefinance.product.model;

import lombok.Data;
import tech.corefinance.product.enums.AccountState;

@Data
public class ProductNewAccountSetting {
    private String typeName;
    private String typeConfig;
    private AccountState initialState;
}
