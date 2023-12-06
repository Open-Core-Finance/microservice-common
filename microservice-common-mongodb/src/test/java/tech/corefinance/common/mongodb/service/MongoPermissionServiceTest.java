package tech.corefinance.common.mongodb.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.corefinance.common.service.PermissionService;
import tech.corefinance.common.service.PermissionServiceImpl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MongoPermissionServiceTest {

    private PermissionService mongoPermissionService;

    @BeforeEach
    public void setUp() {
        mongoPermissionService = new PermissionServiceImpl();
    }

    @Test
    public void test_newPermission() {
        assertNotNull(mongoPermissionService.createEntityObject());
    }
}
