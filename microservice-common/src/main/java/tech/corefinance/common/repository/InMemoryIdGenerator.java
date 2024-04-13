package tech.corefinance.common.repository;

public interface InMemoryIdGenerator<ID> {
    ID generateId();
}
