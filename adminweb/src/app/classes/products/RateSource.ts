import { RateType } from "./Rate";

export class RateSource {
    id: string = "";
    name: string = "";
    index: number = 0;
    note: string = "";
    type: RateType = RateType.INTEREST;
}