import { HttpStatusCode } from "axios";

import { LOGIN_URL, RequestMethod } from "./constants";
import { useRequestApi } from "./useRequestApi";

export function useLogin() {
    const { requestApi } = useRequestApi();

    async function callLogin(username: string, password: string) {
        const response = await requestApi({
            url: LOGIN_URL,
            method: RequestMethod.GET,
            requiresAuth: true,
            credentials: {
                username,
                password,
            },
            queryParams: {
                user: encodeURI(username),
            },
        });
        return response.status !== HttpStatusCode.Unauthorized;
    }

    return { callLogin };
}
