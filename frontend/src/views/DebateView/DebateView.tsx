import "./DebateView.css";
import User from "../../types/User";
import Debate from "../../types/Debate";

import { useState } from "react";
import DeleteDialog from "../../components/DeleteDialog/DeleteDialog";
import Chip from "../../components/Chip/Chip";
import NonClickableChip from "../../components/NonClickableChip/NonClickableChip";
import DebatePhoto from "../../components/DebatePhoto/DebatePhoto";
import Argument from "../../model/Argument";
import ArgumentBubble from "../../components/ArgumentBubble/ArgumentBubble";
import Pagination from "../../components/Pagination/Pagination";
import TextArea from "../../components/TextArea/TextArea";

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

const argument1: Argument = {
    content: "Argument 1",
    createdDate: "2021-01-01",
    status: "introduction",
    likes: 15,
    likedByUser: false,
    deleted: false,
    creator: user1,
    debate: debate1,
};

interface DebaterDisplayProps {
    debater?: string;
    position: string;
}

const DebaterDisplay = ({
    debater = "[deleted]",
    position,
}: DebaterDisplayProps) => {
    const handleGoToProfile = () => {
        // TODO: Implement go to profile call
    };

    return (
        <div className="username-container">
            <h6>
                <b>{position}</b>
            </h6>
            {debater !== "[deleted]" ? (
                <a className="link" onClick={handleGoToProfile}>
                    {debater}
                </a>
            ) : (
                <i>{debater}</i>
            )}
        </div>
    );
};

interface PostArgumentCardProps {
    handleSubmit: () => void;
    lastArgument: Argument;
    debateCreator: string | undefined;
}

// POST ARGUMENT CARD
const PostArgumentCard = ({
    handleSubmit,
    lastArgument,
    debateCreator,
}: PostArgumentCardProps) => {
    // TODO: Error handling

    let message = "Leave an argument here.";
    let submitMessage = "Post Argument!";
    if (
        lastArgument === null ||
        (lastArgument.status === "introduction" &&
            lastArgument.creator.username === debateCreator)
    ) {
        message = "Please introduce your stance in this debate.";
        submitMessage = "Post Introduction!";
    } else if (lastArgument.status === "closing") {
        message = "Please conclude your argument in this debate.";
        submitMessage = "Post Conclusion!";
    }
    return (
        <form
            encType="multipart/form-data"
            onSubmit={handleSubmit}
            method="post"
            acceptCharset="utf-8"
            id="postArgumentForm"
        >
            <div className="card-content">
                <span className="card-title">{message}</span>
                <TextArea text="Add your argument here:" />
                <div className="image-selector">
                    <div className="file-field input-field">
                        <div className="btn">
                            <label className="white-text">Upload Image</label>
                            <input type="file" />
                        </div>
                        <div className="file-path-wrapper">
                            <input className="file-path validate" type="text" />
                        </div>
                    </div>
                    <a id="x" className="material-icons">
                        close
                    </a>
                </div>
                <button
                    className="btn waves-effect center-block submitBtn"
                    type="submit"
                    name="argument"
                    form="postArgumentForm"
                >
                    {submitMessage}
                    <i className="material-icons right">send</i>
                </button>
            </div>
        </form>
    );
};

// DEBATE VIEW
interface DebateViewProps {
    debate?: Debate;
}

const DebateView = ({ debate = debate1 }: DebateViewProps) => {
    // TODO: Change to real values
    const [authorized, setAuthorized] = useState(true);
    const [canCloseDebate, setCanCloseDebate] = useState(false);
    const [canDeleteDebate, setCanDeleteDebate] = useState(false);
    const [subscribed, setSubscribed] = useState(false);
    const argumentsList: Argument[] = [argument1];

    const handleCloseDebate = () => {
        // TODO: Implement close debate call
    };

    const handleDeleteDebate = () => {
        // TODO: Implement delete debate call
    };

    const handleSubscribe = () => {
        // TODO: Implement subscribe call
        // consider subscribed state for the call
    };

    const handleGoToLogin = () => {
        // TODO: Implement go to login call
    };

    const handlePostArgument = () => {
        // TODO: Implement post argument call
    };

    // TODO: use router for redirecting category
    return (
        <>
            <div className="card normalized-margins">
                <div className="card-content debate-info-holder">
                    <div className="debate-holder-separator">
                        <div className="debate-text-holder">
                            <div className="debate-info-holder">
                                <h4 className="debate-title word-wrap">
                                    {debate.name}
                                </h4>
                                {authorized && (
                                    <div className="right debate-buttons-display">
                                        <div className="col">
                                            {canCloseDebate && (
                                                <button
                                                    className="btn waves-effect chip"
                                                    onClick={handleCloseDebate}
                                                >
                                                    Close Debate
                                                    <i className="material-icons right">
                                                        close
                                                    </i>
                                                </button>
                                            )}
                                            {canDeleteDebate && (
                                                <DeleteDialog
                                                    id="delete-debate"
                                                    handleDelete={
                                                        handleDeleteDebate
                                                    }
                                                    title="Are you sure you want to delete this debate?"
                                                />
                                            )}
                                        </div>
                                    </div>
                                )}
                            </div>
                            <hr className="dashed" />
                            <h5 className="debate-description word-wrap">
                                {debate.description}
                            </h5>
                            {debate.isCreatorFor ? (
                                <>
                                    <DebaterDisplay
                                        debater={debate.creator.username}
                                        position="For: "
                                    />
                                    <DebaterDisplay
                                        debater="oponent"
                                        position="Against: "
                                    />
                                </>
                            ) : (
                                <>
                                    <DebaterDisplay
                                        debater="oponent"
                                        position="For: "
                                    />
                                    <DebaterDisplay
                                        debater={debate.creator.username}
                                        position="Against: "
                                    />
                                </>
                            )}
                        </div>
                        <div className="debate-footer">
                            {authorized && (
                                <>
                                    {subscribed ? (
                                        <button
                                            className="btn waves-effect chip"
                                            onClick={handleSubscribe}
                                        >
                                            Unsubscribe
                                            <i className="material-icons right">
                                                notifications_off
                                            </i>
                                        </button>
                                    ) : (
                                        <button
                                            className="btn waves-effect chip"
                                            onClick={handleSubscribe}
                                        >
                                            Subscribe
                                            <i className="material-icons right">
                                                notifications_active
                                            </i>
                                        </button>
                                    )}
                                </>
                            )}
                            <Chip name={debate.category} />
                            <Chip name={debate.createdDate} />
                            <Chip name={debate.status} />
                            <NonClickableChip
                                name={"Subscribed: " + debate.subscriptions}
                            />
                        </div>
                    </div>
                    <DebatePhoto id={undefined} alt="debate photo" />
                </div>
            </div>
            <div className="debate-content">
                <div className="z-depth-3 argument-list">
                    {argumentsList.map((argument) => {
                        let header;
                        switch (argument.status) {
                            case "introduction":
                                header = "Introduction";
                                break;
                            case "closing":
                                header = "Conclusion";
                                break;
                            case "argument":
                                header = "Argument";
                                break;
                        }
                        return (
                            <>
                                <h5 className="center">{header}</h5>
                                <ArgumentBubble argument={argument} />
                            </>
                        );
                    })}
                    {argumentsList.length === 0 && (
                        <h5 className="center">No arguments yet</h5>
                    )}
                    <Pagination param="" totalPages={1} />
                </div>
                <div className="post-arguments">
                    <div className="card no-top-margin">
                        <div className="card-content">
                            {authorized &&
                                debate.status !== "closed" &&
                                debate.status !== "voting" &&
                                (argumentsList[argumentsList.length - 1].creator
                                    .username === user1.username ? (
                                    <PostArgumentCard
                                        handleSubmit={handlePostArgument}
                                        lastArgument={
                                            argumentsList[
                                                argumentsList.length - 1
                                            ]
                                        }
                                        debateCreator={debate.creator.username}
                                    />
                                ) : (
                                    <div className="card-title card-title-margins">
                                        You need to wait for your turn to post
                                        your argument.
                                    </div>
                                ))}
                            {!authorized && (
                                <div className="card-title card-title-margins">
                                    Hey! Would you like to participate in the
                                    discussion, vote for the winner, argument,
                                    like other people&apos;s arguments, and
                                    subscribe to debates?{" "}
                                    <a onClick={handleGoToLogin}>
                                        You need to be logged-in in order to do
                                        that!
                                    </a>
                                </div>
                            )}
                        </div>
                    </div>
                    <div>VOTE SECTION</div>
                </div>
            </div>
        </>
    );
};

export default DebateView;
