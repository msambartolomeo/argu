import { useState } from "react";

export const useFetch = () => {
    const [data, setData] = useState(null);
    const [error, setError] = useState<Error | null>(null);
    const [loading, setLoading] = useState(false);

    async function fetchData(url: string) {
        setLoading(true);
        try {
            const response = await fetch(url);
            const json = await response.json();
            setData(json);
        } catch (error) {
            setError(error as Error);
        } finally {
            setLoading(false);
        }
    }
    return { data, error, loading, fetchData };
};
