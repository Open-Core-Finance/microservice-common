package tech.corefinance.common.service;

public interface EntityInitializerHandler<T>  {
    T initializeEntity(T entity, boolean overrideIfExisted);
}
