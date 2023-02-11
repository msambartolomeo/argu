import { useRef } from "react";

import { HttpStatusCode } from "axios";
import { FieldValues, useForm } from "react-hook-form";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import { z } from "zod";

import { zodResolver } from "@hookform/resolvers/zod";

import "./CreateDebate.css";

import InputField from "../../components/InputField/InputField";
import RadioComponent from "../../components/RadioComponent/RadioComponent";
import SelectComponent from "../../components/SelectComponent/SelectComponent";
import SubmitButton from "../../components/SubmitButton/SubmitButton";
import TextArea from "../../components/TextArea/TextArea";
import { useCreateDebate } from "../../hooks/debates/useCreateDebate";
import { useCreateDebateWithImage } from "../../hooks/debates/useCreateDebateWithImage";
import { useSharedAuth } from "../../hooks/useAuth";
import "../../locales/index";
import DebateCategory from "../../types/enums/DebateCategory";

const CreateDebate = () => {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const { userInfo } = useSharedAuth();
    const { loading, createDebate } = useCreateDebate();
    const { loading: loadingWithImage, createDebateWithImage } =
        useCreateDebateWithImage();

    const schema = z.object({
        title: z
            .string()
            .min(1, t("createDebate.errors.titleEmpty") as string)
            .max(100, t("createDebate.errors.titleTooLong") as string),
        description: z
            .string()
            .min(1, t("createDebate.errors.descriptionEmpty") as string)
            .max(280, t("createDebate.errors.descriptionTooLong") as string),
        category: z.any().refine(
            (value) => {
                if (!value) {
                    return false;
                }
                return Object.values(DebateCategory).includes(
                    value.value as DebateCategory
                );
            },
            {
                message: t("createDebate.errors.categoryEmpty") as string,
            }
        ),
        isCreatorFor: z
            .any()
            .refine(
                (value) => {
                    return value !== null;
                },
                {
                    message: t(
                        "createDebate.errors.isCreatorForEmpty"
                    ) as string,
                }
            )
            .refine(
                (value) => {
                    return value === "true" || value === "false";
                },
                {
                    message: t(
                        "createDebate.errors.isCreatorForInvalid"
                    ) as string,
                }
            ),
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
                (value: FieldValues) => {
                    if (!value || value.length === 0) {
                        return true;
                    }
                    if (value.length > 1) {
                        return false;
                    }
                    return value[0].size <= 10 * 1024 * 1024;
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

    const categoryOptions = Object.values(DebateCategory).map((category) => ({
        value: category,
        label: t(`categories.${category}`) as string,
    }));

    const isCreatorForOptions = [
        {
            value: "true",
            label: t("createDebate.for") as string,
        },
        {
            value: "false",
            label: t("createDebate.against") as string,
        },
    ];

    const imageRef = useRef<HTMLInputElement>(null);
    const imageNameRef = useRef<HTMLInputElement>(null);

    const clearImage = () => {
        if (imageRef.current && imageNameRef.current) {
            imageRef.current.value = "";
            imageNameRef.current.value = "";
        }
    };

    const handleCreateSubmit = async (data: FieldValues) => {
        const { title, description, category, isCreatorFor, opponent, image } =
            data;
        const imageList = image as FileList;
        const imageFile = imageList?.length > 0 ? imageList[0] : null;

        const debateData = {
            title: title as string,
            description: description as string,
            category: category.value as DebateCategory,
            isCreatorFor: isCreatorFor === "true",
            opponentUsername: opponent as string,
        };

        const response = imageFile
            ? await createDebateWithImage({
                  ...debateData,
                  image: imageFile,
              })
            : await createDebate(debateData);

        switch (response.status) {
            case HttpStatusCode.Created:
                navigate({
                    pathname: response.location as string,
                });
                break;
            case HttpStatusCode.BadRequest:
                setError("opponent", {
                    type: "manual",
                    message: t(
                        "createDebate.errors.opponentUsernameNotFound"
                    ) as string,
                });
                break;
            case HttpStatusCode.NotFound:
                // TODO: Add toast with something like "An error occurred, please try again later"
                break;
        }
    };

    return (
        <div className="card debate-form-container">
            <form
                acceptCharset="utf-8"
                encType="multipart/form-data"
                onSubmit={handleSubmit((data) => {
                    handleCreateSubmit(data);
                })}
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

                    <RadioComponent
                        name="isCreatorFor"
                        label={t("createDebate.position") as string}
                        control={control}
                        options={isCreatorForOptions}
                        error={errors.isCreatorFor?.message as string}
                    />

                    <InputField
                        text={t("createDebate.opponentUsername")}
                        register={register("opponent")}
                        error={errors.opponent?.message as string}
                    />
                    <table className="no-borders">
                        <tbody>
                            <tr>
                                <td>
                                    <SelectComponent
                                        name="category"
                                        label={
                                            t(
                                                "createDebate.categoryPlaceholder"
                                            ) as string
                                        }
                                        control={control}
                                        options={categoryOptions}
                                        error={
                                            errors.category?.message as string
                                        }
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
                                                accept="image/*"
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
                    <SubmitButton
                        text={t("createDebate.createDebate")}
                        disabled={loading || loadingWithImage}
                    />
                </div>
            </form>
        </div>
    );
};

export default CreateDebate;
