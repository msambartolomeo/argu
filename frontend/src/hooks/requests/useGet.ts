import { RequestMethod } from "./constants";
import { useRequestApi } from "./useRequestApi";

export const useGet = () => {
    const { loading, requestApi } = useRequestApi();

    async function callGet(
        url: string,
        headers?: Record<string, string>,
        requiresAuth?: boolean
    ) {
        return await requestApi({
            url: url,
            method: RequestMethod.GET,
            headers: headers,
            requiresAuth: requiresAuth,
        });
    }

    return { loading, callGet };
};
