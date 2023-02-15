import { RequestMethod } from "./constants";
import { useRequestApi } from "./useRequestApi";

export const usePut = () => {
    const { loading, requestApi } = useRequestApi();

    async function callPut(
        url: string,
        inData: object,
        headers: Record<string, string>,
        requiresAuth: boolean
    ) {
        return await requestApi({
            url: url,
            method: RequestMethod.PUT,
            body: inData,
            headers: headers,
            requiresAuth: requiresAuth,
        });
    }

    return { loading, callPut };
};
