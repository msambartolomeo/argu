import { HttpStatusCode } from "axios";
import { PostOutput } from "../../types/PostOutput";
import { usePost } from "../requests/usePost";
import { chatsEndpoint } from "./constants";

export interface CreateChatInput {
    debateId: number;
    message: string;
}

export const useCreateChat = () => {
    const { loading, callPost } = usePost();

    async function createChat({
        debateId,
        message,
    }: CreateChatInput): Promise<PostOutput> {
        const response = await callPost(
            chatsEndpoint(debateId),
            { message },
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
            default:
                return {
                    status: response.status,
                };
        }
    }

    return { loading, createChat };
};
