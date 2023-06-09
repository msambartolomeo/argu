import { useState } from "react";

import axios, { AxiosError, AxiosResponse, HttpStatusCode } from "axios";
import { Buffer } from "buffer";
import { useNavigate } from "react-router-dom";

import { AUTHORIZATION_HEADER, REFRESH_HEADER } from "./constants";

import i18n from "../../locales";
import { useSharedAuth } from "../useAuth";

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
                navigate("/login", {
                    state: { from: window.location.pathname.substring(13) },
                });
                throw new Error("No auth token or credentials. Please login");
            }
        }

        if (credentials) {
            const encodedBasic = Buffer.from(
                `${credentials?.username}:${credentials?.password}`
            ).toString("base64");
            headers = {
                Authorization: `Basic ${encodedBasic}`,
                ...headers,
            };
        } else if (authToken) {
            headers = {
                Authorization: `${authToken}`,
                ...headers,
            };
        } else if (refreshToken) {
            headers = {
                Authorization: `${refreshToken}`,
                ...headers,
            };
        }

        setLoading(true);
        try {
            const response = await axios({
                url,
                method: method || "GET",
                data: body,
                headers: {
                    "Accept-Language": i18n.language,
                    ...headers,
                },
                params: input.queryParams,
            });

            if (response.headers[AUTHORIZATION_HEADER]) {
                setAuthToken(response.headers[AUTHORIZATION_HEADER]);
            }
            if (response.headers[REFRESH_HEADER]) {
                setRefreshToken(response.headers[REFRESH_HEADER]);
            }
            return response;
        } catch (err) {
            if (axios.isAxiosError(err)) {
                const axiosError = err as AxiosError;

                const response = axiosError.response;

                if (response?.status === HttpStatusCode.Unauthorized) {
                    if (credentials) {
                        // NOTE: Basic credentials invalid, only possible during login, returning
                        return axiosError.response as AxiosResponse;
                    }
                    if (authToken) {
                        // NOTE: authToken expired or invalid, trying again with refreshToken
                        setAuthToken(null);
                    } else if (refreshToken) {
                        // NOTE: refreshToken expired or invalid, forcing login with Basic
                        setRefreshToken(null);
                    }

                    if (headers) {
                        delete headers["Authorization"];
                    }

                    return await requestApi({
                        url,
                        method,
                        body,
                        headers,
                        requiresAuth,
                    });
                }

                if (response?.headers[AUTHORIZATION_HEADER]) {
                    setAuthToken(response.headers[AUTHORIZATION_HEADER]);
                }
                if (response?.headers[REFRESH_HEADER]) {
                    setRefreshToken(response.headers[REFRESH_HEADER]);
                }

                if (axiosError.response) {
                    return axiosError.response as AxiosResponse;
                }
                return {
                    status: 503,
                } as AxiosResponse;
            }
            throw err;
        } finally {
            setLoading(false);
        }
    }
    return { loading, requestApi };
};
