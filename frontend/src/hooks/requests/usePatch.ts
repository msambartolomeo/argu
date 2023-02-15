import { RequestMethod } from "./constants";
import { useRequestApi } from "./useRequestApi";

export const usePatch = () => {
    const { loading, requestApi } = useRequestApi();

    async function callPatch(
        url: string,
        inData: Record<string, unknown>,
        headers?: Record<string, string>,
        requiresAuth?: boolean
    ) {
        return await requestApi({
            url: url,
            method: RequestMethod.PATCH,
            body: inData,
            headers: headers,
            requiresAuth: requiresAuth,
        });
    }

    return { loading, callPatch };
};
