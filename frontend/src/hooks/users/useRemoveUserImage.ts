import { useDelete } from "../requests/useDelete";

export const useRemoveUserImage = () => {
    const { loading, callDelete } = useDelete();

    async function removeUserImage(imageUrl: string) {
        const response = await callDelete(imageUrl);
        return response.status;
    }

    return { loading, removeUserImage };
};
