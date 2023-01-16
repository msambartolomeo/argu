import { Button, Stack } from "@mui/material";
import { Link } from "react-router-dom";
import DebateCategory from "../../types/enums/DebateCategory";
import "./CategoryFilters.css";

interface CategoryFiltersProps {
    selectedCategory: string | null;
}

const CategoryFilters = ({ selectedCategory }: CategoryFiltersProps) => {
    const categories = Object.values(DebateCategory);

    return (
        <div className="category-filters-container">
            <h5 className="categories-header">Categories</h5>
            <Stack direction="column" spacing={1} className="categories-stack">
                <Link
                    to="/discover"
                    className={`category-button ${
                        selectedCategory === null
                            ? "selected-category-button"
                            : ""
                    }`}
                >
                    All
                </Link>
                {categories.map((category) => (
                    <Link
                        key={category}
                        to={`/discover?category=${category}`}
                        className={`category-button ${
                            category === selectedCategory
                                ? "selected-category-button"
                                : ""
                        }`}
                    >
                        {category}
                    </Link>
                ))}
            </Stack>
        </div>
    );
};

export default CategoryFilters;
