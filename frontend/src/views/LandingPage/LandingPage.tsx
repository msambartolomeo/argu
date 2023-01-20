import { Parallax, ParallaxLayer } from "@react-spring/parallax";
import React from "react";
import parallaxBackgroud from "../../assets/speech-bubbles.jpeg";
import LandingPageExploreComponent from "../../components/LandingPageExploreComponent/LandinPageExploreComponent";
import Navbar from "../../components/Navbar/Navbar";
import TrendingDebatesComponent from "../../components/TrendingDebatesComponent/TrendingDebatesComponent";
import DebateCategory from "../../types/enums/DebateCategory";
import LandingPageAboutComponent from "../../components/LandingPageAboutComponent/LandingPageAboutComponent";
import Debate from "../../types/Debate";
import "./LandingPage.css";

// TODO: Add icons

interface ParallaxBackgroundProps {
    background?: string;
}

const LandingPage = ({
    background = parallaxBackgroud,
}: ParallaxBackgroundProps) => {
    const parallaxPages = 5;
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
