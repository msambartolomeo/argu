import { Stack } from "@mui/material";
import { useTranslation } from "react-i18next";

import { Link } from "react-router-dom";

import "../../locales/index";
import DebateCategory from "../../types/enums/DebateCategory";
import "./CategoryFilters.css";

interface CategoryFiltersProps {
    selectedCategory: string | null;
}

const CategoryFilters = ({ selectedCategory }: CategoryFiltersProps) => {
    const { t } = useTranslation();

    const categories = Object.values(DebateCategory);

    return (
        <div className="category-filters-container">
            <h5 className="categories-header">
                {t("discovery.categories.title")}
            </h5>
            <Stack direction="column" spacing={1} className="categories-stack">
                <Link
                    to="/discover"
                    className={`category-button ${
                        selectedCategory === null
                            ? "selected-category-button"
                            : ""
                    }`}
                >
                    {t("discovery.categories.all")}
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
                        {t(`discovery.categories.${category}`)}
                    </Link>
                ))}
            </Stack>
        </div>
    );
};

export default CategoryFilters;
