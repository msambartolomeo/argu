import { HttpStatusCode } from "axios";

import { useDelete } from "../requests/useDelete";

export interface DeleteLikeInput {
    likeUrl: string;
    // TODO: Maybe get the userUrl from the context
    userUrl: string;
}

export const useDeleteLike = () => {
    const { loading, callDelete } = useDelete();

    async function callDeleteLike({
        likeUrl,
        userUrl,
    }: DeleteLikeInput): Promise<HttpStatusCode> {
        const response = await callDelete(likeUrl, {}, true, {
            user: userUrl,
        });
        return response.status;
    }

    return { loading, callDeleteLike };
};
