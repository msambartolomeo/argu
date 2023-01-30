import { HttpStatusCode } from "axios";
import { usePost } from "../requests/usePost";
import { argumentsEndpoint } from "./constants";
import BadRequestError from "../../types/errors/BadRequestError";
import { PostOutput } from "../../types/PostOutput";

export interface CreateArgumentInput {
    debateId: number;
    content: string;
}

export interface CreateArgumentOutput extends PostOutput {
    errors?: BadRequestError[];
}

export const useCreateArgument = () => {
    const { loading, callPost } = usePost();

    async function createArgument({
        debateId,
        content,
    }: CreateArgumentInput): Promise<CreateArgumentOutput> {
        const response = await callPost(
            argumentsEndpoint(debateId),
            { content },
            {
                "Content-Type": "application/json",
            },
            true
        );

        switch (response.status) {
            case HttpStatusCode.Created:
                return {
                    status: response.status,
                    location: response.headers.location,
                };
            // TODO: Should be handle more errors? Only ones possible are 403 and maybe 409 (if the debate is closed), which should never happen.
            default:
                return {
                    status: response.status,
                };
        }
    }

    return { loading, createArgument };
};
