import { useRef } from "react";

import { Controller, FieldValues, useForm } from "react-hook-form";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import { z } from "zod";

import { zodResolver } from "@hookform/resolvers/zod";

import "./CreateDebate.css";

import InputField from "../../components/InputField/InputField";
import SelectDropdown from "../../components/SelectDropdown/SelectDropdown";
import SubmitButton from "../../components/SubmitButton/SubmitButton";
import TextArea from "../../components/TextArea/TextArea";
import { useSharedAuth } from "../../hooks/useAuth";
import "../../locales/index";
import DebateCategory from "../../types/enums/DebateCategory";

const CreateDebate = () => {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const { userInfo } = useSharedAuth();

    const schema = z.object({
        title: z
            .string()
            .min(1, t("createDebate.errors.titleEmpty") as string)
            .max(100, t("createDebate.errors.titleTooLong") as string),
        description: z
            .string()
            .min(1, t("createDebate.errors.descriptionEmpty") as string)
            .max(280, t("createDebate.errors.descriptionTooLong") as string),
        category: z.nativeEnum(DebateCategory),
        isCreatorFor: z
            .string()
            .refine((value) => value === "true" || value === "false"),
        opponent: z
            .string()
            .min(1, t("createDebate.errors.opponentUsernameEmpty") as string)
            .max(64, t("createDebate.errors.opponentUsernameTooLong") as string)
            .refine((value) => value !== userInfo?.username, {
                message: t(
                    "createDebate.errors.opponentUsernameSame"
                ) as string,
            }),
        image: z
            .any()
            .optional()
            .refine(
                (value: FileList) => {
                    if (!value || value.length === 0) {
                        return true;
                    }
                    if (value.length > 1) {
                        return false;
                    }
                    const image = value[0];
                    const allowedExtensions = ["jpg", "jpeg", "png"];
                    const extension = image.name.split(".").pop();
                    if (extension) {
                        return allowedExtensions.includes(extension);
                    }
                    return false;
                },
                { message: t("createDebate.errors.imageInvalid") as string }
            )
            .refine(
                (value: FieldValues) => {
                    if (!value || value.length === 0) {
                        return true;
                    }
                    if (value.length > 1) {
                        return false;
                    }
                    return value[0].size <= 1024 * 1024 * 10;
                },
                {
                    message: t("createDebate.errors.imageTooLarge") as string,
                }
            ),
    });

    const {
        register,
        handleSubmit,
        formState: { errors },
        control,
        setError,
    } = useForm({
        resolver: zodResolver(schema),
        defaultValues: {
            title: "",
            description: "",
            category: null,
            isCreatorFor: null,
            opponent: "",
            image: null,
        },
    });

    const categories = Object.values(DebateCategory).map((category) => ({
        value: category,
        label: t(`categories.${category}`) as string,
    }));

    const imageRef = useRef<HTMLInputElement>(null);
    const imageNameRef = useRef<HTMLInputElement>(null);

    const clearImage = () => {
        if (imageRef.current && imageNameRef.current) {
            imageRef.current.value = "";
            imageNameRef.current.value = "";
        }
    };

    const handleCreateSubmit = async (data: FieldValues) => {
        console.log(data);
    };

    return (
        <div className="card debate-form-container">
            <form
                acceptCharset="utf-8"
                encType="multipart/form-data"
                onSubmit={
                    (console.log(errors),
                    handleSubmit((data) => {
                        handleCreateSubmit(data);
                    }))
                }
            >
                <div className="card-content">
                    <span className="card-title center">
                        {t("createDebate.createDebate")}
                    </span>
                    <TextArea
                        text={t("createDebate.title")}
                        register={register("title")}
                        error={errors.title?.message as string}
                    />
                    <TextArea
                        text={t("createDebate.description")}
                        register={register("description")}
                        error={errors.description?.message as string}
                    />
                    <table className="no-borders radio-button-class">
                        <tbody>
                            <tr>
                                <td>
                                    <label>{t("createDebate.position")}</label>
                                </td>
                            </tr>
                            <tr>
                                <td className="radio-button-label">
                                    <label>
                                        <input
                                            type="radio"
                                            value="true"
                                            id="for"
                                            {...register("isCreatorFor")}
                                        />
                                        <span>{t("createDebate.for")}</span>
                                    </label>
                                </td>
                            </tr>
                            <tr>
                                <td className="radio-button-label">
                                    <label>
                                        <input
                                            type="radio"
                                            value="false"
                                            id="against"
                                            {...register("isCreatorFor")}
                                        />
                                        <span>{t("createDebate.against")}</span>
                                    </label>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <InputField text={t("createDebate.opponentUsername")} />
                    <table className="no-borders">
                        <tbody>
                            <tr>
                                <td>{t("createDebate.category")}</td>
                                <td>
                                    <Controller
                                        name="category"
                                        control={control}
                                        render={({ field }) => (
                                            <SelectDropdown
                                                {...field}
                                                suppliers={categories}
                                                placeholder={
                                                    t(
                                                        "createDebate.categoryPlaceholder"
                                                    ) as string
                                                }
                                            />
                                        )}
                                    />
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <table className="no-borders">
                        <tbody>
                            <tr>
                                <td>
                                    {t("createDebate.image")}
                                    <a
                                        className="material-icons"
                                        onClick={clearImage}
                                    >
                                        close
                                    </a>
                                </td>
                                <td>
                                    <div className="file-field input-field">
                                        <div className="btn">
                                            <label className="white-text">
                                                {t("createDebate.uploadImage")}
                                            </label>
                                            <input
                                                {...register("image")}
                                                id="image"
                                                type="file"
                                            />
                                        </div>
                                        <div className="file-path-wrapper">
                                            <input
                                                className="file-path"
                                                ref={imageNameRef}
                                                type="text"
                                            />
                                        </div>
                                    </div>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <SubmitButton text={t("createDebate.createDebate")} />
                </div>
            </form>
        </div>
    );
};

export default CreateDebate;
