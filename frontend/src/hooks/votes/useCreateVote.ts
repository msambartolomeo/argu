import { HttpStatusCode } from "axios";

import DebateVote from "../../types/enums/DebateVote";
import { usePost } from "../requests/usePost";

export interface CreateVoteInput {
    voteUrl: string;
    type: DebateVote;
}

export const useCreateVote = () => {
    const { loading, callPost } = usePost();

    async function createVote({
        voteUrl,
        type,
    }: CreateVoteInput): Promise<HttpStatusCode> {
        const response = await callPost(
            voteUrl,
            {
                vote: type,
            },
            {},
            true
        );
        return response.status;
    }

    return { loading, createVote };
};
