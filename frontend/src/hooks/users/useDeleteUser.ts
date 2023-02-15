import { USERS_ENDPOINT } from "./constants";

import { useDelete } from "../requests/useDelete";

export const useDeleteUser = () => {
    const { loading, callDelete } = useDelete();

    async function deleteUser(url: string) {
        const response = await callDelete(
            USERS_ENDPOINT + encodeURI(url),
            {},
            true
        );

        return {
            status: response.status,
            message: response.data.message,
        };
    }
    return { loading, deleteUser };
};
