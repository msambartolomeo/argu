import React from "react";
import Chip from "@mui/material/Chip";
import Stack from "@mui/material/Stack";
import "./LandingPageExploreComponent.css";

// TODO: Internationalize
// TODO: Add icons

interface CategoriesProps {
    categories: string[];
}

const LandingPageExploreComponent = ({ categories }: CategoriesProps) => {
    return (
        <React.Fragment>
            <h2 className="text-container">Explore debates</h2>
            <div className="chip-container">
                <Stack direction="row" spacing={1}>
                    {categories.map((category) => (
                        <Chip
                            className="chip-class"
                            key={category}
                            label={category}
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
