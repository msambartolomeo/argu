import "./DebateView.css";
import User from "../../types/User";
import Debate from "../../types/Debate";

import { useState, useEffect } from "react";
import DeleteDialog from "../../components/DeleteDialog/DeleteDialog";
import Chip from "../../components/Chip/Chip";
import NonClickableChip from "../../components/NonClickableChip/NonClickableChip";
import DebatePhoto from "../../components/DebatePhoto/DebatePhoto";
import Argument from "../../model/Argument";
import ArgumentBubble from "../../components/ArgumentBubble/ArgumentBubble";
import Pagination from "../../components/Pagination/Pagination";
import TextArea from "../../components/TextArea/TextArea";
import Chat from "../../types/Chat";
import DebateListItem from "../../components/DebateListItem/DebateListItem";

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
    status: "voting",
    subscriptions: 1,
    votesFor: 80,
    votesAgainst: 20,
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

const chat1: Chat = {
    message: "Message 1",
    createdDate: "2021-01-01",
    selfURL: "",
    creatorURL: "",
    debateURL: "",
};

const chats1: Chat[] = [chat1, chat1, chat1, chat1, chat1, chat1, chat1, chat1];

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

// VOTES SECTION
interface VotesSectionProps {
    debate: Debate;
    userData?: User;
}

const VoteSection = ({ debate, userData }: VotesSectionProps) => {
    // TODO: View user vote logic
    const [vote, setVote] = useState("for");

    const handleUserVoted = (vote: string) => {
        if (vote === "for") {
            if (debate.isCreatorFor) return debate.creator.username;
            return debate.opponent?.username;
        }
        if (debate.isCreatorFor) return debate.opponent?.username;
        return debate.creator.username;
    };

    const handleVoteButton = () => {
        // TODO: Implement vote call
    };

    const handleVoteResult = () => {
        const result = debate.votesFor - debate.votesAgainst;
        if (result === 0) return "This debate resulted in a draw!";

        let winner: string | undefined;
        if (result > 0) {
            winner = debate.isCreatorFor
                ? debate.creator.username
                : debate.opponent?.username;
        } else {
            winner = debate.isCreatorFor
                ? debate.opponent?.username
                : debate.creator.username;
        }
        winner = winner ? winner : "[deleted]";
        return `The winner of this debate is ${winner}!`;
    };

    return (
        <div className="card vote-section no-top-margin">
            <h5>Votes</h5>
            {userData &&
                !vote &&
                (debate.status === "open" ||
                    debate.status === "voting" ||
                    debate.status === "closing") && (
                    <>
                        <h5>Who is the winner of the debate?</h5>
                        <div className="vote-buttons">
                            <button
                                onClick={handleVoteButton}
                                className="btn waves-effect"
                            >
                                {debate.creator.username}
                            </button>
                            <button
                                onClick={handleVoteButton}
                                className="btn waves-effect"
                            >
                                {debate.opponent?.username}
                            </button>
                        </div>
                    </>
                )}
            {vote && <h6>Voted: {handleUserVoted(vote)}</h6>}
            {((userData && vote) || !userData) && (
                <div className="progress red">
                    {debate.votesFor > 0 && (
                        <div
                            className="votes-format blue"
                            style={{ width: debate.votesFor + "%" }}
                        >
                            <span>
                                {debate.isCreatorFor
                                    ? debate.creator.username
                                    : debate.opponent?.username}
                            </span>
                            <span>{debate.votesFor}%</span>
                        </div>
                    )}
                    {debate.votesAgainst > 0 && (
                        <div
                            className="votes-format"
                            style={{ width: debate.votesAgainst + "%" }}
                        >
                            <span>
                                {debate.isCreatorFor
                                    ? debate.opponent?.username
                                    : debate.creator.username}
                            </span>
                            <span>{debate.votesAgainst}%</span>
                        </div>
                    )}
                </div>
            )}
            {debate.status === "closed" && <h6>{handleVoteResult()}</h6>}
            {userData &&
                vote &&
                (debate.status === "open" || debate.status === "voting") && (
                    <>
                        <h6>Changed your mind?</h6>
                        <button className="btn waves-effect">Unvote</button>
                    </>
                )}
            {debate.votesFor + debate.votesAgainst === 0 && (
                <h5 className="center">No votes yet</h5>
            )}
            {debate.status === "voting" && (
                <h6 className="center">Voting will end on 17/02/2023</h6>
            )}
        </div>
    );
};

// CHAT SECTION
interface ChatSectionProps {
    debate: Debate;
    userData: User | undefined;
}
const ChatSection = ({ debate, userData }: ChatSectionProps) => {
    const chat: Chat[] = chats1;
    useEffect(() => {
        const chatElem = document.getElementById("chat");
        chatElem?.scrollTo(0, chatElem.scrollHeight);
    }, []);

    return (
        <div className="card">
            {((debate.status !== "open" && debate.status !== "closing") ||
                !userData ||
                (userData.username !== debate.creator.username &&
                    userData.username !== debate.opponent?.username)) && (
                <div className="chat-box card-content">
                    <h5>Discussion</h5>
                    <div className="message-container" id="chat">
                        {chat.length > 0 ? (
                            chat.map((c: Chat) => (
                                <div key={c.selfURL} className="message">
                                    <p className="datetime">{c.createdDate}</p>
                                    <p>user</p>
                                    <p>{c.message}</p>
                                </div>
                            ))
                        ) : (
                            <div className="message">
                                <h6>There are no messages in this debate</h6>
                            </div>
                        )}
                    </div>
                    {userData &&
                        debate.status !== "closed" &&
                        debate.status !== "deleted" && (
                            <>
                                <form
                                    method="post"
                                    acceptCharset="utf-8"
                                    id="chatForm"
                                >
                                    <div className="send-chat">
                                        <div className="input-field flex-grow">
                                            <label>Message</label>
                                            <input className="materialize-textarea" />
                                        </div>
                                        <button
                                            className="btn waves-effect center-block submitBtn"
                                            type="submit"
                                            name="chat"
                                            form="chatForm"
                                        >
                                            Send
                                        </button>
                                    </div>
                                </form>
                            </>
                        )}
                    {userData && <Pagination param="todo" totalPages={1} />}
                </div>
            )}
        </div>
    );
};

// RECOMMENDED DEBATES SECTION
const RecommendedDebatesSection = () => {
    const recommendedDebates: Debate[] = [debate1, debate1, debate1];
    const [slideIndex, setSlideIndex] = useState(0);

    return (
        <>
            {recommendedDebates.length > 0 && (
                <div className="card vote-section">
                    <h5>Recommended debates</h5>
                    <div className="row">
                        <div className="slideshow-container">
                            {recommendedDebates.map((d: Debate, index) => (
                                <div
                                    key={d.id}
                                    className="fade"
                                    style={{
                                        display:
                                            slideIndex === index
                                                ? "block"
                                                : "none",
                                    }}
                                >
                                    <DebateListItem debate={d} />
                                </div>
                            ))}
                        </div>
                    </div>
                    <div className="image-selector">
                        {recommendedDebates.length > 1 && (
                            <a
                                className="prev btn image-selector"
                                onClick={() =>
                                    setSlideIndex(
                                        slideIndex === 0
                                            ? recommendedDebates.length - 1
                                            : slideIndex - 1
                                    )
                                }
                            >
                                ❮
                            </a>
                        )}
                        {recommendedDebates.length > 1 && (
                            <a
                                className="next btn image-selector"
                                onClick={() =>
                                    setSlideIndex(
                                        slideIndex ===
                                            recommendedDebates.length - 1
                                            ? 0
                                            : slideIndex + 1
                                    )
                                }
                            >
                                ❯
                            </a>
                        )}
                    </div>
                </div>
            )}
        </>
    );
};

// DEBATE VIEW
interface DebateViewProps {
    debate?: Debate;
}

const DebateView = ({ debate = debate1 }: DebateViewProps) => {
    // TODO: Change to real values and hooks
    const [userData, setuserData] = useState<User | undefined>(user1);
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
                                {userData && (
                                    <div className="right debate-buttons-display">
                                        <div className="col">
                                            {debate.status === "open" &&
                                                (userData?.username ===
                                                    debate.creator.username ||
                                                    userData?.username ===
                                                        debate.opponent
                                                            ?.username) && (
                                                    <button
                                                        className="btn waves-effect chip"
                                                        onClick={
                                                            handleCloseDebate
                                                        }
                                                    >
                                                        Close Debate
                                                        <i className="material-icons right">
                                                            close
                                                        </i>
                                                    </button>
                                                )}
                                            {debate.status !== "deleted" &&
                                                userData?.username ===
                                                    debate.creator.username && (
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
                            {userData && (
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
                            {userData &&
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
                            {!userData && (
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
                    <VoteSection debate={debate} userData={userData} />
                    <ChatSection debate={debate} userData={userData} />
                    <RecommendedDebatesSection />
                </div>
            </div>
        </>
    );
};

export default DebateView;
