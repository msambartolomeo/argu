import { useLocation } from "react-router-dom";
import DatePicker from "../DatePicker/DatePicker";
import SelectDropdown from "../SelectDropdown/SelectDropdown";
import "./OrderByComponent.css";
import SortSelectComponent from "./SortSelectComponent/SortSelectComponent";

const OrderByComponent = () => {
    const search = useLocation().search;
    const selectedCategory = new URLSearchParams(search).get("category");

    const orderBySuppliers = [
        { value: "newest", label: "Newest" },
        { value: "oldest", label: "Oldest" },
        { value: "A - Z", label: "A - Z" },
        { value: "Z - A", label: "Z - A" },
        { value: "least-subscribed", label: "Least subscribed" },
        { value: "most-subscribed", label: "Most subscribed" },
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
    };

    const statusProps = {
        header: "Status",
        defaultValue: "all",
        id: "status",
        labelId: "status-label",
        suppliers: statusSuppliers,
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
                <DatePicker label="Date" placeholder="Select a date" />
            </div>
        </div>
    );
};

export default OrderByComponent;
