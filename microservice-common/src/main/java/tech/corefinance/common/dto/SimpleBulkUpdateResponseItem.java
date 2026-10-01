package tech.corefinance.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.corefinance.common.ex.CustomConstraintViolation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleBulkUpdateResponseItem implements BulkUpdateResponseItem {
    private Serializable id;
    @JsonIgnore
    private Object entity;
    private int status;
    private Map<String, Object> additionalData = new HashMap<>();
    private Set<CustomConstraintViolation> constraintViolations;
}
