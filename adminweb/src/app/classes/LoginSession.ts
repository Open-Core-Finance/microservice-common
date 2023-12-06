import {Gender} from "./Gender";

export class LoginSession {
    loginId: string = "";
    userId: string = "";
    token: string = "";
    refreshToken: string = "";
    username: string = "";
    userEmail: string = "";
    _userRoles: UserRole[] = [];
    fistName: string = "";
    lastName: string | null = null;
    displayName: string = "";
    address: string | null = null;
    gender: Gender = Gender.MALE;
    birthday: string | null = null;
    phoneNumber: string | null = null;

    get userRoles() {
        return this.userRoles;
    }

    set userRoles(userRoles: UserRole[]) {
        this._userRoles = userRoles || [];
    }

}

export class UserRole {
    resourceType: string | null = null;
    resourceId: string | null = null;
    roleId: string = "";
}

export enum AppPlatform {
    ANDROID = "ANDROID", IOS = "IOS", WEB = "WEB", UNKNOWN = "UNKNOWN"
}

export class AppVersion {

    /**
     * <div>Format: major.minor.maintenance.build</div> <div>For example:
     * 2.0.1.123</div>
     */
    major: number = 0;
    minor: number = 0;
    maintenance: number = 1;
    build: string = "";
}