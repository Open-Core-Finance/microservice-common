export class ProductCategory {
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