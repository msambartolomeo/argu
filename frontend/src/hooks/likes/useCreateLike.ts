import { HttpStatusCode } from "axios";

import { usePost } from "../requests/usePost";

export interface CreateLikeInput {
    likeUrl: string;
    // TODO: Maybe get the userUrl from the context
    userUrl: string;
}

export const useCreateLike = () => {
    const { loading, callPost } = usePost();

    async function createLike({
        likeUrl,
        userUrl,
    }: CreateLikeInput): Promise<HttpStatusCode> {
        const response = await callPost(likeUrl, {}, {}, true, {
            user: userUrl,
        });
        return response.status;
    }

    return { loading, createLike };
};
