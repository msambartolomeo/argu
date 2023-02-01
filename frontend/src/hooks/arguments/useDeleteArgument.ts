import { useDelete } from "../requests/useDelete";

export const useDeleteArgument = () => {
    const { loading, callDelete } = useDelete();

    async function deleteArgument(argumentsURL: string): Promise<number> {
        const response = await callDelete(argumentsURL, {}, true);
        return response.status;
    }

    return { loading, deleteArgument };
};
