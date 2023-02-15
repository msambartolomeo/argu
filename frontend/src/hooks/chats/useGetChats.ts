import { HttpStatusCode } from "axios";

import { PaginatedList } from "../../types/PaginatedList";
import PaginatedOutput from "../../types/PaginatedOutput";
import ChatDto from "../../types/dto/ChatDto";
import { useGet } from "../requests/useGet";

export interface GetChatsInput {
    chatUrl: string;
}

export const useGetChats = () => {
    const { loading, callGet } = useGet();

    async function getChats({
        chatUrl,
    }: GetChatsInput): Promise<PaginatedOutput<ChatDto>> {
        const response = await callGet(chatUrl, {}, false);
        switch (response.status) {
            case HttpStatusCode.Ok:
                return {
                    status: response.status,
                    data: new PaginatedList<ChatDto>(
                        response.data as ChatDto[],
                        response.headers.link as string,
                        response.headers["x-total-pages"] as string
                    ),
                };
            default:
                return {
                    status: response.status,
                };
        }
    }

    return { loading, getChats };
};
