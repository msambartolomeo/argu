import { HttpStatusCode } from "axios";

import { CreateArgumentInput, CreateArgumentOutput } from "./useCreateArgument";

import { usePost } from "../requests/usePost";

export interface CreateArgumentWithImageInput extends CreateArgumentInput {
    image: File;
}

export const useCreateArgumentWithImage = () => {
    const { loading, callPost } = usePost();

    async function createArgumentWithImage({
        argumentsURL,
        content,
        image,
    }: CreateArgumentWithImageInput): Promise<CreateArgumentOutput> {
        const formData = new FormData();
        formData.append("content", content);
        formData.append("image", image);

        const response = await callPost(
            argumentsURL,
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
