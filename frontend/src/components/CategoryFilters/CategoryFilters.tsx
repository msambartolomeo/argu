import { Button, Stack } from "@mui/material";
import DebateCategory from "../../model/enums/DebateCategory";
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
