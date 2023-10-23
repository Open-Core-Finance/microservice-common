package tech.corefinance.common.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;

@Document(collection = "internal_service_config")
@Data
@Table(name = "internal_service_config")
@Entity
public class InternalServiceConfig implements GenericModel<String> {
    @Id
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.UUID)
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
