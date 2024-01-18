export class ValueConstraint {
    currencyId: string = "";
    currencyName: string = "";
    /**
     * Min amount of money.
     */
    minVal: number | null= 0.0;
    /**
     * Max amount of money.
     */
    maxVal: number | null= 0.0;
    /**
     * Default amount of money.
     */
    defaultVal: number | null= 0.0;
}

export class CurrencyLimitValue {
    currencyId: string = "";
    currencyName: string = "";
    limit = 0.0;
}