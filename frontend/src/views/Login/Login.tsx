import { useTranslation } from "react-i18next";

import { FormEvent, useState } from "react";

import { useNavigate } from "react-router-dom";

import InputField from "../../components/InputField/InputField";
import SubmitButton from "../../components/SubmitButton/SubmitButton";
import { useLogin } from "../../hooks/requests/useLogin";
import "../../locales/index";
import "./Login.css";

const Login = () => {
    const navigate = useNavigate();
    const { t } = useTranslation();

    const { callLogin } = useLogin();
    const [username, setUsername] = useState<string>();
    const [password, setPassword] = useState<string>();

    const handleRedirect = () => {
        navigate("/register");
    };

    const login = (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        // TODO: validate username and password are filled with zod
        callLogin(username || "", password || "");
        // FIXME: Go to last page
        navigate("/");
    };

    return (
        <div className="card login-container">
            <form method="post" acceptCharset="utf-8" onSubmit={login}>
                <div className="card-content container-with-space">
                    <span className="card-title center">
                        {t("login.welcomeBack")}
                    </span>
                    <InputField
                        text={t("login.username")}
                        onChange={(event) => setUsername(event.target.value)}
                    />
                    <InputField
                        text={t("login.password")}
                        type="password"
                        onChange={(event) => setPassword(event.target.value)}
                    />
                    <div className="left">
                        <label>
                            <input
                                type="checkbox"
                                className="filled-in"
                                name="rememberme"
                                id="rememberme"
                            />
                            <span>{t("login.rememberMe")}</span>
                        </label>
                    </div>
                    <SubmitButton text={t("login.login")} />
                </div>
            </form>
            <h6 className="center">
                {t("login.noAccount")}&nbsp;
                <a onClick={handleRedirect}>{t("login.register")}</a>
            </h6>
        </div>
    );
};

export default Login;
