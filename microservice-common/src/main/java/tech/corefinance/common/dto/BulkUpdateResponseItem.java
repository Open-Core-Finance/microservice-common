package tech.corefinance.common.dto;

import tech.corefinance.common.ex.CustomConstraintViolation;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public interface BulkUpdateResponseItem {

    int STATUS_CREATED = 0;
    int STATUS_UPDATED = 1;
    int STATUS_DELETED = 2;

    int STATUS_ERROR = Integer.MAX_VALUE;

    int getStatus();

    void setStatus(int status);

    Serializable getId();

    Object getEntity();

    Map<String, Object> getAdditionalData();

    Set<CustomConstraintViolation> getConstraintViolations();
}
