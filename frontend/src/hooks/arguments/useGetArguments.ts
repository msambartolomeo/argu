import { useGet } from "../requests/useGet";
import { argumentsEndpoint } from "./constants";
import ArgumentDto from "../../types/dto/ArgumentDto";

export interface GetArgumentsInput {
    debateId: number;
    page: number;
    size: number;
}

export const useGetArguments = () => {
    const { loading, callGet } = useGet();

    async function getArguments({
        debateId,
        page,
        size,
    }: GetArgumentsInput): Promise<ArgumentDto[]> {
        const response = await callGet(argumentsEndpoint(debateId), {}, false, {
            page: page.toString(),
            size: size.toString(),
        });
        return response.data as ArgumentDto[];
    }

    return { loading, getArguments };
};
