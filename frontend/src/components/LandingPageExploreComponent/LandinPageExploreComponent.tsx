import Chip from "@mui/material/Chip";
import Stack from "@mui/material/Stack";
import { useTranslation } from "react-i18next";

import React from "react";

import "../../locales/index";
import "./LandingPageExploreComponent.css";

// TODO: Add icons

interface CategoriesProps {
    categories: string[];
}

const LandingPageExploreComponent = ({ categories }: CategoriesProps) => {
    const { t } = useTranslation();

    return (
        <div className="explore-component-container">
            <h2 className="text-container">
                <i className="material-icons left medium">explore</i>
                {t("landingPage.explore.title")}
            </h2>
            <div className="chip-container">
                <Stack direction="row" spacing={1}>
                    {categories.map((category) => (
                        <Chip
                            className="chip-class"
                            key={category}
                            label={t(`landingPage.explore.buttons.${category}`)}
                            component="a"
                            href={`/paw-2022a-06/discover?category=${category}`}
                            clickable
                        />
                    ))}
                </Stack>
            </div>
        </div>
    );
};

export default LandingPageExploreComponent;
