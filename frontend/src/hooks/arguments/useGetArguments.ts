import { useGet } from "../requests/useGet";
import { DEBATES_ENDPOINT } from "../debates/constants";
import { ARGUMENTS_ENDPOINT } from "./constants";

export interface GetArgumentsInput {
    debateId: number;
    page: number;
    size: number;
}

export const useGetArguments = () => {
    const { loading, callGet } = useGet();

    async function getArguments({ debateId, page, size }: GetArgumentsInput) {
        const response = await callGet(
            DEBATES_ENDPOINT + debateId + ARGUMENTS_ENDPOINT,
            {},
            false,
            { page: page.toString(), size: size.toString() }
        );
        return response.data;
    }

    return { loading, getArguments };
};
