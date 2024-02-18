package tech.corefinance.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.model.ResourceAction;
import tech.corefinance.common.repository.ResourceActionRepository;

@Repository
@RequestMapping(value = "/resource-actions")
@ControllerManagedResource("resourceaction")
@RestController
public class ResourceActionController
        implements CrudServiceAndController<String, ResourceAction, ResourceAction, ResourceActionRepository> {
    @Autowired
    private ResourceActionRepository resourceActionRepository;

    @Override
    public ResourceActionRepository getRepository() {
        return resourceActionRepository;
    }
}
