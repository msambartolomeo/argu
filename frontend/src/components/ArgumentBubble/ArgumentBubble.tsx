import { useState } from "react";

import cn from "classnames";
import { useTranslation } from "react-i18next";

import "./ArgumentBubble.css";

import "../../locales/index";
import ArgumentDto from "../../types/dto/ArgumentDto";
import DeleteDialog from "../DeleteDialog/DeleteDialog";
import NonClickableChip from "../NonClickableChip/NonClickableChip";

interface LikeButtonProps {
    icon: string;
    handleSubmit?: () => void;
}

const LikeButton = ({ icon, handleSubmit }: LikeButtonProps) => {
    return (
        <button type="submit" className="btn-flat" onSubmit={handleSubmit}>
            <i className="material-icons">{icon}</i>
        </button>
    );
};

interface ArgumentBubbleProps {
    argument: ArgumentDto;
    debateCreator: string;
}

const ArgumentBubble = ({ argument, debateCreator }: ArgumentBubbleProps) => {
    const [authorized, setAuthorized] = useState(false);
    const { t } = useTranslation();

    const handleLikeSubmit = () => {
        // TODO: Implement like
    };

    const handleUnlikeSubmit = () => {
        // TODO: Implement unlike
    };

    const handleGoToLogin = () => {
        // TODO: Redirect to login
    };

    const handleDelete = () => {
        // TODO: Implement delete argument
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
                    <div className="comment-header-section">
                        {authorized ? (
                            argument.likedByUser ? (
                                <form
                                    method="delete"
                                    acceptCharset="utf-8"
                                    onSubmit={handleUnlikeSubmit}
                                >
                                    <LikeButton icon="favorite" />
                                </form>
                            ) : (
                                <form
                                    method="post"
                                    acceptCharset="utf-8"
                                    onSubmit={handleLikeSubmit}
                                >
                                    <LikeButton icon="favorite_border" />
                                </form>
                            )
                        ) : (
                            <LikeButton
                                icon="favorite_border"
                                handleSubmit={handleGoToLogin}
                            />
                        )}
                        <div>
                            <p>{argument.likes}</p>
                        </div>
                    </div>
                    <div className="comment-header-section">
                        <NonClickableChip name={argument.createdDate} />
                        <NonClickableChip name={argument.status} />
                    </div>
                    {!argument.deleted && authorized && (
                        <DeleteDialog
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
