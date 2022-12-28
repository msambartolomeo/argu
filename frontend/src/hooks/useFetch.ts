import { useState } from "react";

export const useFetch = () => {
    const [data, setData] = useState(null);
    const [error, setError] = useState<Error | null>(null);
    const [loading, setLoading] = useState(false);

    async function fetchData(endpoint: string) {
        setLoading(true);
        try {
            const response = await fetch(
                "http://localhost:8080/paw-2022a-06/api/" + endpoint
            );
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
