package tech.corefinance.account.gl.dto;

import lombok.Data;
import tech.corefinance.account.common.dto.CreateAccountRequest;
import tech.corefinance.common.model.CreateUpdateDto;

@Data
public class CreateGlAccountRequest extends CreateAccountRequest implements CreateUpdateDto<String> {

    private String id;
    private String name;
    private String categoryId;
    private String categoryName;
    private String typeId;
    private String typeName;
    private String description;
    private String[] supportedCurrencies;
    private String productId;
}
