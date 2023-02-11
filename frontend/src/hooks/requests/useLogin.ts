import { HttpStatusCode } from "axios";

import { LOGIN_URL, RequestMethod } from "./constants";
import { useRequestApi } from "./useRequestApi";

export function useLogin() {
    const { loading, requestApi } = useRequestApi();

    async function callLogin(username: string, password: string) {
        const response = await requestApi({
            url: LOGIN_URL + encodeURI(username),
            method: RequestMethod.GET,
            requiresAuth: true,
            credentials: {
                username,
                password,
            },
        });
        return response.status !== HttpStatusCode.Unauthorized;
    }

    return { loading, callLogin };
}
