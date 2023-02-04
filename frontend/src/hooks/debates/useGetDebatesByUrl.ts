import { HttpStatusCode } from "axios";

import { PaginatedList } from "../../types/PaginatedList";
import PaginatedOutput from "../../types/PaginatedOutput";
import DebateDto from "../../types/dto/DebateDto";
import { useGet } from "../requests/useGet";

export interface GetDebatesByUrlInput {
    url: string;

    page?: number;
    size?: number;
}

export interface GetDebatesByUrlOutput {
    status: HttpStatusCode;
    data?: PaginatedList<DebateDto>;
    message?: string;
}

export const useGetDebatesByUrl = () => {
    const { loading, callGet } = useGet();

    async function getDebatesByUrl({
        url,
        page,
        size,
    }: GetDebatesByUrlInput): Promise<PaginatedOutput<DebateDto>> {
        const response = await callGet(url, {}, false, {
            page: page?.toString() ?? "0",
            size: size?.toString() ?? "10",
        });

        switch (response.status) {
            case HttpStatusCode.Ok:
                return {
                    status: response.status,
                    data: new PaginatedList<DebateDto>(
                        response.data as DebateDto[],
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
    return { loading, getDebatesByUrl };
};
