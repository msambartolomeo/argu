import { useEffect, useState } from "react";

import { HttpStatusCode } from "axios";
import { useParams } from "react-router-dom";

import { CircularProgress } from "@mui/material";

import ArgumentList from "./ArgumentList";
import ChatSection from "./ChatSection";
import DebateHeader from "./DebateHeader";
import "./DebateView.css";
import PostArgument from "./PostArgument";
import RecommendedDebatesSection from "./RecommendedDebatesSection";
import VoteSection from "./VoteSection";

import {
    GetDebateByIdOutput,
    useGetDebateById,
} from "../../hooks/debates/useGetDebateById";
import "../../locales/index";
import { PaginatedList } from "../../types/PaginatedList";
import User from "../../types/User";
import ArgumentDto from "../../types/dto/ArgumentDto";
import DebateDto from "../../types/dto/DebateDto";
import UserRole from "../../types/enums/UserRole";
import { Error } from "../Error/Error";

// TODO: Connect to API and remove
const user1: User = {
    username: "azul",
    createdDate: "2021-01-01",
    role: UserRole.MODERATOR,
};

const DebateView = () => {
    // TODO: Change to real values and hooks
    const [debateData, setDebateData] = useState<DebateDto | undefined>();
    const [argumentList, setArgumentList] = useState<
        PaginatedList<ArgumentDto>
    >(PaginatedList.emptyList());

    const [refresh, setRefresh] = useState<boolean>(true);
    const [userData, setuserData] = useState<User | undefined>(user1);

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

    return (
        <>
            <DebateHeader
                debate={debateData}
                refreshDebate={() => setRefresh(true)}
            />
            <div className="debate-content">
                <ArgumentList
                    debate={debateData}
                    argumentList={argumentList}
                    setArgumentList={setArgumentList}
                />
                <div className="post-arguments">
                    <PostArgument
                        debate={debateData}
                        argumentList={argumentList}
                        setArgumentList={setArgumentList}
                    />
                    <VoteSection debate={debateData} userData={userData} />
                    <ChatSection debate={debateData} userData={userData} />
                    <RecommendedDebatesSection debate={debateData} />
                </div>
            </div>
        </>
    );
};

export default DebateView;
