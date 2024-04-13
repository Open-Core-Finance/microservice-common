package tech.corefinance.common.repository;

import java.util.UUID;

public class InMemoryStringIdGenerator implements InMemoryIdGenerator<String> {
    @Override
    public String generateId() {
        return UUID.randomUUID().toString();
    }
}
