package tech.corefinance.common.entity_author;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.bind.annotation.RequestMethod;
import tech.corefinance.common.model.GenericModel;

@Data
@Document("anonymous_url_access")
@Table(name = "anonymous_url_access")
@Entity
public class AnonymousUrlAccess implements GenericModel<String> {
    @Id
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String url;

    @Column(name = "request_method")
    @Enumerated(EnumType.STRING)
    private RequestMethod requestMethod;

}
