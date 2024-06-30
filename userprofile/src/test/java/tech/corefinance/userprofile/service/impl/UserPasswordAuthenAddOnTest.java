package tech.corefinance.userprofile.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import tech.corefinance.userprofile.app.TestUserProfileApplication;

import java.util.HashMap;

@SpringBootTest(classes = TestUserProfileApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles({"common", "default", "unittest"})
@ComponentScan(basePackages = {"tech.corefinance"})
@Slf4j
public class UserPasswordAuthenAddOnTest {

    @Autowired
    private UserPasswordAuthenAddOn userPasswordAuthenAddOn;

    @BeforeAll
    public static void beforeAll() {
        System.setProperty("LIQUIBASE_TEST_CHANGELOG", "classpath:test-h2-db/changelog/sql-changelog.xml");
    }

    @Test
    public void testAuthenticate() {
         assertNotNull(userPasswordAuthenAddOn.authenticate("admin", "admin", new HashMap<>()), "Verify initial password ok!");
    }
}
