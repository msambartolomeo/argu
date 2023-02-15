import { API_URL } from "../constants";

export enum RequestMethod {
    GET = "GET",
    POST = "POST",
    PUT = "PUT",
    DELETE = "DELETE",
    PATCH = "PATCH",
}

export const AUTHORIZATION_HEADER = "authorization";
export const REFRESH_HEADER = "x-refresh";

// NOTE: This url is used because it is an endpoint where the GET verb requires auth
export const LOGIN_URL = API_URL + "users/";
