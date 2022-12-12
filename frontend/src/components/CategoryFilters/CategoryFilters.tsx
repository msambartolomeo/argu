import { Button, Stack } from "@mui/material";
import DebateCategory from "../../model/enums/DebateCategory";
import "./CategoryFilters.css";
import { useLocation } from "react-router-dom";

const CategoryFilters = () => {
    const categories = Object.values(DebateCategory);

    const search = useLocation().search;
    const selectedCategory = new URLSearchParams(search).get("category");

    return (
        <div className="category-filters-container">
            <h5 className="categories-header">Categories</h5>
            <Stack direction="column" spacing={1} className="categories-stack">
                <Button
                    variant="text"
                    href="/debates"
                    className={`category-button ${
                        selectedCategory === null ? "active" : ""
                    }`}
                >
                    All
                </Button>
                {categories.map((category) => (
                    <Button
                        key={category}
                        variant="text"
                        href={`/debates?category=${category}`}
                        className={`category-button ${
                            category === selectedCategory ? "active" : ""
                        }`}
                    >
                        {category}
                    </Button>
                ))}
            </Stack>
        </div>
    );
};

export default CategoryFilters;
