import { HttpStatusCode } from "axios";
import { PaginatedList } from "../../types/PaginatedList";
import ChatDto from "../../types/dto/ChatDto";
import { useGet } from "../requests/useGet";
import PaginatedOutput from "../../types/PaginatedOutput";

export interface GetChatsInput {
    debateUrl: string;
    page: number;
    size: number;
}

export const useGetChats = () => {
    const { loading, callGet } = useGet();

    async function getChats({
        debateUrl,
        page,
        size,
    }: GetChatsInput): Promise<PaginatedOutput<ChatDto>> {
        const response = await callGet(debateUrl, {}, false, {
            page: page.toString(),
            size: size.toString(),
        });
        switch (response.status) {
            case HttpStatusCode.Ok:
                return {
                    status: response.status,
                    data: new PaginatedList<ChatDto>(
                        response.data as ChatDto[],
                        response.headers.link as string
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
