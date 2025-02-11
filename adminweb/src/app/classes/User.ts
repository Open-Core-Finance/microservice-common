import { GeneralModel, ListableItem } from "./CommonClasses";
import { Gender } from "./Gender";
import { Role } from "./Role";

export class UserDto {
    userId: string = "";
    username: string = "";
    email: string = "";
    firstName: string = "";
    middleName: string | null = null;
    lastName: string | null = null;
    displayName: string = "";

    toString() {
        return this.displayName;
    }
}

export class User implements GeneralModel<string>, ListableItem {
    index = 0;
    id: string = "";
    username: string = "";
    email: string = "";
    firstName: string = "";
    middleName: string | null = null;
    lastName: string | null = null;
    displayName: string = "";
    gender: Gender = Gender.UNKNOWN;
    birthday = new Date();
    activated = true;
    address: string = "";
    phoneNumber: string = "";

    roles: Role[] = [];
    additionalAttributes: Map<string, any> = new Map();
}
