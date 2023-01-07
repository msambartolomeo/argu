import { useState } from "react";

export const useAuth = () => {
    const [authToken, setAuthToken] = useState<string | null>(
        localStorage.getItem("authToken")
    );
    const [refreshToken, setRefreshToken] = useState<string | null>(
        localStorage.getItem("refreshToken")
    );

    const replaceAuthToken = (authToken: string | null) => {
        setAuthToken(authToken);
        if (authToken) {
            localStorage.setItem("authToken", authToken);
        } else {
            localStorage.removeItem("authToken");
        }
    };

    const replaceRefreshToken = (refreshToken: string | null) => {
        setRefreshToken(refreshToken);
        if (refreshToken) {
            localStorage.setItem("refreshToken", refreshToken);
        } else {
            localStorage.removeItem("refreshToken");
        }
    };

    return { authToken, refreshToken, replaceAuthToken, replaceRefreshToken };
};
