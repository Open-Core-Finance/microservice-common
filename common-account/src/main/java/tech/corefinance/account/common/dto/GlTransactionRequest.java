package tech.corefinance.account.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.corefinance.account.common.entity.AccountTransaction;
import tech.corefinance.common.model.CreateUpdateDto;

@Data
@EqualsAndHashCode(callSuper = true)
public class GlTransactionRequest extends AccountTransaction implements CreateUpdateDto<String> {

    @Override
    public String getEntityId() {
        return getId();
    }

    @Override
    public void setEntityId(String entityId) {
        setId(entityId);
    }

    @Override
    @JsonIgnore
    public String getId() {
        return super.getId();
    }

    @Override
    @JsonIgnore
    public void setId(String id) {
        super.setId(id);
    }
}
