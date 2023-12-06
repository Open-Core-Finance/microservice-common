package tech.corefinance.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.annotation.PermissionAction;
import tech.corefinance.common.controller.CrudServiceAndController;
import tech.corefinance.common.dto.PageDto;
import tech.corefinance.common.model.ResourceAction;
import tech.corefinance.product.entity.Organization;
import tech.corefinance.product.repository.OrganizationRepository;

import java.util.List;

@RestController
@RequestMapping("/organizations")
@ControllerManagedResource("organization")
public class OrganizationController implements CrudServiceAndController<String, Organization, Organization, OrganizationRepository> {

    @Autowired
    private OrganizationRepository repository;

    @Override
    public OrganizationRepository getRepository() {
        return repository;
    }

    @Override
    @PermissionAction(action = ResourceAction.COMMON_ACTION_LIST)
    @PostMapping(value = "/")
    public PageDto<?> search(int pageSize, int pageIndex, List<Sort.Order> orders, String searchText) {
        return CrudServiceAndController.super.search(pageSize, pageIndex, orders, searchText);
    }
}
