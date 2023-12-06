package tech.corefinance.common.mongodb.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tech.corefinance.common.context.JwtContext;
import tech.corefinance.common.dto.JwtTokenDto;
import tech.corefinance.common.enums.AppPlatform;
import tech.corefinance.common.model.AppVersion;
import tech.corefinance.common.mongodb.support.app.TestCommonApplication;
import tech.corefinance.common.mongodb.support.model.CustomSequentialIdData;
import tech.corefinance.common.mongodb.support.repository.CustomSequentialIdDataRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = TestCommonApplication.class)
@ActiveProfiles({"common", "default", "unittest"})
public class MongoSequenceTest {

    @Autowired
    private CustomSequentialIdDataRepository customSequentialIdDataRepository;
    @Autowired
    private MongoSequenseRepository mongoSequenseRepository;

    private JwtTokenDto tokenDto;

    @BeforeEach
    void setUp() {
        tokenDto = new JwtTokenDto(UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                AppPlatform.UNKNOWN, new AppVersion("1.1.1-BETA"), UUID.randomUUID().toString(), "127.0.0.1");
        JwtContext.getInstance().setJwt(tokenDto);
    }

    @AfterEach
    void tearDown() {
        JwtContext.getInstance().removeJwt();
    }

    @Test
    public void test_nextSequence() {
        String name = "test data";
        var entity = new CustomSequentialIdData();
        entity.setName(name);
        entity = customSequentialIdDataRepository.save(entity);
        assertEquals(1, entity.getId());
        assertEquals(name, entity.getName());
        assertNotNull(entity.getLastModifiedDate());
        assertNotNull(entity.getCreatedDate());
        assertNotNull(entity.getCreatedBy());
        assertNotNull(entity.getLastModifiedBy());
        assertNotNull(mongoSequenseRepository.findById(CustomSequentialIdData.class.getSimpleName()).get());
    }
}
