import { HttpStatusCode } from "axios";

import { useDelete } from "../requests/useDelete";

export interface DeleteLikeInput {
    likeUrl: string;
}

export const useDeleteLike = () => {
    const { loading, callDelete } = useDelete();

    async function callDeleteLike({
        likeUrl,
    }: DeleteLikeInput): Promise<HttpStatusCode> {
        const response = await callDelete(likeUrl, {}, true);
        return response.status;
    }

    return { loading, callDeleteLike };
};
