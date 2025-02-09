package tech.corefinance.common.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import tech.corefinance.common.repository.ResourceActionRepository;
import tech.corefinance.common.test.support.app.TestCommonApplication;
import tech.corefinance.common.test.support.config.EncryptedConfig;

import java.util.LinkedList;

@SpringBootTest(classes = TestCommonApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles({"common", "default", "unittest"})
@ComponentScan(basePackages = {"tech.corefinance"})
public class EncryptedConfigVerify {

    @Autowired
    private EncryptedConfig encryptedConfig;

    @MockitoBean
    private ResourceActionRepository resourceActionRepository;

    @BeforeEach
    public void setUp() {
        PowerMockito.when(resourceActionRepository.saveAll(Mockito.any())).thenReturn(new LinkedList<>());
    }

    @Test
    void test_CompareEncryptedWithPlain() {
        Assertions.assertEquals(encryptedConfig.getPlain(), encryptedConfig.getEncoded());
    }

}
