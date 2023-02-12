import { useCallback } from "react";

import { HttpStatusCode } from "axios";
import { useSnackbar } from "notistack";
import { useForm } from "react-hook-form";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import { z } from "zod";

import { zodResolver } from "@hookform/resolvers/zod";

import InputField from "../../components/InputField/InputField";
import SubmitButton from "../../components/SubmitButton/SubmitButton";
import { useLogin } from "../../hooks/requests/useLogin";
import {
    CreateUserConflictError,
    CreateUserInput,
    useCreateUser,
} from "../../hooks/users/useCreateUser";

const Register = () => {
    const navigate = useNavigate();
    const { t } = useTranslation();
    const { enqueueSnackbar } = useSnackbar();

    document.title = "Argu | " + t("register.register");

    const handleRedirect = () => {
        navigate("/login");
    };

    const schema = z
        .object({
            username: z
                .string()
                .max(64, t("register.errors.usernameTooLong") as string)
                .min(1, t("register.errors.usernameEmpty") as string),
            password: z
                .string()
                .max(100, t("register.errors.passwordTooLong") as string)
                .min(1, t("register.errors.passwordEmpty") as string),
            email: z
                .string()
                .email(t("register.errors.emailInvalid") as string)
                .max(100, t("register.errors.emailTooLong") as string)
                .min(1, t("register.errors.emailEmpty") as string),
            confirmPassword: z.string(),
        })
        .superRefine(({ confirmPassword, password }, ctx) => {
            if (confirmPassword !== password) {
                ctx.addIssue({
                    code: "custom",
                    message: t("register.errors.passwordsDontMatch") as string,
                    path: ["confirmPassword"],
                });
            }
        });

    const {
        register,
        handleSubmit,
        formState: { errors: formErrors },
        setError,
    } = useForm({
        resolver: zodResolver(schema),
    });

    const { loading, createUser } = useCreateUser();
    const { callLogin } = useLogin();

    const onSubmit = useCallback(async (data: CreateUserInput) => {
        const response = await createUser(data);
        switch (response.status) {
            case HttpStatusCode.Created:
                {
                    const loggedIn = await callLogin(
                        data.username,
                        data.password
                    );
                    if (loggedIn) navigate("/");
                }
                break;
            case HttpStatusCode.Conflict:
                {
                    const error = response.errors as CreateUserConflictError;
                    if (error.username) {
                        setError("username", {
                            message: t(
                                "register.errors.usernameTaken"
                            ) as string,
                        });
                    }
                    if (error.email) {
                        setError("email", {
                            message: t("register.errors.emailTaken") as string,
                        });
                    }
                }
                break;
            default:
                enqueueSnackbar(t("errors.unexpected"), {
                    variant: "error",
                });
                break;
        }
    }, []);

    return (
        <div className="card login-container">
            <form
                acceptCharset="utf-8"
                onSubmit={handleSubmit((data) => {
                    onSubmit(data as CreateUserInput);
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
                        error={formErrors.email?.message as string}
                    />
                    <InputField
                        text={t("register.username")}
                        register={register("username")}
                        error={formErrors.username?.message as string}
                    />
                    <InputField
                        text={t("register.password")}
                        type="password"
                        register={register("password")}
                        error={formErrors.password?.message as string}
                    />
                    <InputField
                        text={t("register.confirmPassword")}
                        type="password"
                        register={register("confirmPassword")}
                        error={formErrors.confirmPassword?.message as string}
                    />
                    <SubmitButton
                        text={t("register.register")}
                        disabled={loading}
                    />
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
