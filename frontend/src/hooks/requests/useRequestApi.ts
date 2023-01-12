import { useState } from "react";
import { useAuth } from "../useAuth";
import axios, { AxiosError, AxiosResponse } from "axios";
import { useNavigate } from "react-router-dom";
import { Buffer } from "buffer";

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
}

const AUTHORIZATION_HEADER = "authorization";
const REFRESH_HEADER = "x-refresh";

export const useRequestApi = () => {
    const [loading, setLoading] = useState(false);

    const { getAuthToken, getRefreshToken, setAuthToken, setRefreshToken } =
        useAuth();

    const navigate = useNavigate();

    async function requestApi(input: RequestApiInput): Promise<AxiosResponse> {
        const authToken = getAuthToken();
        const refreshToken = getRefreshToken();

        const { url, method, body, requiresAuth, credentials } = input;
        let { headers } = input;

        if (requiresAuth) {
            if (!authToken && !refreshToken && !credentials) {
                navigate("/login");
                throw new Error("No credentials provided");
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
        } else {
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
            return response;
        } catch (err) {
            if (axios.isAxiosError(err)) {
                const axiosError = err as AxiosError;

                // TODO: Preguntar si deberíamos también incluir 403 acá.
                if (axiosError.response?.status === 401) {
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
                return axiosError.response as AxiosResponse;
            }
            throw err;
        } finally {
            setLoading(false);
        }
    }
    return { loading, requestApi };
};
