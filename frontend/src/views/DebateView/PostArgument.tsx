import { useCallback, useEffect, useState } from "react";

import { HttpStatusCode } from "axios";
import { useForm } from "react-hook-form";
import { useTranslation } from "react-i18next";
import { useNavigate, useSearchParams } from "react-router-dom";
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
import { PAGE_DEFAULT } from "../../types/globalConstants";

type FieldValues = {
    content: string;
    image?: FileList;
};

interface Props {
    debate: DebateDto;
    argumentList: PaginatedList<ArgumentDto>;
    refreshArgs: () => void;
}

function PostArgument({ debate, argumentList, refreshArgs }: Props) {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const [lastArgument, setLastArgument] = useState<ArgumentDto | undefined>(
        undefined
    );
    const [requestErr, setRequestErr] = useState<string | undefined>(undefined);

    const MAX_FILE_SIZE = 10 * 1024 * 1024;

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
        image: z
            .any()
            .optional()
            .refine((file) => {
                if (file === undefined || file.length === 0) return true;
                if (file.length > 1) {
                    return false;
                }
                return (
                    file[0]?.size <= MAX_FILE_SIZE,
                    t("profile.imageTooBig").toString()
                );
            }),
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
    } = useForm<FieldValues>({
        resolver: zodResolver(schema),
    });

    const handlePostArgument = useCallback(async (data: FieldValues) => {
        if (data.image !== undefined && data.image[0] !== undefined) {
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
                    setRequestErr(t("errors.conflict.debateClosed").toString());
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
                    setRequestErr(t("errors.conflict.debateClosed").toString());
                    break;
            }
        }
    }, []);

    const handleResetImage = () => {
        const fileInput = document.getElementById(
            "imageFileText"
        ) as HTMLInputElement;
        fileInput.value = "";
        fileInput.classList.remove("valid");

        const imageFile = document.getElementById(
            "imageFile"
        ) as HTMLInputElement;
        imageFile.value = "";
    };

    const [queryParams] = useSearchParams();

    useEffect(() => {
        if (
            argumentList.lastElement &&
            Number(queryParams.get("page") || PAGE_DEFAULT) !==
                argumentList.totalPages
        ) {
            getLastArgument({ url: argumentList.lastElement }).then((res) => {
                switch (res.status) {
                    case HttpStatusCode.Ok:
                        setLastArgument(res.data);
                        break;
                    case HttpStatusCode.NotFound:
                        setLastArgument(undefined);
                        break;
                }
            });
        } else {
            setLastArgument(argumentList.data[argumentList.data.length - 1]);
        }
    }, [argumentList]);

    if (isLastArgLoading && !lastArgument)
        return <CircularProgress size={100} />;

    return (
        <>
            {debate.status !== t("debate.statuses.statusClosed") &&
                debate.status !== t("debate.statuses.statusVoting") && (
                    <div className="card no-top-margin">
                        <div className="card-content">
                            {userInfo &&
                            (userInfo.username === debate.creatorName ||
                                userInfo.username === debate.opponentName) ? (
                                (argumentList.data.length === 0 &&
                                    userInfo.username === debate.creatorName) ||
                                lastArgument?.creatorName !==
                                    userInfo.username ? (
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
                                                            id="imageFile"
                                                            {...register(
                                                                "image"
                                                            )}
                                                        />
                                                    </div>
                                                    <div className="file-path-wrapper">
                                                        <input
                                                            className="file-path validate"
                                                            type="text"
                                                            id="imageFileText"
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
                                                    onClick={handleResetImage}
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
                                )
                            ) : userInfo ? (
                                <div className="card-title card-title-margins">
                                    {t("debate.notParticipating")}
                                </div>
                            ) : (
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
