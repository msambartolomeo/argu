import React from "react";
import "./TrendingDebatesComponent.css";

// TODO: Internationalize
// TODO: Add icons
// TODO: Add debate list item component with API calls

const TrendingDebatesComponent = () => {
    return (
        <React.Fragment>
            <div className="trending-debates-container">
                <h2 className="title-container">Trending</h2>
                <h4 className="debate-container">debate-list-item</h4>
                <h4 className="debate-container">debate-list-item</h4>
                <h4 className="debate-container">debate-list-item</h4>
            </div>
        </React.Fragment>
    );
};

export default TrendingDebatesComponent;
