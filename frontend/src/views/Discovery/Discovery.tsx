import { Pagination } from "@mui/material";
import { useTranslation } from "react-i18next";

import { useEffect, useState } from "react";
import React from "react";

import { useSearchParams } from "react-router-dom";

import { SelectChangeEvent } from "@mui/material";

import "./Discovery.css";

import CategoryFilters from "../../components/CategoryFilters/CategoryFilters";
import DebatesList from "../../components/DebatesList/DebatesList";
import OrderByComponent from "../../components/OrderByComponent/OrderByComponent";
import { useGetDebates } from "../../hooks/debates/useGetDebates";
import "../../locales/index";
import DebateDto from "../../types/dto/DebateDto";
import DebateCategory from "../../types/enums/DebateCategory";
import DebateOrder from "../../types/enums/DebateOrder";
import DebateStatus from "../../types/enums/DebateStatus";

const Discovery = () => {
    const { t } = useTranslation();

    document.title = "Argu | " + t("discovery.title");

    const [page, setPage] = useState(1);

    const [queryParams, setQueryParams] = useSearchParams();

    const {
        data: debatesList,
        loading: isLoading,
        getDebates: getDebates,
    } = useGetDebates();

    const handlePageChange = (
        event: React.ChangeEvent<unknown>,
        value: number
    ) => {
        setPage(value);
    };

    useEffect(() => {
        // TODO: handle page updates in another useEffect
        // queryParams.append("page", page.toString());
        getDebates({
            category: queryParams.get("category") as DebateCategory,
            order: queryParams.get("order") as DebateOrder,
            status: queryParams.get("status") as DebateStatus,
            date: queryParams.get("date") as string,
            page: page - 1,
        }).catch((error) => {
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
                <div className="debates-list-container">
                    <DebatesList debates={debatesList} />
                </div>
                <div className="pagination-container">
                    <Pagination
                        count={10}
                        page={page}
                        onChange={handlePageChange}
                        showLastButton
                        showFirstButton
                        size="large"
                    />
                </div>
            </div>
        </div>
    );
};

export default Discovery;
