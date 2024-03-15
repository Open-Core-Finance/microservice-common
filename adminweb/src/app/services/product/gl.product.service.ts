import { Injectable } from '@angular/core';
import { AbstractEntityListService } from '../abstract.entity.list.service';
import { GlProduct } from 'src/app/classes/products/GlProduct';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class GlProductService extends AbstractEntityListService<GlProduct> {

  override get entityServiceUrl(): string {
    return environment.apiUrl.glProduct + "/";
  }

}