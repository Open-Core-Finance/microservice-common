package tech.corefinance.common.jpa.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import tech.corefinance.common.jpa.test.support.TestCommonApplication;
import tech.corefinance.common.service.PermissionService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@Testcontainers
@SpringBootTest(classes = TestCommonApplication.class)
@ActiveProfiles({"common", "default", "unittest"})
@ConditionalOnProperty(prefix = "tech.corefinance.security", name = "authorize-check", havingValue = "true", matchIfMissing = true)
@Slf4j
public class JpaPermissionServiceTest {

    //    @SuppressWarnings("resource")
    //    private static final PostgreSQLContainer<?> postgresqlContainer =
    //            new PostgreSQLContainer<>(DockerImageName.parse("postgres:17")).withDatabaseName("coffee_shop_order_test")
    //                    .withUsername("testuser").withPassword("testpassword");

    @Autowired
    private PermissionService permissionService;

    //    @BeforeAll
    //    public static void setUp() {
    //        try {
    //            // Start the PostgresSQL container before running tests
    //            postgresqlContainer.start();
    //        } catch (ContainerLaunchException e) {
    //            log.error(e.getMessage(), e);
    //        }
    //        var url = postgresqlContainer.getJdbcUrl();
    //        System.setProperty("DB_URL", url);
    //        var userName = postgresqlContainer.getUsername();
    //        System.setProperty("DB_USERNAME", userName);
    //        var password = postgresqlContainer.getPassword();
    //        System.setProperty("DB_PASSWORD", password);
    //    }

    //    @AfterAll
    //    public static void tearDown() {
    //        // Stop the PostgreSQL container after running tests
    //        postgresqlContainer.stop();
    //    }

    @Test
    public void test_search_withPage() {
        var searchResult = permissionService.searchData("admin", 100, 0, List.of(new Sort.Order(Sort.Direction.DESC, "roleId")));
        assertEquals(0, searchResult.getContent().size());
    }

    @Test
    public void test_search_withSort() {
        var searchResult = permissionService.searchData("admin", -1, -1, List.of(new Sort.Order(Sort.Direction.DESC, "roleId")));
        assertEquals(0, searchResult.getContent().size());
    }
}
