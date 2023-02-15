import { HttpStatusCode } from "axios";

import { useGet } from "../requests/useGet";

export const useGetUserImage = () => {
    const { loading, callGet } = useGet();

    async function getUserImage(imageUrl: string): Promise<HttpStatusCode> {
        const response = await callGet(imageUrl);

        return response.status;
    }

    return { loading, getUserImage };
};
