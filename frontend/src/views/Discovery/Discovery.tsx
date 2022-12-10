import CategoryFilters from "../../components/CategoryFilters/CategoryFilters";
import Pagination from "../../components/Pagination/Pagination";
import "./Discovery.css";

const Discovery = () => {
    document.title = "Argu | Debates";

    return (
        <>
            <div className="category-filters-container">
                <CategoryFilters />
            </div>
        </>
    );
};

export default Discovery;
