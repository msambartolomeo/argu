import { ChangeEvent, useState } from "react";

import { useTranslation } from "react-i18next";
import { useSearchParams } from "react-router-dom";

import "./OrderByComponent.css";
import SortSelectComponent from "./SortSelectComponent/SortSelectComponent";

import { useNonInitialEffect } from "../../hooks/useNonInitialEffect";
import "../../locales/index";
import DatePicker from "../DatePicker/DatePicker";

const orderValues = [
    "date_desc",
    "date_asc",
    "alpha_asc",
    "alpha_desc",
    "subs_asc",
    "subs_desc",
];

const statusValues = ["all", "open", "closed"];

const OrderByComponent = () => {
    const { t } = useTranslation();

    const [queryParams, setQueryParams] = useSearchParams();
    const [queryOrder, setQueryOrder] = useState(queryParams.get("order"));
    const [queryStatus, setQueryStatus] = useState(queryParams.get("status"));
    const selectedCategory = queryParams.get("category");

    const handleSelectOrderChange = (event: ChangeEvent<HTMLSelectElement>) => {
        setQueryOrder(event.target.value);
    };

    const handleSelectStatusChange = (
        event: ChangeEvent<HTMLSelectElement>
    ) => {
        setQueryStatus(event.target.value);
    };

    useNonInitialEffect(() => {
        queryParams.delete("order");
        queryParams.delete("page");
        if (queryOrder) {
            queryParams.append("order", queryOrder);
        }
        setQueryParams(queryParams);
    }, [queryOrder]);

    useNonInitialEffect(() => {
        queryParams.delete("status");
        queryParams.delete("page");
        if (queryStatus) {
            queryParams.append("status", queryStatus);
        }
        setQueryParams(queryParams);
    }, [queryStatus]);

    return (
        <div className="order-by-container-with-header">
            <h5 className="order-by-header">
                {selectedCategory
                    ? t("discovery.orderBy.showingDebatesInCategory", {
                          category: t("categories." + selectedCategory),
                      })
                    : t("discovery.orderBy.showingAllDebates")}
            </h5>
            <div className="order-by-container">
                <div className="filter-input-container">
                    <SortSelectComponent
                        options={orderValues}
                        type="order"
                        handleSelectChange={handleSelectOrderChange}
                    />
                    <SortSelectComponent
                        options={statusValues}
                        type="status"
                        handleSelectChange={handleSelectStatusChange}
                    />
                    <DatePicker
                        label={t("discovery.orderBy.datePicker.label")}
                    />
                </div>
            </div>
        </div>
    );
};

export default OrderByComponent;
