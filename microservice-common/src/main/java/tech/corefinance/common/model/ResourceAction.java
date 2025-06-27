package tech.corefinance.common.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestMethod;

@Data
@NoArgsConstructor
public class ResourceAction implements CreateUpdateDto<String>, GenericModel<String> {
    public static final String COMMON_ACTION_ADD = "add";
    public static final String COMMON_DESCRIPTION_ADD = "Add a new data";
    public static final String COMMON_ACTION_UPDATE = "update";
    public static final String COMMON_DESCRIPTION_UPDATE = "Update data";
    public static final String COMMON_ACTION_DELETE = "delete";
    public static final String COMMON_DESCRIPTION_DELETE = "Delete data";
    public static final String COMMON_ACTION_LIST = "list";
    public static final String COMMON_DESCRIPTION_LIST = "List data";
    public static final String COMMON_ACTION_VIEW = "view";
    public static final String COMMON_DESCRIPTION_VIEW = "View data details";
    public static final String COMMON_ACTION_INITIAL = "initial";
    public static final String COMMON_DESCRIPTION_INITIAL = "Initial default data in database";

    private String id;
    private String resourceType;
    private String action = COMMON_ACTION_ADD;
    private String url;
    private RequestMethod requestMethod;
    private String description;
    private boolean noAuthenRequired;
    private String originUrl;

    public ResourceAction(String resourceType, String action, String url, RequestMethod requestMethod, String description,
            boolean noAuthenRequired, String originUrl) {
        this.resourceType = resourceType;
        this.action = action;
        this.url = url;
        this.requestMethod = requestMethod;
        this.description = description;
        this.noAuthenRequired = noAuthenRequired;
        if (originUrl == null) {
            originUrl = "";
        }
        this.originUrl = originUrl;
        this.id = (action + "_" + resourceType + "_" + originUrl.replace("/", "_").replace("{", "").replace("}", "")
                .replace("Id", "_id")).replace("-", "_");
        this.id = this.id.replace("__", "_");
    }
}
