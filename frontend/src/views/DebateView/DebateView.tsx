import { useEffect, useState } from "react";

import { HttpStatusCode } from "axios";
import { useTranslation } from "react-i18next";
import { useParams } from "react-router-dom";

import { CircularProgress } from "@mui/material";

import ArgumentList from "./ArgumentList";
import ChatSection from "./ChatSection";
import DebateHeader from "./DebateHeader";
import "./DebateView.css";
import PostArgumentCard from "./PostArgumentCard";
import RecommendedDebatesSection from "./RecommendedDebatesSection";
import VoteSection from "./VoteSection";

import {
    GetDebateByIdOutput,
    useGetDebateById,
} from "../../hooks/debates/useGetDebateById";
import "../../locales/index";
import Argument from "../../types/Argument";
import Debate from "../../types/Debate";
import User from "../../types/User";
import DebateDto from "../../types/dto/DebateDto";
import UserRole from "../../types/enums/UserRole";
import { Error } from "../Error/Error";

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

const DebateView = () => {
    // TODO: Change to real values and hooks
    const [debateData, setDebateData] = useState<DebateDto | undefined>();

    const [refresh, setRefresh] = useState<boolean>(true);
    const [userData, setuserData] = useState<User | undefined>(user1);
    const argumentsList: Argument[] = [argument1];

    const { t } = useTranslation();
    const params = useParams();

    const { loading: isDebateLoading, getDebate: getDebate } =
        useGetDebateById();

    useEffect(() => {
        if (refresh) {
            const id: string = params.id?.toString() || "";
            getDebate({ id: parseInt(id) }).then(
                (output: GetDebateByIdOutput) => {
                    if (output.status === HttpStatusCode.Ok) {
                        setDebateData(output.data);
                    }
                }
            );
            setRefresh(false);
        }
    }, [refresh]);

    if (typeof debateData === "string") {
        return <Error status={HttpStatusCode.NotFound} message={debateData} />;
    }

    if (isDebateLoading || !debateData) {
        return <CircularProgress size={100} />;
    }

    const handleGoToLogin = () => {
        // TODO: Implement go to login call
    };

    const handlePostArgument = () => {
        // TODO: Implement post argument call
    };

    // TODO: use router for redirecting category
    return (
        <>
            <DebateHeader
                debate={debateData}
                refreshDebate={() => setRefresh(true)}
            />
            <div className="debate-content">
                <ArgumentList debate={debateData} />
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
                    <RecommendedDebatesSection debate={debateData} />
                </div>
            </div>
        </>
    );
};

export default DebateView;
