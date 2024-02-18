import { GeneralModel, ListableItem } from "../CommonClasses";
import {ProductCategoryType} from "./ProductCategory";

export class ProductType implements GeneralModel<string>, ListableItem {
    id: string = "";
    name: string = "";
    index: number = 0;
    type: ProductCategoryType = ProductCategoryType.DEPOSIT;
}
