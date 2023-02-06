import { useEffect, useState } from "react";
import React from "react";

import { HttpStatusCode } from "axios";
import { useTranslation } from "react-i18next";
import { useSearchParams } from "react-router-dom";

import { CircularProgress, Pagination } from "@mui/material";
import { SelectChangeEvent } from "@mui/material";

import "./Discovery.css";

import CategoryFilters from "../../components/CategoryFilters/CategoryFilters";
import DebatesList from "../../components/DebatesList/DebatesList";
import OrderByComponent from "../../components/OrderByComponent/OrderByComponent";
import { useGetDebates } from "../../hooks/debates/useGetDebates";
import "../../locales/index";
import { PaginatedList } from "../../types/PaginatedList";
import DebateDto from "../../types/dto/DebateDto";
import DebateCategory from "../../types/enums/DebateCategory";
import DebateOrder from "../../types/enums/DebateOrder";
import DebateStatus from "../../types/enums/DebateStatus";

const Discovery = () => {
    const { t } = useTranslation();

    document.title = "Argu | " + t("discovery.title");

    const [page, setPage] = useState(1);

    const [debatesList, setDebatesList] = useState<
        PaginatedList<DebateDto> | undefined
    >(undefined);

    const [queryParams, setQueryParams] = useSearchParams();

    const { loading: isLoading, getDebates: getDebates } = useGetDebates();

    useEffect(() => {
        queryParams.delete("page");
        if (page > 1) {
            queryParams.append("page", page.toString());
        }
        setQueryParams(queryParams);
    }, [page]);

    useEffect(() => {
        getDebates({
            category: queryParams.get("category") as DebateCategory,
            order: queryParams.get("order") as DebateOrder,
            status: queryParams.get("status") as DebateStatus,
            date: queryParams.get("date") as string,
            page:
                (queryParams.get("page") as unknown as number) - 1 >= 0
                    ? (queryParams.get("page") as unknown as number) - 1
                    : 0,
            search: queryParams.get("search") as string,
            size: 5,
        })
            .then((res) => {
                switch (res.status) {
                    case HttpStatusCode.Ok:
                        setDebatesList(res.data);
                        break;
                    case HttpStatusCode.NotFound:
                    case HttpStatusCode.NoContent:
                        setDebatesList(undefined);
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
                {isLoading ? (
                    <CircularProgress size={100} />
                ) : (
                    <>
                        <div className="debates-list-container">
                            <DebatesList debates={debatesList?.data || []} />
                        </div>
                        <div className="pagination-container">
                            <Pagination
                                count={debatesList?.totalPages || 1}
                                page={page}
                                onChange={(e, v) => {
                                    setPage(v);
                                }}
                                showLastButton
                                showFirstButton
                                size="large"
                            />
                        </div>
                    </>
                )}
            </div>
        </div>
    );
};

export default Discovery;
