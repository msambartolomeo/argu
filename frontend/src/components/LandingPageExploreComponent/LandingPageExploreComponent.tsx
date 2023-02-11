import React from "react";

import { useTranslation } from "react-i18next";
import { createSearchParams, useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";

import Chip from "@mui/material/Chip";
import Stack from "@mui/material/Stack";

import "./LandingPageExploreComponent.css";

import "../../locales/index";

// TODO: Add icons

interface CategoriesProps {
    categories: string[];
}

const LandingPageExploreComponent = ({ categories }: CategoriesProps) => {
    const { t } = useTranslation();
    const navigate = useNavigate();

    return (
        <div className="explore-component-container">
            <h2 className="text-container">
                <i className="material-icons left medium">explore</i>
                {t("landingPage.explore.title")}
            </h2>
            <div className="chip-container">
                <Link
                    to={{
                        pathname: "/discover",
                    }}
                    replace={false}
                    className="chip-class"
                >
                    {t("discovery.categories.all")}
                </Link>
                {categories.map((category) => (
                    <Link
                        to={{
                            pathname: "/discover",
                            search: createSearchParams({
                                category: category,
                            }).toString(),
                        }}
                        className="chip-class"
                        key={category}
                        replace={false}
                    >
                        {t(`categories.${category}`)}
                    </Link>
                ))}
            </div>
        </div>
    );
};

export default LandingPageExploreComponent;
