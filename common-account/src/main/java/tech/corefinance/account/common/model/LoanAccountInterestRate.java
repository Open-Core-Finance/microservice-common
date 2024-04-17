package tech.corefinance.account.common.model;

import lombok.Data;
import tech.corefinance.product.common.enums.*;
import tech.corefinance.product.common.model.CurrencyValue;

import java.util.List;

@Data
public class LoanAccountInterestRate {
    private LoanInterestCalculationMethod interestCalculationMethod;
    private AccruedInterestPostingFrequency accruedInterestPostingFrequency;
    private InterestCalculationMethod interestCalculationPoint;
    private int percentPerDay;
    private LoanInterestType interestType;
    private InterestDayInYear interestDayInYear;
    /**
     * Interest Rate Constraints (%) for fixed interest rate. <br/>
     * Interest Spread Constraints (%) for index rate source.
     */
    private List<CurrencyValue> interestRateValues;
    private boolean sameConstraintForAllCurrency;
    private String interestRateIndexSource;
    private RepaymentsInterestCalculation repaymentsInterestCalculation;
}
