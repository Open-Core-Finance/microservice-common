package tech.corefinance.balance.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.corefinance.account.common.config.AccountKafkaConfig;
import tech.corefinance.account.common.dto.BalanceCleanupMessage;
import tech.corefinance.account.common.dto.BalanceInitialMessage;
import tech.corefinance.balance.entity.AccountBalance;
import tech.corefinance.balance.entity.AccountBalanceId;
import tech.corefinance.balance.repository.AccountBalanceRepository;

@Transactional
@Service
@Slf4j
public class AccountBalanceServiceImpl implements AccountBalanceService {

    @Autowired
    private AccountBalanceRepository accountBalanceRepository;
    @Autowired
    private AccountKafkaConfig accountKafkaConfig;

    @Override
    public AccountBalanceRepository getRepository() {
        return accountBalanceRepository;
    }

    @KafkaListener(id = "account-balance-init", topics = "#{accountKafkaConfig.getBalancesInitTopic()}",
            groupId = "balance-service")
    public void listenAccountInit(ConsumerRecord<String, BalanceInitialMessage> record) {
        var value = record.value();
        if (value != null && value.getSupportedCurrencies() != null) {
            for (var currency : value.getSupportedCurrencies()) {
                var accountBalanceId = new AccountBalanceId();
                accountBalanceId.setAccountId(value.getAccountId());
                accountBalanceId.setAccountType(value.getAccountType());
                accountBalanceId.setCurrency(currency);
                if (accountBalanceRepository.existsById(accountBalanceId)) {
                    log.debug("AccountBalance for ID [{}] existed!", accountBalanceId);
                } else {
                    AccountBalance accountBalance = new AccountBalance();
                    accountBalance.setId(accountBalanceId);
                    log.debug("Saving new accountBalance: {}", accountBalance);
                    accountBalanceRepository.save(accountBalance);
                }
            }
        } else {
            log.error("Received null data! Cannot continue!");
        }
    }

    @KafkaListener(id = "account-balance-cleanup", topics = "#{accountKafkaConfig.getBalancesCleanupTopic()}",
            groupId = "balance-service")
    public void listenAccountDeleted(ConsumerRecord<String, BalanceCleanupMessage> record) {
        var value = record.value();
        if (value != null) {
            log.debug("Cleaning balances of {} account [{}]", value.getAccountType(), value.getAccountId());
            accountBalanceRepository.deleteAllByIdAccountTypeAndIdAccountId(value.getAccountType(), value.getAccountId());
        }
    }
}

