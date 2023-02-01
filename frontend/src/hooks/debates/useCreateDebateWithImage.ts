import { HttpStatusCode } from "axios";
import { usePost } from "../requests/usePost";
import { DEBATES_ENDPOINT } from "./constants";
import { useState } from "react";
import BadRequestError from "../../types/errors/BadRequestError";
import { CreateDebateInput, CreateDebateOutput } from "./useCreateDebate";

export interface CreateDebateWithImageInput extends CreateDebateInput {
    image: File;
}

export const createDebateWithImage = () => {
    const { loading, callPost } = usePost();
    const [data, setData] = useState<CreateDebateOutput>();

    async function createDebateWithImage(
        inData: CreateDebateWithImageInput
    ): Promise<void> {
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

    return { data, loading, createDebate: createDebateWithImage };
};
