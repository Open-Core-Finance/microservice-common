package tech.corefinance.product.enums;

public enum DepositBalanceInterestCalculation {
    /**
     * Minimum Daily Balance: <br/>
     * System will base the interest calculation on the minimum balance the client had in their account during the day.
     */
    DAILY_MIN,
    /**
     * Average Daily Balance: <br/>
     * System will calculate the average balance that the client had in their account during the day, and base the interest calculation on that amount.
     */
    DAILY_AVG,
    /**
     * End of Day Balance: <br/>
     * System will base the interest calculation on the balance the client had in their account at the end of the day.
     */
    END_OF_dAY;
}
