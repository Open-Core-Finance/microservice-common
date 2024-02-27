package tech.corefinance.product.common.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tech.corefinance.product.common.enums.AccountState;

@Data
public class ProductNewAccountSetting {
    @NotNull(message = "new_account_type_null")
    private ProductNewAccountSettingType type;
    private String randomPatternTemplate;
    private int increasementStartingFrom;
    private AccountState initialState;
}
