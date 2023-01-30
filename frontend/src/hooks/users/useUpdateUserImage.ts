import { usePut } from "../requests/usePut";
import { imageEndpoint, USERS_ENDPOINT } from "./constants";

export interface UseUpdateUserImageInput {
    username: string;
    image: File;
}

export const useUpdateUserImage = () => {
    const { loading, callPut } = usePut();

    async function updateUserImage({
        username,
        image,
    }: UseUpdateUserImageInput) {
        // TODO: Validate MimeType header usage
        const formData = new FormData();
        formData.append("image", image);

        const response = await callPut(
            imageEndpoint(encodeURI(username)),
            formData,
            {
                "Content-Type": "multipart/form-data",
            },
            true
        );
        return response.status;
    }

    return { loading, updateUserImage };
};
