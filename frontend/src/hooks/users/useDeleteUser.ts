import { USERS_ENDPOINT } from "./constants";

import { useDelete } from "../requests/useDelete";

export const useDeleteUser = () => {
    const { loading, callDelete } = useDelete();

    async function deleteUser(url: string) {
        // TODO: Maybe don't accept id as a parameter and get it from the context
        const response = await callDelete(
            USERS_ENDPOINT + encodeURI(url),
            {},
            true
        );

        // TODO: Validate return object
        return {
            status: response.status,
            message: response.data.message,
        };
    }
    return { loading, deleteUser };
};
