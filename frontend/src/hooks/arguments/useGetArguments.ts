import { HttpStatusCode } from "axios";

import { PaginatedList } from "../../types/PaginatedList";
import PaginatedOutput from "../../types/PaginatedOutput";
import ArgumentDto from "../../types/dto/ArgumentDto";
import { useGet } from "../requests/useGet";

export interface GetArgumentsInput {
    argumentsUrl: string;
}

export const useGetArguments = () => {
    const { loading, callGet } = useGet();

    async function getArguments({
        argumentsUrl,
    }: GetArgumentsInput): Promise<PaginatedOutput<ArgumentDto>> {
        const response = await callGet(argumentsUrl, {}, false, {});
        switch (response.status) {
            case HttpStatusCode.Ok:
                return {
                    status: response.status,
                    data: new PaginatedList<ArgumentDto>(
                        response.data as ArgumentDto[],
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

    return { loading, getArguments };
};
