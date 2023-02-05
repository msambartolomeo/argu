import { Pagination, SelectChangeEvent } from "@mui/material";
import { useTranslation } from "react-i18next";

import { useEffect, useState } from "react";
import React from "react";

import { useTranslation } from "react-i18next";
import { useLocation, useNavigate } from "react-router-dom";

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

    const search = useLocation().search;
    const selectedCategory = new URLSearchParams(search).get("category");
    const date = useRef() as RefObject<HTMLInputElement>;

    const [queryOrder, setQueryOrder] = useState("");
    const [queryStatus, setQueryStatus] = useState("");
    const [page, setPage] = useState(1);
    const navigate = useNavigate();

    const handleSelectOrderChange = (event: SelectChangeEvent) => {
        setQueryOrder(event.target.value);
        setPage(1);
    };

    const handleSelectStatusChange = (event: SelectChangeEvent) => {
        setQueryStatus(event.target.value);
        setPage(1);
    };

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
        const queryParams = new URLSearchParams();
        if (selectedCategory) {
            queryParams.append("category", selectedCategory);
            if (queryOrder) {
                queryParams.append("order", queryOrder);
            }
            if (queryStatus) {
                queryParams.append("status", queryStatus);
            }
        } else {
            if (queryOrder) {
                queryParams.append("order", queryOrder);
            }
            if (queryStatus) {
                queryParams.append("status", queryStatus);
            }
        }
        queryParams.append("page", page.toString());
        getDebates({
            category: selectedCategory as DebateCategory,
            order: queryOrder as DebateOrder,
            status: queryStatus as DebateStatus,
            page: page - 1,
            size: 5,
        }).catch((error) => {
            throw new Error("Error loading debates list: ", error);
        });
        navigate("/discover?" + queryParams.toString());
    }, [queryOrder, queryStatus, selectedCategory, page, navigate]);

    const orderByProps = {
        queryOrder: queryOrder,
        handleSelectOrderChange: handleSelectOrderChange,
        queryStatus: queryStatus,
        handleSelectStatusChange: handleSelectStatusChange,
        selectedCategory: selectedCategory,
        date: date,
    };

    const categories = Object.values(DebateCategory);

    console.log("debatesList", debatesList);

    return (
        <div className="discovery-container">
            <div className="filters-container">
                <CategoryFilters selectedCategory={selectedCategory} />
            </div>
            <div className="debates-container">
                <div className="discovery-order-container">
                    <OrderByComponent {...orderByProps} />
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
