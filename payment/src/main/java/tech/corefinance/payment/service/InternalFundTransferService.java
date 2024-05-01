package tech.corefinance.payment.service;

import tech.corefinance.common.service.CommonService;
import tech.corefinance.payment.entity.InternalFundTransfer;
import tech.corefinance.payment.repository.InternalFundTransferRepository;

public interface InternalFundTransferService extends CommonService<String, InternalFundTransfer, InternalFundTransferRepository> {
}
