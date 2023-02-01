import { HttpStatusCode } from "axios";

import { usePost } from "../requests/usePost";

export interface CreateVoteInput {
    voteUrl: string;
    // TODO: Maybe get the userUrl from the context
    userUrl: string;
}

export const useCreateVote = () => {
    const { loading, callPost } = usePost();

    async function createVote({
        voteUrl,
        userUrl,
    }: CreateVoteInput): Promise<HttpStatusCode> {
        const response = await callPost(voteUrl, {}, {}, true, {
            user: userUrl,
        });
        return response.status;
    }

    return { loading, createVote };
};
