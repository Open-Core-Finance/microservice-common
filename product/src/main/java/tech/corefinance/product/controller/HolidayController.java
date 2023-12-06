package tech.corefinance.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudServiceAndController;
import tech.corefinance.product.entity.Holiday;
import tech.corefinance.product.repository.HolidayRepository;

@RestController
@RequestMapping("/holidays")
@ControllerManagedResource("holiday")
public class HolidayController implements CrudServiceAndController<String, Holiday, Holiday, HolidayRepository> {

    @Autowired
    private HolidayRepository repository;

    @Override
    public HolidayRepository getRepository() {
        return repository;
    }
}
