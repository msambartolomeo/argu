import jwtDecode from "jwt-decode";
import { useBetween } from "use-between";

import { useState } from "react";

import UserRole from "../types/enums/UserRole";

interface UserInfo {
    sub: string;
    exp: number;
    username: string;
    email: string;
    role: UserRole;
    points: number;
}

const useAuth = () => {
    // TODO: Deberíamos tener una forma de guardar estos token en la memoria de la página
    // sin siempre buscar a localStorage. Esto es para que no nos modifiquen localStorage
    // mientras se navega y se rompa algo (podría devolver 403 un pedido)

    const [userInfo, setUserInfo] = useState<UserInfo | null>(() => {
        const token = localStorage.getItem("authToken")?.split(" ")[1];
        if (token) {
            return jwtDecode(token);
        }
        return null;
    });

    console.log(userInfo);

    const getAuthToken = () => {
        return localStorage.getItem("authToken");
    };

    const getRefreshToken = () => {
        return localStorage.getItem("refreshToken");
    };

    const setAuthToken = (token: string | null) => {
        if (token) {
            localStorage.setItem("authToken", token);
            setUserInfo(jwtDecode(token.split(" ")[1]));
        } else {
            localStorage.removeItem("authToken");
            setUserInfo(null);
        }
    };

    const setRefreshToken = (token: string | null) => {
        if (token) {
            localStorage.setItem("refreshToken", token);
        } else {
            localStorage.removeItem("refreshToken");
        }
    };

    return {
        getAuthToken,
        getRefreshToken,
        setAuthToken,
        setRefreshToken,
        userInfo,
    };
};

export const useSharedAuth = () => useBetween(useAuth);
