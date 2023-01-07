import { useState } from "react";

export const useRequestApi = () => {
    const [data, setData] = useState(null);
    const [error, setError] = useState<Error | null>(null);
    const [loading, setLoading] = useState(false);

    async function requestApi(
        url: string,
        method?: string,
        body?: object,
        headers?: HeadersInit
    ) {
        setLoading(true);
        try {
            const response = await fetch(url, {
                method: method || "GET",
                body: body ? JSON.stringify(body) : undefined,
                headers: {
                    Accept: "application/json",
                    ...headers,
                },
            });
            const json = await response.json();
            setData(json);
        } catch (error) {
            setError(error as Error);
        } finally {
            setLoading(false);
        }
    }
    return { data, error, loading, setData, setError, requestApi };
};
