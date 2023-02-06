import { HttpStatusCode } from "axios";

import { DEBATES_ENDPOINT } from "./constants";
import { CreateDebateInput, CreateDebateOutput } from "./useCreateDebate";

import BadRequestError from "../../types/errors/BadRequestError";
import { usePost } from "../requests/usePost";

export interface CreateDebateWithImageInput extends CreateDebateInput {
    image: File;
}

export const createDebateWithImage = () => {
    const { loading, callPost } = usePost();

    async function createDebateWithImage(
        inData: CreateDebateWithImageInput
    ): Promise<CreateDebateOutput> {
        const formData = new FormData();
        formData.append("image", inData.image);
        formData.append("title", inData.title);
        formData.append("description", inData.description);
        formData.append("category", inData.category);
        formData.append("isCreatorFor", inData.isCreatorFor.toString());
        formData.append("opponentUsername", inData.opponentUsername);

        const response = await callPost(
            DEBATES_ENDPOINT,
            formData,
            {
                "Content-Type": "multipart/form-data",
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

    return { loading, createDebateWithImage };
};
