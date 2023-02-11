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
import ArgumentDto from "../../types/dto/ArgumentDto";
import DebateDto from "../../types/dto/DebateDto";
import { Error } from "../Error/Error";

const DebateView = () => {
    const [debateData, setDebateData] = useState<DebateDto | undefined>();
    const [argumentList, setArgumentList] = useState<
        PaginatedList<ArgumentDto>
    >(PaginatedList.emptyList());

    const params = useParams();

    const { loading: isDebateLoading, getDebate: getDebate } =
        useGetDebateById();

    useEffect(() => {
        const id: string = params.id?.toString() || "";
        getDebate({ id: parseInt(id) }).then((output: GetDebateByIdOutput) => {
            if (output.status === HttpStatusCode.Ok) {
                setDebateData(output.data);
            }
        });
    }, [params]);

    if (typeof debateData === "string") {
        return <Error status={HttpStatusCode.NotFound} message={debateData} />;
    }

    if (isDebateLoading || !debateData) {
        return <CircularProgress size={100} />;
    }

    return (
        <>
            <DebateHeader debate={debateData} />
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
                    <VoteSection debateData={debateData} />
                    <ChatSection debate={debateData} />
                    <RecommendedDebatesSection debate={debateData} />
                </div>
            </div>
        </>
    );
};

export default DebateView;
