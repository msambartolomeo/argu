import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { useTranslation } from "react-i18next";
import { z } from "zod";

import { useEffect } from "react";

import { useNavigate } from "react-router-dom";

import InputField from "../../components/InputField/InputField";
import SubmitButton from "../../components/SubmitButton/SubmitButton";

const Register = () => {
    const navigate = useNavigate();
    const { t } = useTranslation();

    const handleRedirect = () => {
        navigate("/login");
    };

    const schema = z
        .object({
            username: z
                .string()
                .max(64, t("login.usernameTooLong") as string)
                .min(1, t("login.usernameEmpty") as string),
            password: z
                .string()
                .max(100, t("login.passwordTooLong") as string)
                .min(1, t("login.passwordEmpty") as string),
            email: z
                .string()
                .email(t("register.invalidEmail") as string)
                .max(100, t("register.emailTooLong") as string)
                .min(1, t("register.emailEmpty") as string),
            confirmPassword: z.string(),
        }) // TODO: Add missing i18n strings
        .superRefine(({ confirmPassword, password }, ctx) => {
            if (confirmPassword !== password) {
                ctx.addIssue({
                    code: "custom",
                    message: t("register.passwordsDontMatch") as string,
                    path: ["confirmPassword"],
                });
            }
        });

    const {
        register,
        handleSubmit,
        formState: { errors },
    } = useForm({
        resolver: zodResolver(schema),
    });

    // TODO: Error handling and api call

    return (
        <div className="card login-container">
            <form
                acceptCharset="utf-8"
                onSubmit={handleSubmit((data) => {
                    // TODO: Implement register
                })}
            >
                <div className="card-content">
                    <span className="card-title center">
                        {t("register.firstTime")}
                    </span>
                    <InputField
                        text={t("register.email")}
                        type="email"
                        register={register("email")}
                    />
                    <InputField
                        text={t("register.username")}
                        register={register("username")}
                    />
                    <InputField
                        text={t("register.password")}
                        type="password"
                        register={register("password")}
                    />
                    <InputField
                        text={t("register.confirmPassword")}
                        type="password"
                        register={register("confirmPassword")}
                    />
                    <SubmitButton text={t("register.register")} />
                </div>
            </form>
            <h6 className="center">
                {t("register.alreadyHaveAccount")}
                <a onClick={handleRedirect}>{t("register.login")}</a>
            </h6>
        </div>
    );
};

export default Register;
