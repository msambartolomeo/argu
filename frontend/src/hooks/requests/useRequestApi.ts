import { Buffer } from "buffer";

import { useState } from "react";

import { useNavigate } from "react-router-dom";

import axios, { AxiosError, AxiosResponse, HttpStatusCode } from "axios";

import { useSharedAuth } from "../useAuth";
import { AUTHORIZATION_HEADER, REFRESH_HEADER } from "./constants";

export interface BasicCredentials {
    username: string;
    password: string;
}

export interface RequestApiInput {
    url: string;
    method?: string;
    body?: object;
    headers?: Record<string, string>;
    requiresAuth?: boolean;
    credentials?: BasicCredentials;
    queryParams?: Record<string, string>;
}

export const useRequestApi = () => {
    const [loading, setLoading] = useState(false);

    const { getAuthToken, getRefreshToken, setAuthToken, setRefreshToken } =
        useSharedAuth();

    const navigate = useNavigate();

    async function requestApi(input: RequestApiInput): Promise<AxiosResponse> {
        const authToken = getAuthToken();
        const refreshToken = getRefreshToken();

        const { url, method, body, requiresAuth, credentials } = input;
        let { headers } = input;

        if (requiresAuth) {
            if (!authToken && !refreshToken && !credentials) {
                // TODO: If we already throw an error (to avoid infinite recursion), should we still navigate to login or leave it to component?
                navigate("/login", {
                    state: { from: window.location.pathname },
                });
                throw new Error("No auth token or credentials");
            }
        }
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
        } else if (credentials) {
            const encodedBasic = Buffer.from(
                `${credentials?.username}:${credentials?.password}`
            ).toString("base64");
            headers = {
                Authorization: `Basic ${encodedBasic}`,
                ...headers,
            };
        }

        setLoading(true);
        try {
            const response = await axios({
                url,
                method: method || "GET",
                data: body,
                headers: headers,
                params: input.queryParams,
            });
            if (requiresAuth) {
                if (response.headers[AUTHORIZATION_HEADER]) {
                    setAuthToken(response.headers[AUTHORIZATION_HEADER]);
                }
                if (response.headers[REFRESH_HEADER]) {
                    setRefreshToken(response.headers[REFRESH_HEADER]);
                }
            }
            return response;
        } catch (err) {
            if (axios.isAxiosError(err)) {
                const axiosError = err as AxiosError;

                const response = axiosError.response;

                // TODO: Preguntar si deberíamos también incluir 403 acá.
                if (response?.status === HttpStatusCode.Unauthorized) {
                    if (authToken) {
                        // NOTE: authToken expired or invalid, trying again with refreshToken
                        setAuthToken(null);
                    } else if (refreshToken) {
                        // NOTE: refreshToken expired or invalid, forcing login with Basic
                        setRefreshToken(null);
                    }
                    await requestApi({
                        url,
                        method,
                        body,
                        headers,
                        requiresAuth,
                    });
                }

                if (requiresAuth) {
                    if (response?.headers[AUTHORIZATION_HEADER]) {
                        setAuthToken(response.headers[AUTHORIZATION_HEADER]);
                    }
                    if (response?.headers[REFRESH_HEADER]) {
                        setRefreshToken(response.headers[REFRESH_HEADER]);
                    }
                }
                return axiosError.response as AxiosResponse;
            }
            throw err;
        } finally {
            setLoading(false);
        }
    }
    return { loading, requestApi };
};
