import UserRole from "./enums/UserRole";

export default interface User {
    username: string;
    role: UserRole;
    createdDate: string;
}
