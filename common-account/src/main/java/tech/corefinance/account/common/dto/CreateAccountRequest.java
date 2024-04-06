package tech.corefinance.account.common.dto;

import lombok.Data;
import tech.corefinance.common.model.CreateUpdateDto;

@Data
public abstract class CreateAccountRequest implements CreateUpdateDto<String> {

    private String id;
    private String name;
    private String categoryId;
    private String typeId;
    private String description;
    private String[] supportedCurrencies;
    private String productId;
}
