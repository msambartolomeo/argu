import { zodResolver } from "@hookform/resolvers/zod";
import { FieldValues, useForm } from "react-hook-form";
import { useTranslation } from "react-i18next";
import { z } from "zod";

import { useEffect } from "react";

import { useNavigate } from "react-router-dom";

import InputField from "../../components/InputField/InputField";
import SubmitButton from "../../components/SubmitButton/SubmitButton";
import "../../locales/index";
import "./Login.css";

const Login = () => {
    const navigate = useNavigate();
    const { t } = useTranslation();

    const handleRedirect = () => {
        navigate("/register");
    };

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
    } = useForm({
        resolver: zodResolver(schema),
    });

    return (
        <div className="card login-container">
            <form
                acceptCharset="utf-8"
                onSubmit={handleSubmit((data) => {
                    // TODO: Implement login
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
