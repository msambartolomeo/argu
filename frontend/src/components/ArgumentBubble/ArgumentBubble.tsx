import "./ArgumentBubble.css";

import cn from "classnames";
import { useState } from "react";

import User from "../../types/User";
import Argument from "../../types/Argument";

import Debate from "../../types/Debate";
import NonClickableChip from "../NonClickableChip/NonClickableChip";
import DeleteDialog from "../DeleteDialog/DeleteDialog";

const user1: User = {
    username: "User 1",
    email: "user1@mail.com",
    createdDate: "2021-01-01",
};

const debate1: Debate = {
    id: 1,
    name: "Debate 1",
    description: "Description 1",
    isCreatorFor: true,
    category: "Category 1",
    createdDate: "2021-01-01",
    status: "Status 1",
    subscriptions: 1,
    votesFor: 0,
    votesAgainst: 0,
    creator: user1,
};

const arg: Argument = {
    content: "Argument 1",
    createdDate: "2021-01-01",
    status: "Status 1",
    likes: 1,
    likedByUser: true,
    deleted: false,
    creator: user1,
    debate: debate1,
};

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
    argument?: Argument;
}

// TODO: Apply i18n
const ArgumentBubble = ({ argument = arg }: ArgumentBubbleProps) => {
    const [authorized, setAuthorized] = useState(false);

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
                "sb-left":
                    argument.creator.username ===
                    argument.debate.creator.username,
                "sb-right":
                    argument.creator.username !==
                    argument.debate.creator.username,
            })}
        >
            <div className="comment-info">
                <h6 className="comment-owner">
                    {argument.creator.username} said
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
                            title="Are you sure you want to delete this argument?"
                        />
                    )}
                </div>
            </div>
            <div>
                {argument.deleted ? (
                    <p className="comment-owner">Argument deleted</p>
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
