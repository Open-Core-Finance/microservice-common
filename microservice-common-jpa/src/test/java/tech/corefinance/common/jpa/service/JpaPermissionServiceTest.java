package tech.corefinance.common.jpa.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import tech.corefinance.common.jpa.test.support.TestCommonApplication;
import tech.corefinance.common.service.PermissionService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest(classes = TestCommonApplication.class)
@ActiveProfiles({"common", "default", "unittest"})
public class JpaPermissionServiceTest {

    private static final PostgreSQLContainer<?> postgresqlContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:16"))
                    .withDatabaseName("coffee_shop_order_test")
                    .withUsername("testuser")
                    .withPassword("testpassword");

    @Autowired
    private PermissionService permissionService;

    @BeforeAll
    public static void setUp() {
        // Start the PostgreSQL container before running tests
        postgresqlContainer.start();
        var url = postgresqlContainer.getJdbcUrl();
        System.setProperty("DB_URL", url);
        var userName = postgresqlContainer.getUsername();
        System.setProperty("DB_USERNAME", userName);
        var password = postgresqlContainer.getPassword();
        System.setProperty("DB_PASSWORD", password);
    }

    @AfterAll
    public static void tearDown() {
        // Stop the PostgreSQL container after running tests
        postgresqlContainer.stop();
    }

    @Test
    public void test_search_withPage() {
        var searchResult = permissionService.searchData("admin", 100, 0,
                List.of(new Sort.Order(Sort.Direction.DESC, "roleId")));
        assertEquals(0, searchResult.getContent().size());
    }

    @Test
    public void test_search_withSort() {
        var searchResult = permissionService.searchData("admin", -1, -1,
                List.of(new Sort.Order(Sort.Direction.DESC, "roleId")));
        assertEquals(0, searchResult.getContent().size());
    }
}
