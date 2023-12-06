package tech.corefinance.common.mongodb.service;

public interface EntityInitializerHandler<T>  {
    T initializeEntity(T entity, boolean overrideIfExisted);
}
