package tech.corefinance.product.model;

import lombok.Data;
import tech.corefinance.product.enums.*;

@Data
public class LoanInterestRate implements InterestRate {
    private InterestCalculationMethod interestCalculationMethod;
    private AccruedInterestPostingFrequency accruedInterestPostingFrequency;
    private LoanInterestType interestType;
    private InterestDayInYear interestDayInYear;
    /**
     * Interest Rate Constraints (%) for fixed interest rate. <br/>
     * Interest Spread Constraints (%) for index rate source.
     */
    private ValueConstraint interestRateConstraint;
    private String interestRateIndexSource;
    private RepaymentsInterestCalculation repaymentsInterestCalculation;

    /**
     * Interest Rate Floor (%).
     */
    private Double rateFloor;
    /**
     * Interest Rate Ceiling (%).
     */
    private Double rateCeiling;

    /**
     * Interest Rate Review Frequency.
     */
    private Integer reviewFrequency;
    /**
     * Interest Rate Review Frequency type as Days|Weeks|Months.
     */
    private FrequencyOptionYearly reviewFrequencyType;
}
