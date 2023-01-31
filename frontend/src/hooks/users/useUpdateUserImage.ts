import { usePut } from "../requests/usePut";

export interface UseUpdateUserImageInput {
    imageUrl: string;
    image: File;
}

export const useUpdateUserImage = () => {
    const { loading, callPut } = usePut();

    async function updateUserImage({
        imageUrl,
        image,
    }: UseUpdateUserImageInput) {
        // TODO: Validate MimeType header usage
        const formData = new FormData();
        formData.append("image", image);

        const response = await callPut(
            imageUrl,
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
