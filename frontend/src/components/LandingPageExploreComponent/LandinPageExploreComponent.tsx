import React from "react";
import Chip from "@mui/material/Chip";
import Stack from "@mui/material/Stack";
import "./LandingPageExploreComponent.css";
import { useTranslation } from "react-i18next";
import "../../locales/index";

// TODO: Add icons

interface CategoriesProps {
    categories: string[];
}

const LandingPageExploreComponent = ({ categories }: CategoriesProps) => {
    const { t } = useTranslation();

    return (
        <React.Fragment>
            <h2 className="text-container">{t("landingPage.explore.title")}</h2>
            <div className="chip-container">
                <Stack direction="row" spacing={1}>
                    {categories.map((category) => (
                        <Chip
                            className="chip-class"
                            key={category}
                            label={t(`landingPage.explore.buttons.${category}`)}
                            component="a"
                            href={`/debates?category=${category}`}
                            clickable
                        />
                    ))}
                </Stack>
            </div>
        </React.Fragment>
    );
};

export default LandingPageExploreComponent;
