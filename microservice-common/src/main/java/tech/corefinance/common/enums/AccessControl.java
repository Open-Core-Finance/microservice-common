package tech.corefinance.common.enums;

/**
 * Supported permission access control
 */
public enum AccessControl {
    /**
     * Allow to access API.
     */
    ALLOWED,
    /**
     * Denied to access API.
     */
    DENIED,
    /**
     * Allow if user is the owner of member of the resource. <br/>
     * When setting access to this option, framework will scan for PermissionResource annotation.<br/>
     * Need to implement ResourceOwnerVerifier for resource type defined in PermissionResource.
     */
    ALLOWED_SPECIFIC_RESOURCES,
    /**
     * Denied if user is the owner of member of the resource. <br/>
     * When setting access to this option, framework will scan for PermissionResource annotation.<br/>
     * Need to implement ResourceOwnerVerifier for resource type defined in PermissionResource.
     */
    DENIED_SPECIFIC_RESOURCES,
    /**
     * Skip framework permission check.
     */
    MANUAL_CHECK
}
