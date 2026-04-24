package tech.corefinance.common.export.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.bind.annotation.RequestMethod;
import tech.corefinance.common.model.ResourceAction;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResourceActionTest extends ResourceAction {

    private LocalDateTime datetime;

    public ResourceActionTest(String resourceType, String action, String url, RequestMethod requestMethod, String description,
                              boolean noAuthenRequired, String originUrl) {
        super(resourceType, action, url, requestMethod, description, noAuthenRequired, originUrl);
        this.datetime = LocalDateTime.now();
    }
}
