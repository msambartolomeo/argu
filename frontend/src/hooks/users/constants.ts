import { API_URL } from "../constants";

export const USERS_ENDPOINT = API_URL + "users/";

export const imageEndpoint = (userId: string) => {
    return USERS_ENDPOINT + `/${userId}/image`;
};
