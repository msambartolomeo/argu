import { useDelete } from "../requests/useDelete";
import { DEBATES_ENDPOINT } from "./constants";

export const useDeleteDebate = () => {
    const { loading, callDelete } = useDelete();

    async function deleteDebate(id: number): Promise<number> {
        const response = await callDelete(DEBATES_ENDPOINT + id, {}, true);
        return response.status;
    }

    return { loading, deleteDebate };
};
