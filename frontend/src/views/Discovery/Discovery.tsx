import { useEffect, useState } from "react";

import { HttpStatusCode } from "axios";
import { useTranslation } from "react-i18next";
import { useSearchParams } from "react-router-dom";

import { CircularProgress, Pagination } from "@mui/material";

import "./Discovery.css";

import CategoryFilters from "../../components/CategoryFilters/CategoryFilters";
import DebatesList from "../../components/DebatesList/DebatesList";
import NoDebatesFound from "../../components/NoDebatesFound/NoDebatesFound";
import OrderByComponent from "../../components/OrderByComponent/OrderByComponent";
import { useGetDebates } from "../../hooks/debates/useGetDebates";
import { useGetDebatesByUrl } from "../../hooks/debates/useGetDebatesByUrl";
import { useNonInitialEffect } from "../../hooks/useNonInitialEffect";
import "../../locales/index";
import { PaginatedList } from "../../types/PaginatedList";
import DebateDto from "../../types/dto/DebateDto";
import DebateCategory from "../../types/enums/DebateCategory";
import DebateOrder from "../../types/enums/DebateOrder";
import DebateStatus from "../../types/enums/DebateStatus";

const Discovery = () => {
    const { t } = useTranslation();

    document.title = "Argu | " + t("discovery.title");

    const [debatesList, setDebatesList] = useState<PaginatedList<DebateDto>>(
        PaginatedList.emptyList()
    );

    const [queryParams, setQueryParams] = useSearchParams();

    const [page, setPage] = useState<number>(
        Number(queryParams.get("page") || 1)
    );
    const [newPage, setNewPage] = useState<number>();

    const { loading: isLoading, getDebates: getDebates } = useGetDebates();
    const { loading: isDebatesUrlLoading, getDebatesByUrl: getDebatesByUrl } =
        useGetDebatesByUrl();

    useNonInitialEffect(() => {
        if (newPage) {
            queryParams.delete("page");
            queryParams.append("page", newPage.toString());
            setQueryParams(queryParams);
        }
    }, [newPage]);

    useEffect(() => {
        const queryPage = Number(queryParams.get("page"));
        if (debatesList.data.length !== 0 && queryPage) {
            let url = "";
            switch (queryPage) {
                case 1:
                    url = debatesList?.first || "";
                    break;
                case debatesList?.totalPages:
                    url = debatesList?.last || "";
                    break;
                case page - 1:
                    url = debatesList?.prev || "";
                    break;
                case page + 1:
                    url = debatesList?.next || "";
                    break;
            }
            if (url) {
                getDebatesByUrl({ url: url }).then((res) => {
                    switch (res.status) {
                        case HttpStatusCode.Ok:
                            if (res.data) setDebatesList(res.data);
                            break;
                        case HttpStatusCode.NoContent:
                            setDebatesList(PaginatedList.emptyList());
                            break;
                    }
                });
                setPage(queryPage);
            }
        } else {
            getDebates({
                category: queryParams.get("category") as DebateCategory,
                order: queryParams.get("order") as DebateOrder,
                status: queryParams.get("status") as DebateStatus,
                date: queryParams.get("date") as string,
                page: queryPage - 1 >= 0 ? queryPage - 1 : 0,
                search: queryParams.get("search") as string,
                size: 5,
            })
                .then((res) => {
                    switch (res.status) {
                        case HttpStatusCode.Ok:
                            if (res.data) setDebatesList(res.data);
                            break;
                        case HttpStatusCode.NotFound:
                        case HttpStatusCode.NoContent:
                            setDebatesList(PaginatedList.emptyList());
                            break;
                        case HttpStatusCode.BadRequest:
                            throw new Error("Bad request");
                        default:
                            throw new Error("Unknown error");
                    }
                })

                .catch((error) => {
                    throw new Error("Error loading debates list: ", error);
                });
        }
    }, [queryParams]);

    return (
        <div className="discovery-container">
            <div className="filters-container">
                <CategoryFilters />
            </div>
            <div className="debates-container">
                <div className="discovery-order-container">
                    <OrderByComponent />
                </div>
                {isLoading || isDebatesUrlLoading ? (
                    <CircularProgress size={100} />
                ) : debatesList.data.length > 0 ? (
                    <>
                        <div className="debates-list-container">
                            <DebatesList debates={debatesList.data} />
                        </div>
                        <div className="pagination-container">
                            <Pagination
                                count={debatesList.totalPages}
                                page={page}
                                color="primary"
                                className="white"
                                onChange={(e, v) => {
                                    setNewPage(v);
                                }}
                                showLastButton
                                showFirstButton
                                siblingCount={0}
                                boundaryCount={0}
                            />
                        </div>
                    </>
                ) : (
                    <div className="no-debates-container">
                        <NoDebatesFound />
                    </div>
                )}
            </div>
        </div>
    );
};

export default Discovery;
