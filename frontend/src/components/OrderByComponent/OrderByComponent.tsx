import { SelectChangeEvent } from "@mui/material";
import { useTranslation } from "react-i18next";

import { RefObject } from "react";

import "../../locales/index";
import DatePicker from "../DatePicker/DatePicker";
import { SelectChangeEvent } from "@mui/material";

import "./OrderByComponent.css";
import SortSelectComponent from "./SortSelectComponent/SortSelectComponent";

interface OrderByComponentProps {
    queryOrder: string;
    handleSelectOrderChange: (event: SelectChangeEvent) => void;
    queryStatus: string;
    handleSelectStatusChange: (event: SelectChangeEvent) => void;
    selectedCategory: string | null;
    date: RefObject<HTMLInputElement>;
}

const OrderByComponent = ({
    queryOrder,
    handleSelectOrderChange,
    queryStatus,
    handleSelectStatusChange,
    selectedCategory,
    date,
}: OrderByComponentProps) => {
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
                        date={date}
                        currentDate=""
                    />
                </div>
            </div>
        </div>
    );
};

export default OrderByComponent;
