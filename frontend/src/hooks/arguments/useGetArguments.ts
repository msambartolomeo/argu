import { useGet } from "../requests/useGet";
import ArgumentDto from "../../types/dto/ArgumentDto";
import { PaginatedList } from "../../types/PaginatedList";
import { HttpStatusCode } from "axios";
import PaginatedOutput from "../../types/PaginatedOutput";

export interface GetArgumentsInput {
    argumentsUrl: string;
    page: number;
    size: number;
}

export const useGetArguments = () => {
    const { loading, callGet } = useGet();

    async function getArguments({
        argumentsUrl,
        page,
        size,
    }: GetArgumentsInput): Promise<PaginatedOutput<ArgumentDto>> {
        const response = await callGet(argumentsUrl, {}, false, {
            page: page.toString(),
            size: size.toString(),
        });
        switch (response.status) {
            case HttpStatusCode.Ok:
                return {
                    status: response.status,
                    data: new PaginatedList<ArgumentDto>(
                        response.data as ArgumentDto[],
                        response.headers.link as string
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
