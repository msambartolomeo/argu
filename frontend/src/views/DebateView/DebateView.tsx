import { useEffect, useState } from "react";

import { HttpStatusCode } from "axios";
import { useLocation, useParams } from "react-router-dom";

import { CircularProgress } from "@mui/material";

import ArgumentList from "./ArgumentList/ArgumentList";
import ChatSection from "./ChatSection/ChatSection";
import DebateHeader from "./DebateHeader/DebateHeader";
import "./DebateView.css";
import PostArgument from "./PostArgument/PostArgument";
import RecommendedDebatesSection from "./RecommendedDebatesSection/RecommendedDebatesSection";
import VoteSection from "./VoteSection/VoteSection";

import { useGetDebateById } from "../../hooks/debates/useGetDebateById";
import { GetDebateOutput } from "../../hooks/debates/useGetDebateByUrl";
import "../../locales/index";
import { PaginatedList } from "../../types/PaginatedList";
import ArgumentDto from "../../types/dto/ArgumentDto";
import DebateDto from "../../types/dto/DebateDto";

const DebateView = () => {
    const location = useLocation();
    const [debateData, setDebateData] = useState<DebateDto | undefined>();
    const [argumentList, setArgumentList] = useState<
        PaginatedList<ArgumentDto>
    >(PaginatedList.emptyList());

    const [refreshArguments, setRefreshArguments] = useState<boolean>(false);

    const params = useParams();

    const { getDebate: getDebate } = useGetDebateById();

    function callGet() {
        getDebate({ id: Number(params.id) }).then((output: GetDebateOutput) => {
            if (output.status === HttpStatusCode.Ok) {
                setDebateData(output.data);
            }
        });
    }

    useEffect(() => {
        setDebateData(undefined);
        if (location.state?.debate) {
            setDebateData(location.state.debate as DebateDto);
            location.state.debate = undefined;
        } else {
            callGet();
        }

        const interval = setInterval(callGet, 30000);

        return () => clearInterval(interval);
    }, [params]);

    if (!debateData) {
        return <CircularProgress size={100} />;
    }

    const handleRefreshArguments = () => {
        setRefreshArguments((currentVal) => !currentVal);
    };

    document.title = "Argu | " + debateData.name;

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
