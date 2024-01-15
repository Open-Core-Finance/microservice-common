import { DayOfWeek } from "../DayOfWeek";
import {User} from "../User";

export class Branch {
  id: string = "";
  name: string = "";
  index: number = 0;
  inheritNonWorkingDays: boolean = true;
  nonWorkingDays: DayOfWeek[] = [DayOfWeek.SATURDAY, DayOfWeek.SUNDAY];
  streetAddressLine1: string = "";
  city: string = "";
  state: string = "";
  zipPostalCode: string = "";
  country: string = "";
  phoneNumber: string = "";
  email: string = "";
  parentBranchId: string = "";

  lastModifiedDate: Date = new Date();
  createdDate: Date = new Date();
  createdBy: User = new User();
  lastModifiedBy: User = new User();
}
