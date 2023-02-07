import { FieldValues, useForm } from "react-hook-form";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import { z } from "zod";

import { zodResolver } from "@hookform/resolvers/zod";

import "./RequestModerator.css";

import SubmitButton from "../../components/SubmitButton/SubmitButton";
import TextArea from "../../components/TextArea/TextArea";
import { useSharedAuth } from "../../hooks/useAuth";
import { useRequestModerator } from "../../hooks/users/useRequestModerator";
import "../../locales/index";

const RequestModerator = () => {
    const { t } = useTranslation();
    const { loading, requestModerator } = useRequestModerator();
    const { userInfo } = useSharedAuth();
    const navigate = useNavigate();

    const schema = z.object({
        reason: z
            .string()
            .min(1, t("requestModerator.errors.reasonEmpty") as string)
            .max(2000, t("requestModerator.errors.reasonTooLong") as string),
    });

    const {
        register,
        handleSubmit,
        formState: { errors },
    } = useForm({
        resolver: zodResolver(schema),
    });

    const handleRequestModerator = async (data: FieldValues) => {
        await requestModerator({
            userUrl: userInfo?.username as string,
            reason: data.reason,
        });
    };

    return (
        <div className="card moderator-container">
            <form
                acceptCharset="utf-8"
                onSubmit={handleSubmit((data) => {
                    handleRequestModerator(data);
                    navigate({
                        pathname: "/",
                    });
                })}
            >
                <div className="card-content">
                    <span className="card-title">
                        {t("requestModerator.requestModerator")}
                    </span>
                    <p>{t("requestModerator.message")}</p>
                    <TextArea
                        text={t("requestModerator.label")}
                        register={register("reason")}
                        error={errors.reason?.message as string}
                    />
                    <SubmitButton
                        text={t("requestModerator.submit")}
                        disabled={loading}
                    />
                </div>
            </form>
        </div>
    );
};

export default RequestModerator;
