import { useEffect, useState } from "react";

import { HttpStatusCode } from "axios";
import cn from "classnames";
import { useSnackbar } from "notistack";
import { useTranslation } from "react-i18next";

import "./ArgumentBubble.css";

import { useDeleteArgument } from "../../hooks/arguments/useDeleteArgument";
import { useCreateLike } from "../../hooks/likes/useCreateLike";
import { useDeleteLike } from "../../hooks/likes/useDeleteLike";
import { useSharedAuth } from "../../hooks/useAuth";
import "../../locales/index";
import ArgumentDto from "../../types/dto/ArgumentDto";
import DeleteDialog from "../DeleteDialog/DeleteDialog";
import NonClickableChip from "../NonClickableChip/NonClickableChip";

interface LikeButtonProps {
    icon: string;
    handleSubmit?: () => void;
    disabled?: boolean;
}

const LikeButton = ({ disabled, icon, handleSubmit }: LikeButtonProps) => {
    return (
        <button disabled={disabled} className="btn-flat" onClick={handleSubmit}>
            <i className="material-icons">{icon}</i>
        </button>
    );
};

interface ArgumentBubbleProps {
    argumentData: ArgumentDto;
    debateCreator: string;
}

const ArgumentBubble = ({
    argumentData,
    debateCreator,
}: ArgumentBubbleProps) => {
    const { userInfo } = useSharedAuth();

    const { t } = useTranslation();

    const [argument, setArgument] = useState<ArgumentDto>(argumentData);

    const { loading: likeLoading, createLike: callLike } = useCreateLike();
    const { loading: unlikeLoading, callDeleteLike: callUnlike } =
        useDeleteLike();
    const { loading: deleteLoading, deleteArgument } = useDeleteArgument();
    const { enqueueSnackbar } = useSnackbar();

    useEffect(() => {
        setArgument(argumentData);
    }, [argumentData]);

    const likeSubmit = () => {
        callLike({
            likeUrl: argument.like as string,
        }).then((code) => {
            switch (code) {
                case HttpStatusCode.Created:
                    argument.likes += 1;
                    argument.likedByUser = true;
                    setArgument(argument);
                    break;
                default:
                    enqueueSnackbar(t("errors.unexpected"), {
                        variant: "error",
                    });
            }
        });
    };

    const unlikeSubmit = () => {
        callUnlike({
            likeUrl: argument.like as string,
        }).then((code) => {
            switch (code) {
                case HttpStatusCode.NoContent:
                    argument.likedByUser = false;
                    argument.likes -= 1;
                    setArgument(argument);
                    break;
                default:
                    enqueueSnackbar(t("errors.unexpected"), {
                        variant: "error",
                    });
            }
        });
    };

    const handleDelete = () => {
        deleteArgument(argument.self).then((code) => {
            switch (code) {
                case HttpStatusCode.NoContent:
                    argument.deleted = true;
                    setArgument(argument);
                    enqueueSnackbar(
                        t("components.argumentBubble.deleteSuccess"),
                        {
                            variant: "success",
                        }
                    );
                    break;
                default:
                    enqueueSnackbar(t("errors.unexpected"), {
                        variant: "error",
                    });
            }
        });
    };

    return (
        <div
            className={cn("speech-bubble", {
                "sb-left": argument.creatorName === debateCreator,
                "sb-right": argument.creatorName !== debateCreator,
            })}
        >
            <div className="comment-info">
                <h6 className="comment-owner">
                    {t("components.argumentBubble.userSaid", {
                        username: argument.creatorName,
                    })}
                </h6>
                <div className="comment-extra">
                    <div className="comment-header-section nowrap">
                        {!argument.deleted &&
                            (argument.likedByUser ? (
                                <LikeButton
                                    disabled={unlikeLoading}
                                    icon="favorite"
                                    handleSubmit={unlikeSubmit}
                                />
                            ) : (
                                <LikeButton
                                    disabled={likeLoading}
                                    icon="favorite_border"
                                    handleSubmit={likeSubmit}
                                />
                            ))}
                        {!argument.deleted && (
                            <div>
                                <p>{argument.likes}</p>
                            </div>
                        )}
                    </div>
                    <div className="comment-header-section">
                        <NonClickableChip name={argument.createdDate} />
                        <NonClickableChip name={argument.status} />
                    </div>
                    {!argument.deleted &&
                        userInfo?.username === argument.creatorName && (
                            <DeleteDialog
                                disabled={deleteLoading}
                                id="delete-argument"
                                handleDelete={handleDelete}
                                title={t(
                                    "components.argumentBubble.deleteConfirmation"
                                )}
                            />
                        )}
                </div>
            </div>
            <div>
                {argument.deleted ? (
                    <p className="comment-owner">
                        {t("components.argumentBubble.deleted")}
                    </p>
                ) : (
                    <p>{argument.content}</p>
                )}
            </div>
            <div>
                {argument.image && (
                    <img
                        src={argument.image}
                        alt="Argument image"
                        className="responsive-img"
                    />
                )}
            </div>
        </div>
    );
};

export default ArgumentBubble;
