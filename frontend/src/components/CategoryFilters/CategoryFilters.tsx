import { useTranslation } from "react-i18next";

import { useEffect, useState } from "react";

import { useSearchParams } from "react-router-dom";

import { Stack } from "@mui/material";

import "./CategoryFilters.css";

import "../../locales/index";
import DebateCategory from "../../types/enums/DebateCategory";

const CategoryFilters = () => {
    const { t } = useTranslation();

    const [queryParams, setQueryParams] = useSearchParams();
    const [selectedCategory, setSelectedCategory] = useState(
        queryParams.get("category")
    );
    const categories = Object.values(DebateCategory);

    useEffect(() => {
        queryParams.delete("category");
        queryParams.delete("page");
        if (selectedCategory) {
            queryParams.append("category", selectedCategory);
        }
        setQueryParams(queryParams);
    }, [selectedCategory]);

    return (
        <div className="category-filters-container">
            <h5 className="categories-header">
                {t("discovery.categories.title")}
            </h5>
            <Stack direction="column" spacing={1} className="categories-stack">
                <div
                    onClick={() => {
                        setSelectedCategory(null);
                    }}
                    className={`category-button ${
                        selectedCategory === null
                            ? "selected-category-button"
                            : ""
                    }`}
                >
                    {t("discovery.categories.all")}
                </div>
                {categories.map((category) => (
                    <div
                        key={category}
                        className={`category-button ${
                            category === selectedCategory
                                ? "selected-category-button"
                                : ""
                        }`}
                        onClick={() => {
                            setSelectedCategory(category);
                        }}
                    >
                        {t(`discovery.categories.${category}`)}
                    </div>
                ))}
            </Stack>
        </div>
    );
};

export default CategoryFilters;
