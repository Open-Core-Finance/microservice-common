import { GeneralModel, ListableItem } from "../CommonClasses";

export class ProductCategory implements GeneralModel<string>, ListableItem {
    id: string = "";
    name: string = "";
    index: number = 0;
    type: ProductCategoryType = ProductCategoryType.DEPOSIT;
}

export enum ProductCategoryType {
    DEPOSIT = "DEPOSIT",
    LOAN = "LOAN",
    GL = "GL",
    CRYPTO = "CRYPTO"
}