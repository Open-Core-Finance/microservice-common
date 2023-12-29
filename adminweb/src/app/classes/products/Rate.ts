export class Rate {
    id: string = "";
    note: string = "";
    index: number = 0;
    rateValue: number = 0.0;
    validFrom: Date = new Date();
    rateSourceId: string = "";
    type: RateType = RateType.INTEREST;
    rateSourceName: string = "";
}

export enum RateType {
    INTEREST = "INTEREST",
    WITHHOLDING_TAX = "WITHHOLDING_TAX",
    VALUE_ADDED_TAX = "VALUE_ADDED_TAX"
}