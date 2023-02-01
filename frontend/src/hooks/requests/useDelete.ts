import { RequestMethod } from "./constants";
import { useRequestApi } from "./useRequestApi";

export const useDelete = () => {
    const { loading, requestApi } = useRequestApi();

    async function callDelete(
        url: string,
        headers?: Record<string, string>,
        requiresAuth?: boolean,
        queryParams?: Record<string, string>
    ) {
        return await requestApi({
            url: url,
            method: RequestMethod.DELETE,
            headers: headers,
            requiresAuth: requiresAuth,
            queryParams: queryParams,
        });
    }

    return { loading, callDelete };
};
