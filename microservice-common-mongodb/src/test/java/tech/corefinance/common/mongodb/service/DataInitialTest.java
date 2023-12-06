package tech.corefinance.common.mongodb.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tech.corefinance.common.model.Permission;
import tech.corefinance.common.mongodb.support.app.TestCommonApplication;
import tech.corefinance.common.service.PermissionService;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = TestCommonApplication.class)
@ActiveProfiles({"common", "default", "unittest"})
public class DataInitialTest {

    @Autowired
    private PermissionInitialService mongoPermissionService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void test_initial_default_data() throws IOException {
        var result = mongoPermissionService.initializationDefaultData();
        var permissions = (List<Permission>) result.get("permission");
        assertEquals(3, permissions.size());
    }
}
