package tech.corefinance.common.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.corefinance.common.entity_author.InternalServiceConfig;
import tech.corefinance.common.dto.BulkUpdateResponseItem;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.common.ex.CustomConstraintViolation;
import tech.corefinance.common.ex.CustomConstraintViolationException;
import tech.corefinance.common.test.support.repository.TestInternalServiceConfigRepository;
import tech.corefinance.common.test.support.service.InternalServiceConfigTestServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CommonServiceBulkUpdateTest {
    private TestInternalServiceConfigRepository repository;
    private TestService service;

    @BeforeEach
    void setUp() {
        repository = mock(TestInternalServiceConfigRepository.class);
        service = spy(new TestService(repository));
    }

    @Test
    void createsAndUpdatesMixedBatchAndKeepsHookDataInInputOrder() {
        var createdDto = dto(null, "new service");
        var updatedDto = dto("existing", "updated service");
        var missingDto = dto("missing", "missing service");
        var existing = entity("existing", "old service");
        when(repository.findById("existing")).thenReturn(Optional.of(existing));
        when(repository.findById("missing")).thenReturn(Optional.empty());
        doAnswer(invocation -> {
            assertEquals("old service", existing.getServiceName());
            return existing.getServiceName();
        }).when(service).beforeEditEntityAttributes(existing, updatedDto);
        var persisted = List.of(entity("generated", "new service"), existing,
                entity("missing", "missing service"));
        when(repository.saveAll(anyList())).thenAnswer(invocation -> {
            List<InternalServiceConfig> batch = invocation.getArgument(0);
            assertEquals(3, batch.size());
            assertNull(batch.get(0).getId());
            assertEquals("new service", batch.get(0).getServiceName());
            assertSame(existing, batch.get(1));
            assertEquals("updated service", existing.getServiceName());
            assertEquals("missing", batch.get(2).getId());
            assertEquals("missing service", batch.get(2).getServiceName());
            return persisted;
        });

        var result = service.createOrUpdateEntities(List.of(createdDto, updatedDto, missingDto));

        assertEquals(3, result.getItems().size());
        assertNull(result.getItems().get(0).getId());
        assertEquals("existing", result.getItems().get(1).getId());
        assertEquals("missing", result.getItems().get(2).getId());
        assertSame(existing, result.getItems().get(1).getEntity());
        assertEquals("new service", ((InternalServiceConfig) result.getItems().get(0).getEntity()).getServiceName());
        result.getItems().forEach(item -> assertNull(item.getConstraintViolations()));
        verify(service, times(2)).createEntityObject();
        verify(service, times(1)).beforeEditEntityAttributes(any(), any());
        verify(repository, never()).findById(isNull());
        verify(repository, never()).save(any());
        var order = inOrder(service, repository);
        order.verify(service).copyAdditionalPropertiesFromDtoToEntity(eq(createdDto), any());
        order.verify(service).customEntityValidation(eq(createdDto), any());
        order.verify(service).beforeEditEntityAttributes(existing, updatedDto);
        order.verify(service).copyAdditionalPropertiesFromDtoToEntity(updatedDto, existing);
        order.verify(service).customEntityValidation(updatedDto, existing);
        order.verify(service).copyAdditionalPropertiesFromDtoToEntity(eq(missingDto), any());
        order.verify(service).customEntityValidation(eq(missingDto), any());
        order.verify(repository).saveAll(anyList());
        order.verify(service).afterEntitiesSaved(same(persisted), eq(Arrays.asList(null, "old service", null)));
        verify(service, never()).afterEntitySaved(any(), any());
    }

    @Test
    void savesEntityReturnedByCustomHooks() {
        var source = dto(null, "source");
        var copied = entity(null, "custom copy");
        var validated = entity(null, "validated");
        doReturn(copied).when(service).copyAdditionalPropertiesFromDtoToEntity(eq(source), any());
        doReturn(validated).when(service).customEntityValidation(source, copied);
        var persisted = List.of(entity("generated", "validated"));
        when(repository.saveAll(List.of(validated))).thenReturn(persisted);

        var response = service.createOrUpdateEntities(List.of(source));
        assertEquals(1, response.getItems().size());
        assertSame(validated, response.getItems().get(0).getEntity());

        verify(repository).saveAll(List.of(validated));
        verify(service).afterEntitiesSaved(same(persisted), eq(Arrays.asList((Object) null)));
    }

    @Test
    void emptyInputSavesAndNotifiesWithEmptyLists() {
        List<InternalServiceConfig> persisted = List.of();
        when(repository.saveAll(List.of())).thenReturn(persisted);

        assertTrue(service.createOrUpdateEntities(List.<ConfigDto>of()).getItems().isEmpty());

        verify(repository).saveAll(List.of());
        verify(repository, never()).findById(any());
        verify(service, never()).createEntityObject();
        verify(service, never()).copyAdditionalPropertiesFromDtoToEntity(any(), any());
        verify(service, never()).customEntityValidation(any(), any());
        verify(service).afterEntitiesSaved(persisted, List.of());
    }

    @Test
    void customConstraintViolationContinuesValidationButPreventsBatchPersistence() {
        var first = dto("first", "first updated");
        var invalid = dto("invalid", "invalid service");
        var last = dto(null, "last service");
        var existing = entity("first", "original");
        when(repository.findById("first")).thenReturn(Optional.of(existing));
        when(repository.findById("invalid")).thenReturn(Optional.empty());
        doReturn("original").when(service).beforeEditEntityAttributes(existing, first);
        var violations = Set.of(new CustomConstraintViolation("service.invalid"));
        doThrow(new CustomConstraintViolationException(violations))
                .when(service).customEntityValidation(eq(invalid), any());

        var response = service.createOrUpdateEntities(List.of(first, invalid, last));

        assertEquals(3, response.getItems().size());
        assertEquals("first", response.getItems().get(0).getId());
        assertNull(response.getItems().get(0).getConstraintViolations());
        assertEquals("invalid", response.getItems().get(1).getId());
        assertEquals(violations, response.getItems().get(1).getConstraintViolations());
        assertEquals("last service", ((InternalServiceConfig) response.getItems().get(2).getEntity()).getServiceName());
        assertNull(response.getItems().get(2).getConstraintViolations());
        assertEquals(BulkUpdateResponseItem.STATUS_ERROR, response.getItems().get(1).getStatus());
        verify(repository, never()).saveAll(any());
        verify(service).internalSaveEntitiesToDatabase(argThat(batch -> batch.size() == 2
                && batch.get(0) == existing && "last service".equals(batch.get(1).getServiceName())), eq(1));
        verify(service).afterEntitiesSaved(eq(List.of()), eq(Arrays.asList("original", null)));
    }

    @Test
    void allCustomConstraintViolationsPreventPersistenceAndNotifyWithEmptyBatch() {
        var invalid = dto(null, "invalid");
        var violations = Set.of(new CustomConstraintViolation("service.invalid"));
        doThrow(new CustomConstraintViolationException(violations))
                .when(service).copyAdditionalPropertiesFromDtoToEntity(eq(invalid), any());

        var response = service.createOrUpdateEntities(List.of(invalid));

        assertEquals(1, response.getItems().size());
        assertEquals(violations, response.getItems().get(0).getConstraintViolations());
        verify(service, never()).customEntityValidation(any(), any());
        assertEquals(BulkUpdateResponseItem.STATUS_ERROR, response.getItems().get(0).getStatus());
        verify(repository, never()).saveAll(any());
        verify(service).internalSaveEntitiesToDatabase(List.of(), 1);
        verify(service).afterEntitiesSaved(List.of(), List.of());
    }

    @Test
    void jakartaConstraintViolationConvertsMessagesAndPreventsBatchPersistence() {
        var first = dto("first", "first updated");
        var invalid = dto("invalid", "invalid service");
        var last = dto(null, "last service");
        var existing = entity("first", "original");
        when(repository.findById("first")).thenReturn(Optional.of(existing));
        when(repository.findById("invalid")).thenReturn(Optional.empty());
        doReturn("original").when(service).beforeEditEntityAttributes(existing, first);
        var failure = new ConstraintViolationException(Set.of(
                violation("service.name.required"), violation("service.apiKey.required")));
        doThrow(failure).when(service).customEntityValidation(eq(invalid), any());

        var response = service.createOrUpdateEntities(List.of(first, invalid, last));

        assertEquals(3, response.getItems().size());
        assertEquals("first", response.getItems().get(0).getId());
        assertNull(response.getItems().get(0).getConstraintViolations());
        assertEquals("invalid", response.getItems().get(1).getId());
        var converted = response.getItems().get(1).getConstraintViolations();
        assertEquals(Set.of(new CustomConstraintViolation("service.name.required"),
                new CustomConstraintViolation("service.apiKey.required")), converted);
        converted.forEach(v -> assertNull(v.getDescription()));
        assertEquals("last service", ((InternalServiceConfig) response.getItems().get(2).getEntity()).getServiceName());
        assertNull(response.getItems().get(2).getConstraintViolations());
        verify(repository, never()).save(any());
        assertEquals(BulkUpdateResponseItem.STATUS_ERROR, response.getItems().get(1).getStatus());
        verify(repository, never()).saveAll(any());
        verify(service).internalSaveEntitiesToDatabase(argThat(batch -> batch.size() == 2
                && batch.get(0) == existing && "last service".equals(batch.get(1).getServiceName())), eq(1));
        verify(service).afterEntitiesSaved(eq(List.of()), eq(Arrays.asList("original", null)));
    }

    @Test
    void jakartaConstraintViolationWithEmptySetStillPreventsPersistence() {
        var invalid = dto(null, "invalid");
        doThrow(new ConstraintViolationException(Set.of()))
                .when(service).customEntityValidation(eq(invalid), any());

        var response = service.createOrUpdateEntities(List.of(invalid));

        assertEquals(1, response.getItems().size());
        assertNotNull(response.getItems().get(0).getConstraintViolations());
        assertTrue(response.getItems().get(0).getConstraintViolations().isEmpty());
        assertEquals(BulkUpdateResponseItem.STATUS_ERROR, response.getItems().get(0).getStatus());
        verify(repository, never()).saveAll(any());
        verify(service).internalSaveEntitiesToDatabase(List.of(), 1);
        verify(service).afterEntitiesSaved(List.of(), List.of());
    }

    @SuppressWarnings("unchecked")
    private static ConstraintViolation<InternalServiceConfig> violation(String message) {
        ConstraintViolation<InternalServiceConfig> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn(message);
        return violation;
    }

    @Test
    void validationFailurePreventsEntireBatchFromBeingSaved() {
        var first = dto(null, "valid");
        var invalid = dto(null, "invalid");
        var failure = new IllegalArgumentException("Invalid service");
        doThrow(failure).when(service).customEntityValidation(eq(invalid), any());

        assertSame(failure, assertThrows(IllegalArgumentException.class,
                () -> service.createOrUpdateEntities(List.of(first, invalid))));

        verify(repository, never()).saveAll(any());
        verify(repository, never()).save(any());
        verify(service, never()).afterEntitiesSaved(anyList(), anyList());
    }

    @Test
    void persistenceFailureIsPropagatedWithoutCallingCompletionHook() {
        var failure = new IllegalStateException("Save failed");
        when(repository.saveAll(anyList())).thenThrow(failure);

        assertSame(failure, assertThrows(IllegalStateException.class,
                () -> service.createOrUpdateEntities(List.of(dto(null, "service")))));

        verify(service, never()).afterEntitiesSaved(anyList(), anyList());
    }

    private static ConfigDto dto(String id, String serviceName) {
        var dto = new ConfigDto();
        dto.setId(id);
        dto.setServiceName(serviceName);
        return dto;
    }

    private static InternalServiceConfig entity(String id, String serviceName) {
        var entity = new InternalServiceConfig();
        entity.setId(id);
        entity.setServiceName(serviceName);
        return entity;
    }

    public static class ConfigDto extends InternalServiceConfig implements CreateUpdateDto<String> {
    }

    private static class TestService extends InternalServiceConfigTestServiceImpl {
        private final TestInternalServiceConfigRepository repository;

        private TestService(TestInternalServiceConfigRepository repository) {
            this.repository = repository;
        }

        @Override
        public TestInternalServiceConfigRepository getRepository() {
            return repository;
        }

        @Override
        public Class<InternalServiceConfig> findEntityClass() {
            return InternalServiceConfig.class;
        }
    }
}
