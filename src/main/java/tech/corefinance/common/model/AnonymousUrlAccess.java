package tech.corefinance.common.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("anonymous_url_access")
@Table(name = "anonymous_url_access")
@Entity
public class AnonymousUrlAccess implements GenericModel<String> {
    @Id
    @jakarta.persistence.Id
    private String id;

    @Transient
    public String getUrl() {
        return getId();
    }
}
