export const useAuth = () => {
    // TODO: Deberíamos tener una forma de guardar estos token en la memoria de la página
    // sin siempre buscar a localStorage. Esto es para que no nos modifiquen localStorage
    // mientras se navega y se rompa algo (podría devolver 403 un pedido)
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
