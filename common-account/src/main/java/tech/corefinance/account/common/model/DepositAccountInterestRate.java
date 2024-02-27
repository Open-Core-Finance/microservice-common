package tech.corefinance.account.common.model;

import lombok.Data;
import tech.corefinance.product.common.enums.InterestCalculationMethod;
import tech.corefinance.product.common.enums.InterestDayInYear;
import tech.corefinance.product.common.model.*;

import java.util.List;

@Data
public class DepositAccountInterestRate {
    private DepositInterestRateTerms interestRateTerms;
    private InterestCalculationMethod interestCalculationMethod;
    private int percentPerDay;
    private DepositBalanceInterestCalculation balanceInterestCalculation;
    private InterestCalculationDateOptionType calculationDateType;
    private int calculationDateFixedMonth;
    private int calculationDateFixedDay;
    private InterestCalculationDateOptionType calculationDateOption;
    private InterestDayInYear interestDayInYear;

    private Boolean applyWithholdingTaxes;
    // Fixed interest rate
    private Boolean allowNegativeInterestRate;
    /**
     * Interest Rate Constraints (%) for fixed interest rate. <br/>
     * Interest Spread Constraints (%) for index rate source.
     */
    private List<CurrencyValue> interestRateValues;
    private String interestRateIndexSource;
    // Tiered interest rate
    private List<TieredInterestItem> interestItems;
}
