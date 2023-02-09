import { useCallback, useEffect, useState } from "react";

import { HttpStatusCode } from "axios";
import { useForm } from "react-hook-form";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import { z } from "zod";

import { zodResolver } from "@hookform/resolvers/zod";
import { CircularProgress } from "@mui/material";

import TextArea from "../../components/TextArea/TextArea";
import { useCreateArgument } from "../../hooks/arguments/useCreateArgument";
import { useCreateArgumentWithImage } from "../../hooks/arguments/useCreateArgumentWithImage";
import { useGetArgumentByUrl } from "../../hooks/arguments/useGetArgumentByUrl";
import { useSharedAuth } from "../../hooks/useAuth";
import { PaginatedList } from "../../types/PaginatedList";
import ArgumentDto from "../../types/dto/ArgumentDto";
import DebateDto from "../../types/dto/DebateDto";

type FieldValues = {
    content: string;
    image?: FileList;
};

interface Props {
    debate: DebateDto;
    argumentList: PaginatedList<ArgumentDto>;
    setArgumentList: (list: PaginatedList<ArgumentDto>) => void;
    refreshArgs: () => void;
}

function PostArgument({
    debate,
    argumentList,
    setArgumentList,
    refreshArgs,
}: Props) {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const [lastArgument, setLastArgument] = useState<ArgumentDto | undefined>(
        undefined
    );
    const [requestErr, setRequestErr] = useState<string | undefined>(undefined);

    // const MAX_FILE_SIZE = 10 * 1024 * 1024;

    const { userInfo } = useSharedAuth();

    let message = t("debate.argument.argumentMessage");
    let submitMessage = t("debate.argument.postArgument");

    if (
        lastArgument === null ||
        (lastArgument?.status === "introduction" &&
            lastArgument?.creatorName === debate.creatorName)
    ) {
        message = t("debate.argument.introMessage");
        submitMessage = t("debate.argument.postIntro");
    } else if (lastArgument?.status === "closing") {
        message = t("debate.argument.conclusionMessage");
        submitMessage = t("debate.argument.postConclusion");
    }

    const schema = z.object({
        content: z.string().min(1).max(2000),
        // image: z
        //     .any()
        //     .optional()
        //     .refine(
        //         (file) => file[0]?.size <= MAX_FILE_SIZE,
        //         t("profile.imageTooBig").toString()
        //     ),
    });

    const { loading: isCreateArgLoading, createArgument: createArgument } =
        useCreateArgument();

    const {
        loading: isCreateArgWithImageLoading,
        createArgumentWithImage: createArgumentWithImage,
    } = useCreateArgumentWithImage();

    const { loading: isLastArgLoading, getArgumentByUrl: getLastArgument } =
        useGetArgumentByUrl();

    const {
        register,
        handleSubmit,
        formState: { errors },
        setError,
    } = useForm<FieldValues>({
        resolver: zodResolver(schema),
    });

    const handlePostArgument = useCallback(async (data: FieldValues) => {
        if (data.image) {
            const response = await createArgumentWithImage({
                argumentsURL: debate.arguments,
                content: data.content,
                image: data.image[0],
            });
            switch (response.status) {
                case HttpStatusCode.Created:
                    refreshArgs();
                    break;
                case HttpStatusCode.NotFound:
                    M.toast({
                        html: "Not Found",
                        classes: "red",
                    });
                    break;
                case HttpStatusCode.Conflict:
                    setRequestErr(t("error.conflict.debateClosed").toString());
                    break;
            }
        } else {
            const response = await createArgument({
                argumentsURL: debate.arguments,
                content: data.content,
            });
            switch (response.status) {
                case HttpStatusCode.Created:
                    refreshArgs();
                    break;
                case HttpStatusCode.NotFound:
                    M.toast({
                        html: "Not Found",
                        classes: "red",
                    });
                    break;
                case HttpStatusCode.Conflict:
                    setRequestErr(t("error.conflict.debateClosed").toString());
                    break;
            }
        }
    }, []);

    useEffect(() => {
        if (argumentList.lastArgument) {
            getLastArgument({ url: argumentList.lastArgument }).then((res) => {
                switch (res.status) {
                    case HttpStatusCode.Ok:
                        setLastArgument(res.data);
                        break;
                    case HttpStatusCode.NotFound:
                        setLastArgument(undefined);
                        break;
                }
            });
        }
    }, [argumentList]);

    if (isLastArgLoading) return <CircularProgress size={100} />;

    return (
        <>
            {debate.status !== t("debate.statuses.statusClosed") &&
                debate.status !== t("debate.statuses.statusVoting") && (
                    <div className="card no-top-margin">
                        <div className="card-content">
                            {userInfo &&
                                ((argumentList.data.length === 0 &&
                                    userInfo.username === debate.creatorName) ||
                                (lastArgument?.creatorName !== undefined &&
                                    lastArgument?.creatorName !==
                                        userInfo.username) ? (
                                    <form
                                        encType="multipart/form-data"
                                        onSubmit={handleSubmit((data) => {
                                            handlePostArgument(
                                                data as FieldValues
                                            );
                                        })}
                                        method="post"
                                        acceptCharset="utf-8"
                                        id="postArgumentForm"
                                    >
                                        <div className="card-content">
                                            <span className="card-title">
                                                {message}
                                            </span>
                                            <TextArea
                                                text={t(
                                                    "debate.argument.content"
                                                )}
                                                register={register("content")}
                                                error={errors.content?.message}
                                            />
                                            <div className="image-selector">
                                                <div className="file-field input-field">
                                                    <div className="btn">
                                                        <label className="white-text">
                                                            {t(
                                                                "debate.argument.image"
                                                            )}
                                                        </label>
                                                        <input
                                                            type="file"
                                                            accept="image/*"
                                                            {...register(
                                                                "image"
                                                            )}
                                                        />
                                                    </div>
                                                    <div className="file-path-wrapper">
                                                        <input
                                                            className="file-path validate"
                                                            type="text"
                                                        />
                                                    </div>
                                                    {errors.image && (
                                                        <span className="helper-text error">
                                                            {
                                                                errors.image
                                                                    ?.message
                                                            }
                                                        </span>
                                                    )}
                                                    {requestErr && (
                                                        <span className="helper-text error">
                                                            {requestErr}
                                                        </span>
                                                    )}
                                                </div>
                                                <a
                                                    id="x"
                                                    className="material-icons"
                                                >
                                                    close
                                                </a>
                                            </div>
                                            <button
                                                className="btn waves-effect center-block submitBtn"
                                                type="submit"
                                                name="argument"
                                                form="postArgumentForm"
                                                disabled={
                                                    isCreateArgLoading ||
                                                    isCreateArgWithImageLoading
                                                }
                                            >
                                                {submitMessage}
                                                <i className="material-icons right">
                                                    send
                                                </i>
                                            </button>
                                        </div>
                                    </form>
                                ) : (
                                    <div className="card-title card-title-margins">
                                        {t("debate.waitTurn")}
                                    </div>
                                ))}
                            {!userInfo && (
                                <div className="card-title card-title-margins">
                                    {t("debate.needToLogin")}
                                    <i
                                        className="link"
                                        onClick={() =>
                                            navigate("/login", {
                                                state: {
                                                    from: window.location.pathname.substring(
                                                        13
                                                    ),
                                                },
                                            })
                                        }
                                    >
                                        {t("debate.firstLogin")}
                                    </i>
                                </div>
                            )}
                        </div>
                    </div>
                )}
        </>
    );
}

export default PostArgument;
