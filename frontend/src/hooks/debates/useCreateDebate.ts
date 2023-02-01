import { HttpStatusCode } from "axios";

import { PostOutput } from "../../types/PostOutput";
import DebateCategory from "../../types/enums/DebateCategory";
import BadRequestError from "../../types/errors/BadRequestError";
import { usePost } from "../requests/usePost";
import { DEBATES_ENDPOINT } from "./constants";

export interface CreateDebateInput {
    title: string;
    description: string;
    category: DebateCategory;
    isCreatorFor: boolean;
    opponentUsername: string;
}

export interface CreateDebateOutput extends PostOutput {
    errors?: BadRequestError[];
}

export const useCreateDebate = () => {
    const { loading, callPost } = usePost();

    async function createDebate(
        inData: CreateDebateInput
    ): Promise<CreateDebateOutput> {
        const response = await callPost(
            DEBATES_ENDPOINT,
            inData,
            {
                "Content-Type": "application/json",
            },
            true
        );

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
            default:
                return {
                    status: response.status,
                };
        }
    }

    return { loading, createDebate };
};
