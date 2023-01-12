import { RequestMethod } from "./constants";
import { useRequestApi } from "./useRequestApi";

export const usePatch = () => {
    const { loading, requestApi } = useRequestApi();

    async function callPatch(
        url: string,
        headers?: Record<string, string>,
        requiresAuth?: boolean
    ) {
        return await requestApi({
            url: url,
            method: RequestMethod.PATCH,
            headers: headers,
            requiresAuth: requiresAuth,
        });
    }

    return { loading, callPatch };
};
