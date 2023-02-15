import { useEffect, useState } from "react";

import { HttpStatusCode } from "axios";
import { useSnackbar } from "notistack";
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
    const { enqueueSnackbar } = useSnackbar();

    const [queryParams, setQueryParams] = useSearchParams();
    const [page, setPage] = useState<number>(
        Number(queryParams.get("page")) || Number(PAGE_DEFAULT)
    );
    const [argumentsUrl, setArgumentsUrl] = useState<string>(() => {
        const page = queryParams.get("page");
        if (page) return `${debate.arguments}?page=${Number(page) - 1}`;
        return debate.arguments;
    });

    const { loading, getArguments } = useGetArguments();

    function callGet() {
        getArguments({
            argumentsUrl,
        }).then((out) => {
            switch (out.status) {
                case HttpStatusCode.Ok:
                    if (out.data) setArgumentList(out.data);
                    break;
                case HttpStatusCode.NoContent:
                    setArgumentList(PaginatedList.emptyList());
                    break;
                default:
                    enqueueSnackbar(t("errors.unexpected"), {
                        variant: "error",
                    });
            }
        });
    }

    useEffect(() => {
        callGet();
    }, [refreshArguments, debate, argumentsUrl]);

    const handleChangePage = async (value: number) => {
        if (value === page) return;

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
        if (url) {
            setArgumentsUrl(url);
            setQueryParams({ page: value.toString() });
            setPage(value);
        }
    };

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

    if (argumentList.data.length === 0 && loading) {
        return (
            // TODO: Center
            <div className="z-depth-3 argument-list">
                <CircularProgress size={100} />
            </div>
        );
    }

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
