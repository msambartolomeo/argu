import { RequestMethod } from "./constants";
import { useRequestApi } from "./useRequestApi";

export const usePost = () => {
    const { loading, requestApi } = useRequestApi();

    async function callPost(
        url: string,
        inData: object,
        headers: Record<string, string>,
        requiresAuth: boolean,
        queryParams: Record<string, string>
    ) {
        return await requestApi({
            url: url,
            method: RequestMethod.POST,
            body: inData,
            headers: {
                "Content-Type": "application/json",
                ...headers,
            },
            requiresAuth: requiresAuth,
            queryParams: queryParams,
        });
    }

    return { loading, callPost };
};
