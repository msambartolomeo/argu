import { useState } from "react";

import { HttpStatusCode } from "axios";

import { useGet } from "../requests/useGet";

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
