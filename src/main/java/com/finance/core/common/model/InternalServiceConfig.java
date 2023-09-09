package com.finance.core.common.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.ZonedDateTime;

@Data
public class InternalServiceConfig implements GenericModel<String> {
    @Id
    private String id;
    @NotNull
    private String serviceName;
    @NotNull
    private String apiKey;
    private boolean activated = true;
    @LastModifiedDate
    private ZonedDateTime lastModifiedDate;
    @CreatedDate
    private ZonedDateTime createdDate;
}
