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
        return { status: response.status, message: response.data.message };
    }

    return { loading, updateUserImage };
};
