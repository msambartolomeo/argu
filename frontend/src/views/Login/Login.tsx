import { FieldValues, useForm } from "react-hook-form";
import { useTranslation } from "react-i18next";
import { useLocation, useNavigate } from "react-router-dom";
import { z } from "zod";

import { zodResolver } from "@hookform/resolvers/zod";

import "./Login.css";

import InputField from "../../components/InputField/InputField";
import SubmitButton from "../../components/SubmitButton/SubmitButton";
import { useLogin } from "../../hooks/requests/useLogin";
import "../../locales/index";

const Login = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const { t } = useTranslation();

    document.title = "Argu | " + t("login.login");

    const { loading, callLogin } = useLogin();

    const schema = z.object({
        username: z
            .string()
            .max(64, t("login.usernameTooLong") as string)
            .min(1, t("login.usernameEmpty") as string),
        password: z
            .string()
            .max(100, t("login.passwordTooLong") as string)
            .min(1, t("login.passwordEmpty") as string),
    });

    const {
        register,
        handleSubmit,
        formState: { errors },
        setError,
    } = useForm({
        resolver: zodResolver(schema),
    });

    const handleRedirectRegister = () => {
        navigate("/register");
    };

    const handleLogin = async (data: FieldValues) => {
        const loggedIn = await callLogin(data.username, data.password);
        if (loggedIn) {
            // NOTE: Redirect to previous page if it exists, otherwise redirect to home
            location.state?.from
                ? navigate(location.state.from, { replace: true })
                : navigate("/", { replace: true });
        } else {
            setError("password", {
                message: t(
                    "login.errors.usernameOrPasswordIncorrect"
                ) as string,
            });
            setError("username", {
                message: t(
                    "login.errors.usernameOrPasswordIncorrect"
                ) as string,
            });
        }
    };

    return (
        <div className="card login-container">
            <form
                acceptCharset="utf-8"
                onSubmit={handleSubmit((data) => {
                    handleLogin(data);
                })}
            >
                <div className="card-content container-with-space">
                    <span className="card-title center">
                        {t("login.welcomeBack")}
                    </span>
                    <InputField
                        text={t("login.username")}
                        register={register("username")}
                        error={errors.username?.message?.toString()}
                    />
                    <InputField
                        text={t("login.password")}
                        type="password"
                        register={register("password")}
                        error={errors.password?.message?.toString()}
                    />
                    <SubmitButton text={t("login.login")} disabled={loading} />
                </div>
            </form>
            <h6 className="center">
                {t("login.noAccount")}&nbsp;
                <a onClick={handleRedirectRegister}>{t("login.register")}</a>
            </h6>
        </div>
    );
};

export default Login;
