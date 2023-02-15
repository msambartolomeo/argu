import { HttpStatusCode } from "axios";

import UserDto from "../../types/dto/UserDto";
import { useGet } from "../requests/useGet";

export interface GetUserByUrlInput {
    url: string;
}

export interface GetUserByUrlOutput {
    status: HttpStatusCode;
    data?: UserDto;
    message?: string;
}

export const useGetUserByUrl = () => {
    const { loading, callGet } = useGet();

    async function getUserByUrl({ url }: GetUserByUrlInput) {
        const response = await callGet(url, {}, false);

        switch (response.status) {
            case HttpStatusCode.Ok:
                return {
                    status: response.status,
                    data: response.data as UserDto,
                };
            default:
                return {
                    status: response.status,
                    message: response.data?.message,
                };
        }
    }
    return { loading, getUserByUrl };
};
