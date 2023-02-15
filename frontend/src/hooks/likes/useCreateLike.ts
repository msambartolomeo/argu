import { HttpStatusCode } from "axios";

import { usePost } from "../requests/usePost";

export interface CreateLikeInput {
    likeUrl: string;
}

export const useCreateLike = () => {
    const { loading, callPost } = usePost();

    async function createLike({
        likeUrl,
    }: CreateLikeInput): Promise<HttpStatusCode> {
        const response = await callPost(likeUrl, {}, {}, true);
        return response.status;
    }

    return { loading, createLike };
};
