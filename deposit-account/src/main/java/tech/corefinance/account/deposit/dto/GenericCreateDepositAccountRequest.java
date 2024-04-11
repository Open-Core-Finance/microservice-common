package tech.corefinance.account.deposit.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.corefinance.account.common.dto.CreateAccountRequest;
import tech.corefinance.account.common.enums.CustomerType;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.product.common.enums.FrequencyOptionYearly;
import tech.corefinance.product.common.model.CurrencyValue;
import tech.corefinance.product.common.model.TieredInterestItem;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class GenericCreateDepositAccountRequest extends CreateAccountRequest implements CreateUpdateDto<String> {
    private Integer termLength;
    private FrequencyOptionYearly termUnit;
    private boolean enableTermDeposit;
    private long customerId;
    private CustomerType customerType;

    private List<CurrencyValue> interestRateValues;
    // Tiered interest rate
    private List<TieredInterestItem> interestItems;
}
