import { HttpStatusCode } from "axios";
import { usePost } from "../requests/usePost";
import { argumentsEndpoint } from "./constants";
import { CreateArgumentInput, CreateArgumentOutput } from "./useCreateArgument";

export interface CreateArgumentWithImageInput extends CreateArgumentInput {
    image: File;
}

export const createArgumentWithImage = () => {
    const { loading, callPost } = usePost();

    async function createArgumentWithImage(
        inData: CreateArgumentWithImageInput
    ): Promise<CreateArgumentOutput> {
        const formData = new FormData();
        formData.append("image", inData.image);
        formData.append("content", inData.content);

        const response = await callPost(
            argumentsEndpoint(inData.debateId),
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
            // TODO: Should be handle more errors? Only ones possible are 403 and maybe 409 (if the debate is closed), which should never happen.
            default:
                return {
                    status: response.status,
                };
        }
    }

    return { loading, createArgumentWithImage };
};
