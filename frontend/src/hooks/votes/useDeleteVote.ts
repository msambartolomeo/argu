import { HttpStatusCode } from "axios";

import { useDelete } from "../requests/useDelete";

export interface DeleteVoteInput {
    voteUrl: string;
}

export const useDeleteVote = () => {
    const { loading, callDelete } = useDelete();

    async function callDeleteVote({
        voteUrl,
    }: DeleteVoteInput): Promise<HttpStatusCode> {
        const response = await callDelete(voteUrl, {}, true);
        return response.status;
    }

    return { loading, callDeleteVote };
};
