package tech.corefinance.common.mongodb.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import tech.corefinance.common.entity_author.Permission;
import tech.corefinance.common.mongodb.support.app.TestCommonApplication;

import java.io.IOException;
import java.util.List;

@SpringBootTest(classes = TestCommonApplication.class)
@ActiveProfiles({"common", "default", "unittest"})
@WebAppConfiguration
public class DataInitialTest {

    @Autowired
    private PermissionInitialService mongoPermissionService;

    @BeforeEach
    public void setUp() {
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test_initial_default_data() throws IOException {
        var result = mongoPermissionService.initializationDefaultData();
        var permissions = (List<Permission>) result.get("permission");
        Assertions.assertEquals(3, permissions.size());
    }
}
