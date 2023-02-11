import { useEffect, useState } from "react";

import { HttpStatusCode } from "axios";
import { useLocation, useParams } from "react-router-dom";

import { CircularProgress } from "@mui/material";

import ArgumentList from "./ArgumentList";
import ChatSection from "./ChatSection";
import DebateHeader from "./DebateHeader";
import "./DebateView.css";
import PostArgument from "./PostArgument";
import RecommendedDebatesSection from "./RecommendedDebatesSection";
import VoteSection from "./VoteSection";

import { useGetDebateById } from "../../hooks/debates/useGetDebateById";
import { GetDebateOutput } from "../../hooks/debates/useGetDebateByUrl";
import "../../locales/index";
import { PaginatedList } from "../../types/PaginatedList";
import ArgumentDto from "../../types/dto/ArgumentDto";
import DebateDto from "../../types/dto/DebateDto";
import { Error } from "../Error/Error";

const DebateView = () => {
    const location = useLocation();
    const [debateData, setDebateData] = useState<DebateDto | undefined>();
    const [argumentList, setArgumentList] = useState<
        PaginatedList<ArgumentDto>
    >(PaginatedList.emptyList());

    const [refreshArguments, setRefreshArguments] = useState<boolean>(false);

    const params = useParams();

    const { loading: isDebateLoading, getDebate: getDebate } =
        useGetDebateById();

    useEffect(() => {
        if (location.state?.debate) {
            setDebateData(location.state.debate as DebateDto);
            location.state.debate = undefined;
            return;
        }
        const id: string = params.id?.toString() || "";
        getDebate({ id: parseInt(id) }).then((output: GetDebateOutput) => {
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

    const handleRefreshArguments = () => {
        setRefreshArguments(!refreshArguments);
    };

    return (
        <>
            <DebateHeader debate={debateData} />
            <div className="debate-content">
                <ArgumentList
                    debate={debateData}
                    argumentList={argumentList}
                    setArgumentList={setArgumentList}
                    refreshArguments={refreshArguments}
                />
                <div className="post-arguments">
                    <PostArgument
                        debate={debateData}
                        argumentList={argumentList}
                        refreshArgs={handleRefreshArguments}
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
