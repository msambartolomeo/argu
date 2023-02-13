import "./LandingPage.css";

import LandingPageAboutComponent from "../../components/LandingPageAboutComponent/LandingPageAboutComponent";
import LandingPageExploreComponent from "../../components/LandingPageExploreComponent/LandingPageExploreComponent";
import TrendingDebatesComponent from "../../components/TrendingDebatesComponent/TrendingDebatesComponent";
import DebateCategory from "../../types/enums/DebateCategory";

const LandingPage = () => {
    document.title = "Argu";

    const categories = Object.values(DebateCategory);

    return (
        <div className="landing-container">
            <div className="explore-container">
                <LandingPageExploreComponent categories={categories} />
            </div>
            <div className="trending-debates-container">
                <TrendingDebatesComponent />
            </div>
            <div className="about-container">
                <LandingPageAboutComponent />
            </div>
        </div>
    );
};

export default LandingPage;
