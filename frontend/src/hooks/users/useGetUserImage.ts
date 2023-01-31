import { useState } from "react";
import { useGet } from "../requests/useGet";
import { HttpStatusCode } from "axios";

export const useGetUserImage = () => {
    // TODO: Validate HTMLImageElement type
    const [data, setData] = useState<HTMLImageElement | null>(null);
    const { loading, callGet } = useGet();

    async function getUserImage(imageUrl: string) {
        const response = await callGet(imageUrl);

        if (response.status === HttpStatusCode.Ok) {
            setData(response.data as HTMLImageElement);
        }
        return response.status;
    }

    return { data, loading, getUserImage };
};
