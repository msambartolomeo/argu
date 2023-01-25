import { useState } from "react";
import UserDto from "../../types/dto/UserDto";
import { useGet } from "../requests/useGet";
import { HttpStatusCode } from "axios";

export interface GetUserByUrlInput {
    url: string;
}

export const useGetUserByUrl = () => {
    const { loading, callGet } = useGet();
    const [data, setData] = useState<UserDto | null>();

    async function getUserByUrl({ url }: GetUserByUrlInput) {
        const response = await callGet(url, {}, false);

        if (response.status === HttpStatusCode.Ok) {
            setData(response.data as UserDto);
        }
        return response.status;
    }
    return { data, loading, getUserByUrl };
};
