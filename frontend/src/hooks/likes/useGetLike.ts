import { HttpStatusCode } from "axios";

import { useGet } from "../requests/useGet";

export interface GetLikeInput {
    likeUrl: string;
    userUrl: string;
}

export const useGetLike = () => {
    const { loading, callGet } = useGet();

    async function getLike({
        likeUrl,
        userUrl,
    }: GetLikeInput): Promise<HttpStatusCode> {
        const response = await callGet(likeUrl, {}, true, {
            user: userUrl,
        });

        return response.status;
    }

    return { loading, getLike };
};
