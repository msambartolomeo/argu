import { useEffect, useState } from "react";

import { HttpStatusCode } from "axios";
import { useTranslation } from "react-i18next";

import { CircularProgress } from "@mui/material";

import ArgumentBubble from "../../components/ArgumentBubble/ArgumentBubble";
import Pagination from "../../components/Pagination/Pagination";
import { useGetArguments } from "../../hooks/arguments/useGetArguments";
import { PaginatedList } from "../../types/PaginatedList";
import ArgumentDto from "../../types/dto/ArgumentDto";
import DebateDto from "../../types/dto/DebateDto";

interface Props {
    debate: DebateDto;
}

function ArgumentList({ debate }: Props) {
    const [argumentList, setArgumentList] =
        useState<PaginatedList<ArgumentDto>>();

    const { t } = useTranslation();

    const { loading, getArguments } = useGetArguments();

    useEffect(() => {
        getArguments({
            argumentsUrl: debate.arguments,
            // TODO: add page
        }).then((out) => {
            switch (out.status) {
                case HttpStatusCode.Ok:
                    setArgumentList(out.data);
                    break;
                case HttpStatusCode.NoContent:
                    setArgumentList(PaginatedList.emptyList());
                    break;
                default:
                // TODO: error
            }
        });
    }, [debate]);

    if (loading) {
        return (
            // TODO: Center
            <div className="z-depth-3 argument-list">
                <CircularProgress size={100} />
            </div>
        );
    }

    return (
        <div className="z-depth-3 argument-list">
            {argumentList?.data.map((argument) => {
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
                        <ArgumentBubble
                            key={argument.self}
                            argumentData={argument}
                            debateCreator={debate.creatorName}
                        />
                    </>
                );
            })}
            {argumentList?.data.length === 0 && (
                <h5 className="center">{t("debate.noArguments")}</h5>
            )}
            <Pagination param="" totalPages={1} />
        </div>
    );
}

export default ArgumentList;
