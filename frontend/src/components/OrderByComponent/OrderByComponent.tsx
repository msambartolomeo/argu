import { SelectChangeEvent } from "@mui/material";
import { useLocation } from "react-router-dom";
import DatePicker from "../DatePicker/DatePicker";
import "./OrderByComponent.css";
import SortSelectComponent from "./SortSelectComponent/SortSelectComponent";

interface OrderByComponentProps {
    queryOrder: string;
    handleSelectOrderChange: (event: SelectChangeEvent) => void;
    queryStatus: string;
    handleSelectStatusChange: (event: SelectChangeEvent) => void;
    selectedCategory: string | null;
}

const OrderByComponent = ({
    queryOrder,
    handleSelectOrderChange,
    queryStatus,
    handleSelectStatusChange,
    selectedCategory,
}: OrderByComponentProps) => {
    const orderBySuppliers = [
        { value: "date_desc", label: "Newest" },
        { value: "date_asc", label: "Oldest" },
        { value: "alpha_asc", label: "A - Z" },
        { value: "alpha_desc", label: "Z - A" },
        { value: "subs_asc", label: "Least subscribed" },
        { value: "subs_desc", label: "Most subscribed" },
    ];

    const statusSuppliers = [
        { value: "all", label: "All" },
        { value: "open", label: "Open" },
        { value: "closed", label: "Closed" },
    ];

    const orderByProps = {
        header: "Order by",
        defaultValue: "newest",
        id: "order-by",
        labelId: "order-by-label",
        suppliers: orderBySuppliers,
        query: queryOrder,
        handleSelectChange: handleSelectOrderChange,
    };

    const statusProps = {
        header: "Status",
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
                    ? `Showing debates in category: ${selectedCategory}`
                    : "Showing all debates"}
            </h5>
            <div className="order-by-container">
                <div className="select-container">
                    <SortSelectComponent {...orderByProps} />
                    <SortSelectComponent {...statusProps} />
                </div>
                <div className="date-picker-container">
                    <DatePicker label="Date" placeholder="Select a date" />
                </div>
            </div>
        </div>
    );
};

export default OrderByComponent;
