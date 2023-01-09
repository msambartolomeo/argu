import { useState } from "react";

export const useAuth = () => {
    const [authToken, setAuthToken] = useState<string | null>(
        localStorage.getItem("authToken")
    );
    const [refreshToken, setRefreshToken] = useState<string | null>(
        localStorage.getItem("refreshToken")
    );

    const replaceAuthToken = (token: string | null) => {
        setAuthToken(token);
        if (token) {
            localStorage.setItem("authToken", token);
        } else {
            localStorage.removeItem("authToken");
        }
    };

    const replaceRefreshToken = (token: string | null) => {
        setRefreshToken(token);
        if (token) {
            localStorage.setItem("refreshToken", token);
        } else {
            localStorage.removeItem("refreshToken");
        }
    };

    return { authToken, refreshToken, replaceAuthToken, replaceRefreshToken };
};
