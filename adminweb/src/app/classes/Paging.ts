import { GeneralApiResponse } from './GeneralApiResponse';

export class Sort {
  public sorted: boolean = false;
  public unsorted: boolean = true;
  public orders: Order[] = [];
}

export class Paging extends GeneralApiResponse {
  public totalPages: number = 1;
  public totalElements: number = 0;
  last: boolean = false;
  public size: number = 0;
  public number: number = 0;
  first: boolean = true;
  public numberOfElements: number = 0;
  public pageNumber: number = 1;
  public sort = new Sort();
  public pageSize: number = 20;

  public override result: any[] = [];
}

export class Order {
  ascending: boolean = true;
  descending: boolean = false;
  ignoreCase: boolean = false;
  nullHandling: string | null = null;

  constructor(public property: string, public direction: OrderDirection) {

  }
}

export enum OrderDirection {
  ASC="ASC", DESC="DESC", NONE= ""
}
