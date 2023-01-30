import { RequestMethod } from "./constants";
import { useRequestApi } from "./useRequestApi";

export const useGet = () => {
    const { loading, requestApi } = useRequestApi();

    async function callGet(
        url: string,
        headers?: Record<string, string>,
        requiresAuth?: boolean,
        queryParams?: Record<string, string>
    ) {
        return await requestApi({
            url: url,
            method: RequestMethod.GET,
            headers: {
                ...headers,
                "Accept-Language": navigator.language,
            },
            requiresAuth: requiresAuth,
            queryParams: queryParams,
        });
    }

    return { loading, callGet };
};
