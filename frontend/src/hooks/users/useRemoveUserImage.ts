import { useDelete } from "../requests/useDelete";

export const useRemoveUserImage = () => {
    const { loading, callDelete } = useDelete();

    async function removeUserImage(url: string) {
        const response = await callDelete(url);
        return response.status;
    }

    return { loading, removeUserImage };
};
