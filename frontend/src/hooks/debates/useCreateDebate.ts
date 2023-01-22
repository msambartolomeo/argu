import { HttpStatusCode } from "axios";
import DebateCategory from "../../types/enums/DebateCategory";
import { usePost } from "../requests/usePost";
import { DEBATES_ENDPOINT } from "./constants";
import { useState } from "react";
import { PostOutput } from "../../types/PostOutput";
import BadRequestError from "../../types/errors/BadRequestError";

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
    const [data, setData] = useState<CreateDebateOutput>();

    async function createDebate(inData: CreateDebateInput): Promise<void> {
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
            default:
                setData({
                    status: response.status,
                });
        }
    }

    return { data, loading, createDebate };
};
