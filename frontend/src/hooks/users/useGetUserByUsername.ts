import { useGetUserByUrl } from "./useGetUserByUrl";
import { USERS_ENDPOINT } from "./constants";

export interface GetUserByUsernameInput {
    username: string;
}

export const useGetUserByUsername = () => {
    const { data, loading, getUserByUrl } = useGetUserByUrl();

    async function getUserByUsername({ username }: GetUserByUsernameInput) {
        return await getUserByUrl({
            url: `${USERS_ENDPOINT}/${encodeURI(username)}`,
        });
    }
    return { data, loading, getUserByUsername };
};
