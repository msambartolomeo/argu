import { HttpStatusCode } from "axios";
import { useDelete } from "../requests/useDelete";

export interface DeleteVoteInput {
    voteUrl: string;
    // TODO: Maybe get the userUrl from the context
    userUrl: string;
}

export const useDeleteVote = () => {
    const { loading, callDelete } = useDelete();

    async function callDeleteVote({
        voteUrl,
        userUrl,
    }: DeleteVoteInput): Promise<HttpStatusCode> {
        const response = await callDelete(voteUrl, {}, true, {
            user: userUrl,
        });
        return response.status;
    }

    return { loading, callDeleteVote };
};
