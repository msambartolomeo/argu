import { useDelete } from "../requests/useDelete";
import { USERS_ENDPOINT } from "./constants";

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
        return response.status;
    }
    return { loading, deleteUser };
};
