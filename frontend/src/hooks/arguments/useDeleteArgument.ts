import { useDelete } from "../requests/useDelete";
import { argumentsEndpoint } from "./constants";

export const useDeleteArgument = () => {
    const { loading, callDelete } = useDelete();

    async function deleteArgument(argumentId: number): Promise<number> {
        const response = await callDelete(
            argumentsEndpoint(argumentId),
            {},
            true
        );
        return response.status;
    }

    return { loading, deleteArgument };
};
