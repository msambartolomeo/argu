import React from "react";
import Chip from "@mui/material/Chip";
import Stack from "@mui/material/Stack";
import "./LandingPageExploreComponent.css";

interface CategoriesProps {
    categories: string[];
}

const LandingPageExploreComponent = ({ categories }: CategoriesProps) => {
    return (
        <React.Fragment>
            <h1 className="text-container">Explore debates</h1>
            <Stack direction="row" spacing={1}>
                {categories.map((category) => (
                    <Chip
                        className="chip"
                        key={category}
                        label={category}
                        component="a"
                        href={`/debates?category=${category}`}
                        clickable
                    />
                ))}
            </Stack>
        </React.Fragment>
    );
};

export default LandingPageExploreComponent;
