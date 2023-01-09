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

    const { authToken, refreshToken, replaceAuthToken, replaceRefreshToken } =
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
        if (requiresAuth) {
            if (authToken) {
                console.log("authToken", authToken);
                headers = {
                    Authorization: `${authToken}`,
                    ...headers,
                };
            } else if (refreshToken) {
                console.log("refreshToken", refreshToken);
                headers = {
                    Authorization: `${refreshToken}`,
                    ...headers,
                };
            } else {
                console.log("BasicCredentials", BasicCredentials);
                // Encode to base64
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
                headers: {
                    "Content-Type": "application/json",
                    ...headers,
                },
            });
            if (requiresAuth) {
                // NOTE: axios forces all headers into lower case
                if (response.headers[AUTHORIZATION_HEADER]) {
                    replaceAuthToken(response.headers[AUTHORIZATION_HEADER]);
                }
                if (response.headers[REFRESH_HEADER]) {
                    replaceRefreshToken(response.headers[REFRESH_HEADER]);
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
