import { HttpStatusCode } from "axios";

import VoteDto from "../../types/dto/VoteDto";
import { useGet } from "../requests/useGet";

export interface GetLikeInput {
    likeUrl: string;
    userUrl: string;
}

export interface GetLikeOutput {
    status: HttpStatusCode;
    vote?: VoteDto;
}

export const useGetLike = () => {
    const { loading, callGet } = useGet();

    async function getLike({
        likeUrl,
        userUrl,
    }: GetLikeInput): Promise<GetLikeOutput> {
        const response = await callGet(likeUrl, {}, true, {
            user: userUrl,
        });

        switch (response.status) {
            case HttpStatusCode.Ok:
                return {
                    status: HttpStatusCode.Ok,
                    vote: response.data,
                };
            default:
                return {
                    status: response.status,
                };
        }
    }

    return { loading, getLike };
};
