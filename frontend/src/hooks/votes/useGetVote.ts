import { HttpStatusCode } from "axios";

import VoteDto from "../../types/dto/VoteDto";
import { useGet } from "../requests/useGet";

export interface GetVoteInput {
    voteUrl: string;
}

export interface GetVoteOutput {
    status: HttpStatusCode;
    vote?: VoteDto;
}

export const useGetVote = () => {
    const { loading, callGet } = useGet();

    async function getVote({ voteUrl }: GetVoteInput): Promise<GetVoteOutput> {
        const response = await callGet(voteUrl, {}, true);

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

    return { loading, getVote };
};
