import { CircularProgress } from "@mui/material";
import { useTranslation } from "react-i18next";

import { useEffect, useState } from "react";

import { useParams } from "react-router-dom";

import { HttpStatusCode } from "axios";

import ArgumentBubble from "../../components/ArgumentBubble/ArgumentBubble";
import Chip from "../../components/Chip/Chip";
import DebateListItem from "../../components/DebateListItem/DebateListItem";
import DebatePhoto from "../../components/DebatePhoto/DebatePhoto";
import DeleteDialog from "../../components/DeleteDialog/DeleteDialog";
import NonClickableChip from "../../components/NonClickableChip/NonClickableChip";
import Pagination from "../../components/Pagination/Pagination";
import TextArea from "../../components/TextArea/TextArea";
import {
    GetDebateByIdOutput,
    useGetDebateById,
} from "../../hooks/debates/useGetDebateById";
import {
    GetDebatesByUrlOutput,
    useGetDebatesByUrl,
} from "../../hooks/debates/useGetDebatesByUrl";
import "../../locales/index";
import Argument from "../../types/Argument";
import Chat from "../../types/Chat";
import Debate from "../../types/Debate";
import { PaginatedList } from "../../types/PaginatedList";
import User from "../../types/User";
import DebateDto from "../../types/dto/DebateDto";
import UserRole from "../../types/enums/UserRole";
import { Error } from "../Error/Error";
import "./DebateView.css";

// TODO: Connect to API and remove
const user1: User = {
    username: "azul",
    createdDate: "2021-01-01",
    role: UserRole.MODERATOR,
};

const debate1: Debate = {
    id: 1,
    name: "Debate 1",
    description: "Description 1",
    isCreatorFor: true,
    category: "Category 1",
    createdDate: "2021-01-01",
    status: "open",
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
    const { t } = useTranslation();

    let message = t("debate.argument.argumentMessage");
    let submitMessage = t("debate.argument.postArgument");
    if (
        lastArgument === null ||
        (lastArgument.status === "introduction" &&
            lastArgument.creator.username === debateCreator)
    ) {
        message = t("debate.argument.introMessage");
        submitMessage = t("debate.argument.postIntro");
    } else if (lastArgument.status === "closing") {
        message = t("debate.argument.conclusionMessage");
        submitMessage = t("debate.argument.postConclusion");
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
                <TextArea text={t("debate.argument.content")} />
                <div className="image-selector">
                    <div className="file-field input-field">
                        <div className="btn">
                            <label className="white-text">
                                {t("debate.argument.image")}
                            </label>
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
    debate?: DebateDto | null;
    userData?: User;
}

const VoteSection = ({ debate, userData }: VotesSectionProps) => {
    // TODO: View user vote logic
    const { t } = useTranslation();
    const [vote, setVote] = useState("for");

    const handleUserVoted = (vote: string) => {
        if (vote === "for") {
            if (debate?.isCreatorFor) return debate.creatorName;
            return debate?.opponentName;
        }
        if (debate?.isCreatorFor) return debate.opponentName;
        return debate?.creatorName;
    };

    const handleVoteButton = () => {
        // TODO: Implement vote call
    };

    const handleVoteResult = () => {
        const result = (debate?.votesFor || 0) - (debate?.votesAgainst || 0);
        if (result === 0) return t("debate.votes.draw");

        let winner: string | undefined;
        if (result > 0) {
            winner = debate?.isCreatorFor
                ? debate.creatorName
                : debate?.opponentName;
        } else {
            winner = debate?.isCreatorFor
                ? debate.opponentName
                : debate?.creatorName;
        }
        winner = winner ? winner : t("debate.userDeleted").toString();
        return t("debate.votes.winner") + winner;
    };

    return (
        <div className="card vote-section no-top-margin">
            <h5>{t("debate.votes.votes")}</h5>
            {userData &&
                !vote &&
                (debate?.status === t("debate.statuses.statusOpen") ||
                    debate?.status === t("debate.statuses.statusVoting") ||
                    debate?.status === t("debate.statuses.statusClosing")) && (
                    <>
                        <h5>{t("debate.votes.whoWins")}</h5>
                        <div className="vote-buttons">
                            <button
                                onClick={handleVoteButton}
                                className="btn waves-effect"
                            >
                                {debate?.creatorName}
                            </button>
                            <button
                                onClick={handleVoteButton}
                                className="btn waves-effect"
                            >
                                {debate.opponentName}
                            </button>
                        </div>
                    </>
                )}
            {vote && (
                <h6>
                    {t("debate.votes.voted")} {handleUserVoted(vote)}
                </h6>
            )}
            {((userData && vote) || !userData) && (
                <div className="progress red">
                    {(debate?.votesFor || 0) > 0 && (
                        <div
                            className="votes-format blue"
                            style={{ width: debate?.votesFor + "%" }}
                        >
                            <span>
                                {debate?.isCreatorFor
                                    ? debate.creatorName
                                    : debate?.opponentName}
                            </span>
                            <span>{debate?.votesFor}%</span>
                        </div>
                    )}
                    {(debate?.votesAgainst || 0) > 0 && (
                        <div
                            className="votes-format"
                            style={{ width: debate?.votesAgainst + "%" }}
                        >
                            <span>
                                {debate?.isCreatorFor
                                    ? debate.opponentName
                                    : debate?.creatorName}
                            </span>
                            <span>{debate?.votesAgainst}%</span>
                        </div>
                    )}
                </div>
            )}
            {debate?.status === t("debate.statuses.statusClosed") && (
                <h6>{handleVoteResult()}</h6>
            )}
            {userData &&
                vote &&
                (debate?.status === t("debate.statuses.statusOpen") ||
                    debate?.status === t("debate.statuses.statusVoting")) && (
                    <>
                        <h6>{t("debate.votes.changeVote")}</h6>
                        <button className="btn waves-effect">
                            {t("debate.votes.unvote")}
                        </button>
                    </>
                )}
            {(debate?.votesFor || 0) + (debate?.votesAgainst || 0) === 0 && (
                <h5 className="center">{t("debate.votes.noVotes")}</h5>
            )}
            {debate?.status === t("debate.statuses.statusVoting") && (
                <h6 className="center">
                    {t("debate.votes.votingEnds")} 17/02/2023
                </h6>
            )}
        </div>
    );
};

// CHAT SECTION
interface ChatSectionProps {
    debate?: DebateDto | null;
    userData: User | undefined;
}
const ChatSection = ({ debate, userData }: ChatSectionProps) => {
    const chat: Chat[] = chats1;
    const { t } = useTranslation();
    useEffect(() => {
        const chatElem = document.getElementById("chat");
        chatElem?.scrollTo(0, chatElem.scrollHeight);
    }, []);

    return (
        <>
            {((debate?.status !== t("debate.statuses.statusOpen") &&
                debate?.status !== t("debate.statuses.statusClosing")) ||
                !userData ||
                (userData.username !== debate.creatorName &&
                    userData.username !== debate.opponentName)) && (
                <div className="card">
                    <div className="chat-box card-content">
                        <h5>{t("debate.chat.title")}</h5>
                        <div className="message-container" id="chat">
                            {chat.length > 0 ? (
                                chat.map((c: Chat) => (
                                    <div key={c.selfURL} className="message">
                                        <p className="datetime">
                                            {c.createdDate}
                                        </p>
                                        <p>user</p>
                                        <p>{c.message}</p>
                                    </div>
                                ))
                            ) : (
                                <div className="message">
                                    <h6>{t("debate.chat.noMessages")}</h6>
                                </div>
                            )}
                        </div>
                        {userData &&
                            debate?.status !==
                                t("debate.statuses.statusClosed") &&
                            debate?.status !==
                                t("debate.statuses.statusDeleted") && (
                                <>
                                    <form
                                        method="post"
                                        acceptCharset="utf-8"
                                        id="chatForm"
                                    >
                                        <div className="send-chat">
                                            <div className="input-field flex-grow">
                                                <label>
                                                    {t("debate.chat.message")}
                                                </label>
                                                <input className="materialize-textarea" />
                                            </div>
                                            <button
                                                className="btn waves-effect center-block submitBtn"
                                                type="submit"
                                                name="chat"
                                                form="chatForm"
                                            >
                                                {t("debate.chat.send")}
                                            </button>
                                        </div>
                                    </form>
                                </>
                            )}
                        {userData && <Pagination param="todo" totalPages={1} />}
                    </div>
                </div>
            )}
        </>
    );
};

interface RecommendedDebatesSectionProps {
    recommendedDebates: DebateDto[];
}

// RECOMMENDED DEBATES SECTION
const RecommendedDebatesSection = ({
    recommendedDebates,
}: RecommendedDebatesSectionProps) => {
    const { t } = useTranslation();
    const [slideIndex, setSlideIndex] = useState(0);

    return (
        <>
            {recommendedDebates.length > 0 && (
                <div className="card vote-section">
                    <h5>{t("debate.recommendedDebates")}</h5>
                    <div className="row">
                        <div className="slideshow-container">
                            {recommendedDebates.map((d: DebateDto, index) => (
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

const DebateView = () => {
    // TODO: Change to real values and hooks
    const [debateData, setDebateData] = useState<DebateDto | undefined>();
    const [recommendedDebates, setRecommendedDebates] = useState<
        PaginatedList<DebateDto> | undefined
    >();
    const [userData, setuserData] = useState<User | undefined>(user1);
    const [subscribed, setSubscribed] = useState<boolean>(false);
    const argumentsList: Argument[] = [argument1];

    const { t } = useTranslation();
    const params = useParams();

    const { loading: isDebateLoading, getDebate: getDebate } =
        useGetDebateById();

    const {
        loading: isRecommendedDebatesLoading,
        getDebatesByUrl: getRecommendedDebates,
    } = useGetDebatesByUrl();

    useEffect(() => {
        const id: string = params.id?.toString() || "";
        getDebate({ id: parseInt(id) }).then((output: GetDebateByIdOutput) => {
            if (output.status === HttpStatusCode.Ok) {
                setDebateData(output.data);
            }
        });
    }, []);

    useEffect(() => {
        if (debateData?.recommendations) {
            getRecommendedDebates({
                url: debateData?.recommendations || "",
            }).then((output: GetDebatesByUrlOutput) => {
                if (output.status === HttpStatusCode.Ok) {
                    setRecommendedDebates(output.data);
                }
            });
        }
    }, [debateData]);

    if (typeof debateData === "string") {
        return <Error status={HttpStatusCode.NotFound} message={debateData} />;
    }

    if (isDebateLoading || isRecommendedDebatesLoading || !debateData) {
        return <CircularProgress size={100} />;
    }

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
                        <div className="debate-text-organizer">
                            <div className="debate-info-holder">
                                <h4 className="debate-title word-wrap">
                                    {debateData?.name}
                                </h4>
                                {userData && (
                                    <div className="right debate-buttons-display">
                                        <div className="col">
                                            {debateData?.status ===
                                                t(
                                                    "debate.statuses.statusOpen"
                                                ) &&
                                                (userData?.username ===
                                                    debateData?.creatorName ||
                                                    userData?.username ===
                                                        debateData?.opponentName) && (
                                                    <button
                                                        className="btn waves-effect chip"
                                                        onClick={
                                                            handleCloseDebate
                                                        }
                                                    >
                                                        {t("debate.close")}
                                                        <i className="material-icons right">
                                                            close
                                                        </i>
                                                    </button>
                                                )}
                                            {debateData?.status !==
                                                t(
                                                    "debate.statuses.statusDeleted"
                                                ) &&
                                                userData?.username ===
                                                    debateData?.creatorName && (
                                                    <DeleteDialog
                                                        id="delete-debate"
                                                        handleDelete={
                                                            handleDeleteDebate
                                                        }
                                                        title={t(
                                                            "debate.deleteConfirmation"
                                                        )}
                                                        name={t(
                                                            "debate.delete"
                                                        )}
                                                    />
                                                )}
                                        </div>
                                    </div>
                                )}
                            </div>
                            <hr className="dashed" />
                            <h5 className="debate-description word-wrap">
                                {debateData?.description}
                            </h5>
                            {debateData?.isCreatorFor ? (
                                <>
                                    <DebaterDisplay
                                        debater={debateData?.creatorName}
                                        position={t("debate.for")}
                                    />
                                    <DebaterDisplay
                                        debater={debateData?.opponentName}
                                        position={t("debate.against")}
                                    />
                                </>
                            ) : (
                                <>
                                    <DebaterDisplay
                                        debater={debateData?.opponentName}
                                        position={t("debate.for")}
                                    />
                                    <DebaterDisplay
                                        debater={debateData?.creatorName}
                                        position={t("debate.against")}
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
                                            {t("debate.unsubscribe")}
                                            <i className="material-icons right">
                                                notifications_off
                                            </i>
                                        </button>
                                    ) : (
                                        <button
                                            className="btn waves-effect chip"
                                            onClick={handleSubscribe}
                                        >
                                            {t("debate.subscribe")}
                                            <i className="material-icons right">
                                                notifications_active
                                            </i>
                                        </button>
                                    )}
                                </>
                            )}
                            <Chip name={debateData?.category} />
                            <Chip name={debateData?.createdDate.toString()} />
                            <Chip name={debateData?.status} />
                            <NonClickableChip
                                name={
                                    t("debate.subscribed") +
                                    debateData?.subscriptionsCount
                                }
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
                                header = t("debate.argument.introduction");
                                break;
                            case "closing":
                                header = t("debate.argument.conclusion");
                                break;
                            case "argument":
                                header = t("debate.argument.argument");
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
                        <h5 className="center">{t("debate.noArguments")}</h5>
                    )}
                    <Pagination param="" totalPages={1} />
                </div>
                <div className="post-arguments">
                    {debateData?.status !== t("debate.statuses.statusClosed") &&
                        debateData?.status !==
                            t("debate.statuses.statusVoting") && (
                            <div className="card no-top-margin">
                                <div className="card-content">
                                    {userData &&
                                        (argumentsList[argumentsList.length - 1]
                                            .creator.username ===
                                        user1.username ? (
                                            <PostArgumentCard
                                                handleSubmit={
                                                    handlePostArgument
                                                }
                                                lastArgument={
                                                    argumentsList[
                                                        argumentsList.length - 1
                                                    ]
                                                }
                                                debateCreator={
                                                    debateData?.creatorName
                                                }
                                            />
                                        ) : (
                                            <div className="card-title card-title-margins">
                                                {t("debate.waitTurn")}
                                            </div>
                                        ))}
                                    {!userData && (
                                        <div className="card-title card-title-margins">
                                            {t("debate.needToLogin")}
                                            <a onClick={handleGoToLogin}>
                                                {t("debate.firstLogin")}
                                            </a>
                                        </div>
                                    )}
                                </div>
                            </div>
                        )}
                    <VoteSection debate={debateData} userData={userData} />
                    <ChatSection debate={debateData} userData={userData} />
                    <RecommendedDebatesSection
                        recommendedDebates={recommendedDebates?.data || []}
                    />
                </div>
            </div>
        </>
    );
};

export default DebateView;
