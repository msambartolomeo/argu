import { useState } from "react";
import { useAuth } from "./useAuth";
import axios, { AxiosError } from "axios";

export const useRequestApi = () => {
    const [data, setData] = useState(null);
    const [error, setError] = useState<Error | null>(null);
    const [loading, setLoading] = useState(false);

    const { authToken, refreshToken, replaceAuthToken, replaceRefreshToken } =
        useAuth();

    async function requestApi(
        url: string,
        method?: string,
        body?: object,
        headers?: object,
        requiresAuth?: boolean
    ) {
        if (requiresAuth) {
            if (authToken) {
                headers = {
                    Authentication: `Bearer ${authToken}`,
                    ...headers,
                };
            } else if (refreshToken) {
                headers = {
                    Authentication: `Bearer ${refreshToken}`,
                    ...headers,
                };
            } else {
                // TODO: Force login & use Basic
            }
        }

        setLoading(true);
        try {
            const response = await axios({
                url,
                method: method || "GET",
                data: body,
                headers: {
                    "Content-Type": "application/json",
                    ...headers,
                },
            });
            setData(response.data);
            if (requiresAuth) {
                if (response.headers["Autherization"]) {
                    replaceAuthToken(response.headers["Autherization"]);
                }
                if (response.headers["X-Refresh"]) {
                    replaceRefreshToken(response.headers["X-Refresh"]);
                }
            }
        } catch (err) {
            if (axios.isAxiosError(err)) {
                const axiosError = err as AxiosError;
                setError(axiosError);
                // NOTE: Server errors
                if (requiresAuth && axiosError.response?.status === 401) {
                    if (authToken) {
                        // NOTE: authToken expired or invalid, trying again with refreshToken
                        replaceAuthToken(null);
                    } else if (refreshToken) {
                        // NOTE: refreshToken expired or invalid, forcing login with Basic
                        replaceRefreshToken(null);
                    }
                    await requestApi(url, method, body, headers, requiresAuth);
                }
            }
        } finally {
            setLoading(false);
        }
    }
    return { data, error, loading, setData, setError, requestApi };
};
