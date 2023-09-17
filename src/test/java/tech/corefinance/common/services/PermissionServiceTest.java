package tech.corefinance.common.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tech.corefinance.common.test.support.app.TestCommonApplication;
import tech.corefinance.common.test.support.service.TestPermissionService;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

@SpringBootTest(classes = TestCommonApplication.class)
@ActiveProfiles({"common", "default", "unittest"})
public class PermissionServiceTest {

    @Autowired
    private TestPermissionService permissionService;

    @Test
    public void test_initialDefaultData() throws IOException {
        var initialized = permissionService.initializationDefaultData();
        assertEquals(2, initialized.size());
    }

}
