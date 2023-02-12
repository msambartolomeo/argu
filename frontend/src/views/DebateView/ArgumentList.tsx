import { useEffect } from "react";

import { HttpStatusCode } from "axios";
import { useTranslation } from "react-i18next";
import { useSearchParams } from "react-router-dom";

import { CircularProgress, Pagination } from "@mui/material";

import ArgumentBubble from "../../components/ArgumentBubble/ArgumentBubble";
import { useGetArguments } from "../../hooks/arguments/useGetArguments";
import "../../locales/index";
import { PaginatedList } from "../../types/PaginatedList";
import ArgumentDto from "../../types/dto/ArgumentDto";
import DebateDto from "../../types/dto/DebateDto";
import { PAGE_DEFAULT } from "../../types/globalConstants";

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

    const [queryParams, setQueryParams] = useSearchParams();
    let page = parseInt(queryParams.get("page") || PAGE_DEFAULT, 10);

    useEffect(() => {
        getArguments({
            argumentsUrl: debate.arguments,
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
    }, [refreshArguments, debate]);

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

    const handleChangePage = async (value: number) => {
        let url = "";
        switch (value) {
            case 1:
                url = argumentList?.first || "";
                break;
            case argumentList?.totalPages:
                url = argumentList?.last || "";
                break;
            case page - 1:
                url = argumentList?.prev || "";
                break;
            case page + 1:
                url = argumentList?.next || "";
                break;
        }
        const res = await getArguments({ argumentsUrl: url });
        switch (res.status) {
            case HttpStatusCode.Ok:
                if (res.data) setArgumentList(res.data);
                break;
            case HttpStatusCode.NoContent:
                setArgumentList(PaginatedList.emptyList());
                break;
        }
        page = value;
        setQueryParams({ page: value.toString() });
    };

    return (
        <div className="z-depth-3 argument-list">
            {list}
            {argumentList?.data.length === 0 && (
                <h5 className="center">{t("debate.noArguments")}</h5>
            )}
            {argumentList.data.length > 0 && (
                <div className="pagination-format">
                    <Pagination
                        count={argumentList?.totalPages || 0}
                        color="primary"
                        className="white"
                        siblingCount={1}
                        page={page}
                        showFirstButton
                        showLastButton
                        onChange={(event, page) => handleChangePage(page)}
                    />
                </div>
            )}
        </div>
    );
}

export default ArgumentList;
