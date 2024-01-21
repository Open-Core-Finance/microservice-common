package tech.corefinance.product.model;

import lombok.Data;
import tech.corefinance.product.enums.*;

import java.util.List;

@Data
public class LoanInterestRate implements InterestRate {
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
    private List<ValueConstraint> interestRateConstraints;
    private String interestRateIndexSource;
    private RepaymentsInterestCalculation repaymentsInterestCalculation;
}
