package tech.corefinance.common.model;

import java.io.Serializable;

public interface AuditableEntity<A extends Serializable> {
    A getCreatedBy();
    A getLastModifiedBy();
    void setCreatedBy(A createdBy);
    void setLastModifiedBy(A lastModifiedBy);
}
