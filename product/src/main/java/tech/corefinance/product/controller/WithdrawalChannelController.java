package tech.corefinance.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.annotation.ControllerManagedResource;
import tech.corefinance.common.controller.CrudServiceAndController;
import tech.corefinance.product.entity.WithdrawalChannel;
import tech.corefinance.product.repository.WithdrawalChannelRepository;

@RestController
@RequestMapping("/withdrawal-channels")
@ControllerManagedResource("withdrawalchannel")
public class WithdrawalChannelController implements CrudServiceAndController<String, WithdrawalChannel, WithdrawalChannel, WithdrawalChannelRepository> {

    @Autowired
    private WithdrawalChannelRepository withdrawalChannelRepository;


    @Override
    public WithdrawalChannelRepository getRepository() {
        return withdrawalChannelRepository;
    }
}
