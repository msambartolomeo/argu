export const useAuth = () => {
    const getAuthToken = () => {
        return localStorage.getItem("authToken");
    };

    const getRefreshToken = () => {
        return localStorage.getItem("refreshToken");
    };

    const setAuthToken = (token: string | null) => {
        if (token) {
            localStorage.setItem("authToken", token);
        } else {
            localStorage.removeItem("authToken");
        }
    };

    const setRefreshToken = (token: string | null) => {
        if (token) {
            localStorage.setItem("refreshToken", token);
        } else {
            localStorage.removeItem("refreshToken");
        }
    };

    return { getAuthToken, getRefreshToken, setAuthToken, setRefreshToken };
};
