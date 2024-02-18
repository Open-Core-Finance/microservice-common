package tech.corefinance.common.model;

public interface ModifiedDateTrackedEntity<T> {
    T getCreatedDate();
    T getLastModifiedDate();
    void setCreatedDate(T createdDate);
    void setLastModifiedDate(T lastModifiedDate);
}
