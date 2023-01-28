import { usePost } from "../requests/usePost";
import { USERS_ENDPOINT } from "./constants";
import BadRequestError from "../../types/errors/BadRequestError";
import { useState } from "react";
import { HttpStatusCode } from "axios";
import { PostOutput } from "../../types/PostOutput";

export interface CreateUserInput {
    username: string;
    email: string;
    password: string;
}

export interface CreateUserOutput extends PostOutput {
    errors?: BadRequestError[] | CreateUserConflictError;
}

export interface CreateUserConflictError {
    email: boolean;
    username: boolean;
    message: string;
    messageCode: string;
}

export const useCreateUser = () => {
    const { loading, callPost } = usePost();
    const [data, setData] = useState<CreateUserOutput>();

    async function createUser(inData: CreateUserInput) {
        const response = await callPost(USERS_ENDPOINT, inData, {}, false);

        switch (response.status) {
            case HttpStatusCode.Created:
                setData({
                    status: response.status,
                    location: response.headers.location,
                });
                break;
            case HttpStatusCode.BadRequest:
                setData({
                    status: response.status,
                    errors: response.data as BadRequestError[],
                });
                break;
            case HttpStatusCode.Conflict:
                setData({
                    status: response.status,
                    errors: response.data as CreateUserConflictError,
                });
                break;
            default:
                setData({
                    status: response.status,
                });
                break;
        }
    }
    return { data, loading, createUser };
};
