package tech.corefinance.common.repository;

import java.util.UUID;

public class InMemoryUuidGenerator implements InMemoryIdGenerator<UUID> {
    @Override
    public UUID generateId() {
        return UUID.randomUUID();
    }
}
