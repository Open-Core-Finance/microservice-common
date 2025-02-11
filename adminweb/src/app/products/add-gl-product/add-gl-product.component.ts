import {AfterViewInit, Component, OnInit} from '@angular/core';
import {environment} from "../../../environments/environment";
import {ProductCategoryType} from "../../classes/products/ProductCategory";
import { GlProduct } from 'src/app/classes/products/GlProduct';
import { GeneralProductAddComponent } from '../GeneralProductAddComponent';

@Component({
    selector: 'app-add-gl-product',
    templateUrl: './add-gl-product.component.html',
    styleUrl: './add-gl-product.component.sass',
    standalone: false
})
export class AddGlProductComponent extends GeneralProductAddComponent<GlProduct> implements AfterViewInit, OnInit {

  protected override getProductCategoryType(): ProductCategoryType {
    return ProductCategoryType.GL;
  }
  protected override getServiceUrl(): string {
    return environment.apiUrl.glProduct;
  }
  
  protected override newEmptyEntity(): GlProduct {
    return new GlProduct();
  }
}
