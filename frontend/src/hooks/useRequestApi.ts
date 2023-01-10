import { useState } from "react";
import { useAuth } from "./useAuth";
import axios, { AxiosError } from "axios";
import { useNavigate } from "react-router-dom";
import { Buffer } from "buffer";

export interface BasicCredentials {
    username: string;
    password: string;
}

const AUTHORIZATION_HEADER = "authorization";
const REFRESH_HEADER = "x-refresh";

export const useRequestApi = () => {
    const [data, setData] = useState(null);
    const [error, setError] = useState<Error | null>(null);
    const [loading, setLoading] = useState(false);

    const { getAuthToken, getRefreshToken, setAuthToken, setRefreshToken } =
        useAuth();

    async function requestApi(
        url: string,
        method?: string,
        body?: object,
        headers?: Record<string, string>,
        requiresAuth?: boolean,
        // TODO: Validate how to get credentials, maybe also stored in useAuth
        BasicCredentials?: BasicCredentials
    ) {
        const authToken = getAuthToken();
        const refreshToken = getRefreshToken();
        if (requiresAuth) {
            if (authToken) {
                headers = {
                    Authorization: `${authToken}`,
                    ...headers,
                };
            } else if (refreshToken) {
                headers = {
                    Authorization: `${refreshToken}`,
                    ...headers,
                };
            } else {
                const encodedBasic = Buffer.from(
                    `${BasicCredentials?.username}:${BasicCredentials?.password}`
                ).toString("base64");
                headers = {
                    Authorization: `Basic ${encodedBasic}`,
                    ...headers,
                };
            }
        }

        setLoading(true);
        try {
            const response = await axios({
                url,
                method: method || "GET",
                data: body,
                headers: headers,
            });
            if (requiresAuth) {
                // NOTE: axios forces all headers into lower case
                if (response.headers[AUTHORIZATION_HEADER]) {
                    setAuthToken(response.headers[AUTHORIZATION_HEADER]);
                }
                if (response.headers[REFRESH_HEADER]) {
                    setRefreshToken(response.headers[REFRESH_HEADER]);
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
                        setAuthToken(null);
                    } else if (refreshToken) {
                        // NOTE: refreshToken expired or invalid, forcing login with Basic
                        setRefreshToken(null);
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
