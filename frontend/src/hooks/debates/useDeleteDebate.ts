import { useDelete } from "../requests/useDelete";

export const useDeleteDebate = () => {
    const { loading, callDelete } = useDelete();

    async function deleteDebate(url: string): Promise<number> {
        const response = await callDelete(url, {}, true);
        return response.status;
    }

    return { loading, deleteDebate };
};
