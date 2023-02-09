import { useEffect } from "react";

import { HttpStatusCode } from "axios";
import { useTranslation } from "react-i18next";

import { CircularProgress } from "@mui/material";

import ArgumentBubble from "../../components/ArgumentBubble/ArgumentBubble";
import Pagination from "../../components/Pagination/Pagination";
import { useGetArguments } from "../../hooks/arguments/useGetArguments";
import "../../locales/index";
import { PaginatedList } from "../../types/PaginatedList";
import ArgumentDto from "../../types/dto/ArgumentDto";
import DebateDto from "../../types/dto/DebateDto";

interface Props {
    debate: DebateDto;
    argumentList: PaginatedList<ArgumentDto>;
    setArgumentList: (list: PaginatedList<ArgumentDto>) => void;
    refreshArguments: boolean;
}

function ArgumentList({
    debate,
    argumentList,
    setArgumentList,
    refreshArguments,
}: Props) {
    const { t } = useTranslation();

    const { loading, getArguments } = useGetArguments();

    useEffect(() => {
        getArguments({
            argumentsUrl: debate.arguments,
            // TODO: add page
        }).then((out) => {
            switch (out.status) {
                case HttpStatusCode.Ok:
                    if (out.data) setArgumentList(out.data);
                    break;
                case HttpStatusCode.NoContent:
                    setArgumentList(PaginatedList.emptyList());
                    break;
                default:
                // TODO: error
            }
        });
    }, [refreshArguments]);

    if (loading) {
        return (
            // TODO: Center
            <div className="z-depth-3 argument-list">
                <CircularProgress size={100} />
            </div>
        );
    }

    const list = argumentList?.data.map((argument, index) => {
        return (
            <div key={argument.self}>
                {
                    /* NOTE: only show header if it is the first element 
                       or if the status of the previous one is different */
                    (index === 0 ||
                        argumentList.data[index - 1].status !==
                            argument.status) && (
                        <h5 className="center">{argument.status}</h5>
                    )
                }
                <ArgumentBubble
                    argumentData={argument}
                    debateCreator={debate.creatorName}
                />
            </div>
        );
    });

    return (
        <div className="z-depth-3 argument-list">
            {list}
            {argumentList?.data.length === 0 && (
                <h5 className="center">{t("debate.noArguments")}</h5>
            )}
            <Pagination param="" totalPages={1} />
        </div>
    );
}

export default ArgumentList;
