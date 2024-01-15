package tech.corefinance.product.service;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.criteria.internal.ValueHandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tech.corefinance.common.context.TenantContext;
import tech.corefinance.common.ex.ServiceProcessingException;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.product.entity.Branch;
import tech.corefinance.product.repository.BranchRepository;
import tech.corefinance.product.repository.OrganizationRepository;

@Service
@Transactional
@Slf4j
public class BranchServiceImpl implements BranchService {

    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private OrganizationRepository organizationRepository;

    @Override
    public BranchRepository getRepository() {
        return branchRepository;
    }

    @Override
    public <D extends CreateUpdateDto<String>> void copyAdditionalPropertiesFromDtoToEntity(D source, Branch dest) {
        BranchService.super.copyAdditionalPropertiesFromDtoToEntity(source, dest);
        var tenantContext = TenantContext.getInstance();
        var currentTenant = tenantContext.getTenantId();
        log.debug("Current tenant [{}]", currentTenant);

        if (dest.isInheritNonWorkingDays()) {
            var errorMarker = new ErrorHolder(null);
            // Start a virtual thread to run a task
            Thread thread = Thread.ofVirtual().start(() -> {
                try {
                    tenantContext.clearTenantId();
                    var org = organizationRepository.findById(currentTenant).orElseThrow(() ->
                            new ServiceProcessingException("organization_not_found"));
                    dest.setNonWorkingDays(org.getNonWorkingDays());
                } catch (Throwable e) {
                    errorMarker.value = e;
                } finally {
                    tenantContext.setTenantId(currentTenant);
                }
            });
            try {
                thread.join();
            } catch (InterruptedException e) {
                errorMarker.value = e;
            }
            if (errorMarker.value != null) {
                if (errorMarker.value instanceof RuntimeException runtimeEx) {
                    throw runtimeEx;
                } else {
                    throw new ServiceProcessingException("organization_load_error", errorMarker.value);
                }
            }
        }
    }
    private static class ErrorHolder {
        Throwable value;
        ErrorHolder(Throwable value) {
            this.value = value;
        }
    }
}
