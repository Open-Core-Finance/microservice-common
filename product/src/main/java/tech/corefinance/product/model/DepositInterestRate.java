package tech.corefinance.product.model;

import lombok.Data;
import tech.corefinance.product.enums.DepositBalanceInterestCalculation;
import tech.corefinance.product.enums.InterestCalculationMethod;
import tech.corefinance.product.enums.InterestDayInYear;

import java.util.List;

@Data
public class DepositInterestRate implements InterestRate {
    private InterestCalculationMethod interestCalculationMethod;
    private DepositBalanceInterestCalculation balanceInterestCalculation;
    private DepositInterestCalculationDateOption calculationDateOption;
    private InterestDayInYear interestDayInYear;

    private Boolean applyWithholdingTaxes;
    // Fixed interest rate
    private Boolean allowNegativeInterestRate;
    /**
     * Interest Rate Constraints (%) for fixed interest rate. <br/>
     * Interest Spread Constraints (%) for index rate source.
     */
    private ValueConstraint interestRateConstraint;
    private String interestRateIndexSource;
    // Tiered interest rate
    private List<TieredInterestItem> interestItems;
}
