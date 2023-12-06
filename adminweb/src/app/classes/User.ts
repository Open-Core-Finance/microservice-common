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

export class User {
    id: string = "";
    username: string = "";
    email: string = "";
    firstName: string = "";
    middleName: string | null = null;
    lastName: string | null = null;
    displayName: string = "";
}