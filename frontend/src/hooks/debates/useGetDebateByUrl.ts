import { HttpStatusCode } from "axios";

import DebateDto from "../../types/dto/DebateDto";
import { useGet } from "../requests/useGet";

export interface GetDebateByUrlInput {
    url: string;
}

export interface GetDebateOutput {
    status: HttpStatusCode;
    data?: DebateDto;
    message?: string;
}

export const useGetDebateByUrl = () => {
    const { loading, callGet } = useGet();

    async function getDebateByUrl({
        url,
    }: GetDebateByUrlInput): Promise<GetDebateOutput> {
        const response = await callGet(url, {
            Accept: "application/json",
        });

        switch (response.status) {
            case HttpStatusCode.Ok:
                return {
                    status: response.status,
                    data: response.data as DebateDto,
                };

            case HttpStatusCode.NotFound:
                return {
                    status: response.status,
                    message: response.data?.message,
                };
            default:
                return {
                    status: response.status,
                    message: response.data?.message,
                };
        }
    }

    return { loading, getDebateByUrl };
};
