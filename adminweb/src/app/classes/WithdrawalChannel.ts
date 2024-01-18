import { User } from "./User";

export class WithdrawalChannel {
    id: string = "";
    name: string = "";
  
    lastModifiedDate: Date = new Date();
    createdDate: Date = new Date();
    createdBy: User = new User();
    lastModifiedBy: User = new User();
}