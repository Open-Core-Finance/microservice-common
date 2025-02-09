package tech.corefinance.userprofile.common.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import tech.corefinance.userprofile.common.app.TestUserProfileApplication;
import tech.corefinance.userprofile.repository.UserProfileRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = TestUserProfileApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles({"common", "default", "unittest"})
@ComponentScan(basePackages = {"tech.corefinance"})
@Slf4j
public class UserProfileServiceTest {

    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeAll
    public static void beforeAll() {
        System.setProperty("LIQUIBASE_TEST_CHANGELOG", "classpath:test-h2-db/changelog/sql-changelog.xml");
    }

    @Test
    public void testChangePassword() {
        var newPassword = UUID.randomUUID().toString();
        var userProfile = userProfileRepository.findFirstByEmailIgnoreCaseOrUsernameIgnoreCase("admin", "admin").orElseThrow();
        var currentPassword = "admin";
        userProfileService.changePassword(userProfile.getId(), currentPassword, newPassword, newPassword);
        userProfile = userProfileRepository.findById(userProfile.getId()).orElseThrow();
        assertTrue(passwordEncoder.matches(newPassword, userProfile.getPassword()), "Password hasn't changed to the new one!!");
        userProfileService.changePassword(userProfile.getId(), newPassword, currentPassword, currentPassword);
        userProfile = userProfileRepository.findById(userProfile.getId()).orElseThrow();
        assertTrue(passwordEncoder.matches(currentPassword, userProfile.getPassword()), "Password hasn't changed back!!");
    }
}
