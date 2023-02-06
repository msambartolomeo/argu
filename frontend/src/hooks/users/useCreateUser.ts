import { useState } from "react";

import { HttpStatusCode } from "axios";

import { USERS_ENDPOINT } from "./constants";

import { PostOutput } from "../../types/PostOutput";
import BadRequestError from "../../types/errors/BadRequestError";
import { usePost } from "../requests/usePost";

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

    async function createUser(inData: CreateUserInput) {
        const response = await callPost(USERS_ENDPOINT, inData, {}, false);

        switch (response.status) {
            case HttpStatusCode.Created:
                return {
                    status: response.status,
                    location: response.headers.location,
                };
            case HttpStatusCode.BadRequest:
                return {
                    status: response.status,
                    errors: response.data as BadRequestError[],
                };
            case HttpStatusCode.Conflict:
                return {
                    status: response.status,
                    errors: response.data as CreateUserConflictError,
                };
            default:
                return {
                    status: response.status,
                };
        }
    }
    return { loading, createUser };
};
