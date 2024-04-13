package tech.corefinance.common.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestMethod;

@Data
@NoArgsConstructor
public class ResourceAction implements CreateUpdateDto<String>, GenericModel<String> {
    public static final String COMMON_ACTION_ADD = "add";
    public static final String COMMON_ACTION_UPDATE = "update";
    public static final String COMMON_ACTION_DELETE = "delete";
    public static final String COMMON_ACTION_LIST = "list";
    public static final String COMMON_ACTION_VIEW = "view";
    public static final String COMMON_ACTION_INITIAL = "initial";

    private String id;
    private String resourceType;
    private String action = COMMON_ACTION_ADD;
    private String url;
    private RequestMethod requestMethod;

    public ResourceAction(String resourceType, String action, String url, RequestMethod requestMethod) {
        this.resourceType = resourceType;
        this.action = action;
        this.url = url;
        this.id = action + "-" + resourceType + "-" + url.replace("/", "_");
        this.requestMethod = requestMethod;
    }
}
