import { HttpStatusCode } from "axios";

import { PostOutput } from "../../types/PostOutput";
import { usePost } from "../requests/usePost";

export interface CreateChatInput {
    chatUrl: string;
    message: string;
}

export const useCreateChat = () => {
    const { loading, callPost } = usePost();

    async function createChat({
        chatUrl,
        message,
    }: CreateChatInput): Promise<PostOutput> {
        const response = await callPost(
            chatUrl,
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
