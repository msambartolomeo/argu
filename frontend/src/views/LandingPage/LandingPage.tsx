import "./LandingPage.css";

import parallaxBackgroud from "../../assets/speech-bubbles.jpeg";
import LandingPageAboutComponent from "../../components/LandingPageAboutComponent/LandingPageAboutComponent";
import LandingPageExploreComponent from "../../components/LandingPageExploreComponent/LandingPageExploreComponent";
import TrendingDebatesComponent from "../../components/TrendingDebatesComponent/TrendingDebatesComponent";
import DebateCategory from "../../types/enums/DebateCategory";

// TODO: Add icons

interface ParallaxBackgroundProps {
    background?: string;
}

const LandingPage = ({
    background = parallaxBackgroud,
}: ParallaxBackgroundProps) => {
    document.title = "Argu";

    const parallaxPages = 5;
    const categories = Object.values(DebateCategory);

    return (
        <div data-testid="landing-page" className="landing-container">
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
