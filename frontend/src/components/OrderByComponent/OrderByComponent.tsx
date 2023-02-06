import { SelectChangeEvent } from "@mui/material";
import { useTranslation } from "react-i18next";

import { useEffect, useState } from "react";

import { useSearchParams } from "react-router-dom";

import "../../locales/index";
import DatePicker from "../DatePicker/DatePicker";
import { SelectChangeEvent } from "@mui/material";

import "./OrderByComponent.css";
import SortSelectComponent from "./SortSelectComponent/SortSelectComponent";

const OrderByComponent = () => {
    const { t } = useTranslation();

    const datePickerPlaceholder: string = t(
        "discovery.orderBy.datePicker.placeholder"
    );

    const orderBySuppliers = [
        { value: "date_desc", label: "dateDesc" },
        { value: "date_asc", label: "dateAsc" },
        { value: "alpha_asc", label: "alphaAsc" },
        { value: "alpha_desc", label: "alphaDesc" },
        { value: "subs_asc", label: "subsAsc" },
        { value: "subs_desc", label: "subsDesc" },
    ];

    const statusSuppliers = [
        { value: "all", label: "all" },
        { value: "open", label: "open" },
        { value: "closed", label: "closed" },
    ];

    const [queryParams, setQueryParams] = useSearchParams();
    const [queryOrder, setQueryOrder] = useState(
        queryParams.get("order") ?? ""
    );
    const [queryStatus, setQueryStatus] = useState(
        queryParams.get("status") ?? ""
    );
    const selectedCategory = queryParams.get("category");

    const handleSelectOrderChange = (event: SelectChangeEvent) => {
        setQueryOrder(event.target.value);
    };

    const handleSelectStatusChange = (event: SelectChangeEvent) => {
        setQueryStatus(event.target.value);
    };

    useEffect(() => {
        queryParams.delete("order");
        queryParams.delete("page");
        if (queryOrder) {
            queryParams.append("order", queryOrder);
        }
        setQueryParams(queryParams);
    }, [queryOrder]);

    useEffect(() => {
        queryParams.delete("status");
        queryParams.delete("page");
        if (queryStatus) {
            queryParams.append("status", queryStatus);
        }
        setQueryParams(queryParams);
    }, [queryStatus]);

    const orderByProps = {
        header: "Order by",
        name: "orderBy",
        defaultValue: "newest",
        id: "order-by",
        labelId: "order-by-label",
        suppliers: orderBySuppliers,
        query: queryOrder,
        handleSelectChange: handleSelectOrderChange,
    };

    const statusProps = {
        header: "Status",
        name: "status",
        defaultValue: "all",
        id: "status",
        labelId: "status-label",
        suppliers: statusSuppliers,
        query: queryStatus,
        handleSelectChange: handleSelectStatusChange,
    };

    return (
        <div className="order-by-container-with-header">
            <h5 className="order-by-header">
                {selectedCategory
                    ? t("discovery.orderBy.showingDebatesInCategory", {
                          category: t(
                              "discovery.categories." + selectedCategory
                          ),
                      })
                    : t("discovery.orderBy.showingAllDebates")}
            </h5>
            <div className="order-by-container">
                <div className="select-container">
                    <SortSelectComponent {...orderByProps} />
                    <SortSelectComponent {...statusProps} />
                </div>
                <div className="date-picker-container">
                    <DatePicker
                        label={t("discovery.orderBy.datePicker.label")}
                        placeholder={datePickerPlaceholder}
                    />
                </div>
            </div>
        </div>
    );
};

export default OrderByComponent;
