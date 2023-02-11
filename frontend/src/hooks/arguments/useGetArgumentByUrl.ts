import { HttpStatusCode } from "axios";

import ArgumentDto from "../../types/dto/ArgumentDto";
import { useGet } from "../requests/useGet";

export interface GetArgumentByUrlInput {
    url: string;
}

export interface GetArgumentByUrlOutput {
    status: HttpStatusCode;
    data?: ArgumentDto;
    message?: string;
}

export const useGetArgumentByUrl = () => {
    const { loading, callGet } = useGet();

    async function getArgumentByUrl({ url }: GetArgumentByUrlInput) {
        const response = await callGet(url, {}, false);

        switch (response.status) {
            case HttpStatusCode.Ok:
                return {
                    status: response.status,
                    data: response.data as ArgumentDto,
                };
            default:
                return {
                    status: response.status,
                    message: response.data?.message,
                };
        }
    }
    return { loading, getArgumentByUrl };
};
