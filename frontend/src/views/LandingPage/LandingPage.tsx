import { Parallax, ParallaxLayer } from "@react-spring/parallax";
import React from "react";
import parallaxBackgroud from "../../assets/speech-bubbles.jpeg";
import Chip from "../../components/Chip/Chip";
import LandingPageExploreComponent from "../../components/LandingPageExploreComponent/LandinPageExploreComponent";
import Navbar from "../../components/Navbar/Navbar";
import DebateCategory from "../../model/enums/DebateCategory";

interface ParallaxBackgroundProps {
    background?: string;
}

const LandingPage = ({
    background = parallaxBackgroud,
}: ParallaxBackgroundProps) => {
    const parallaxPages = 5;
    const categories = Object.values(DebateCategory);

    return (
        <React.Fragment>
            <div className="navbar-backgound">
                <Parallax pages={parallaxPages}>
                    <ParallaxLayer
                        offset={0}
                        speed={1}
                        style={{
                            zIndex: 3,
                        }}
                    >
                        <Navbar />
                    </ParallaxLayer>
                    <ParallaxLayer
                        offset={0.2}
                        style={{
                            zIndex: 3,
                        }}
                    >
                        <div className="explore-container">
                            <LandingPageExploreComponent
                                categories={categories}
                            />
                        </div>
                    </ParallaxLayer>
                    <ParallaxLayer offset={1}>
                        <h2>Goodbye</h2>
                    </ParallaxLayer>
                    <ParallaxLayer
                        offset={0}
                        speed={0.4}
                        factor={0.65}
                        style={{
                            backgroundImage: `url(${background})`,
                            backgroundSize: "cover",
                            zIndex: 0,
                            opacity: 0.6,
                        }}
                    ></ParallaxLayer>
                    <ParallaxLayer
                        offset={0}
                        speed={0.4}
                        factor={0.65}
                        style={{
                            backgroundColor: "black",
                            backgroundSize: "cover",
                            zIndex: -1,
                            opacity: 1,
                        }}
                    ></ParallaxLayer>
                </Parallax>
            </div>
        </React.Fragment>
    );
};

export default LandingPage;
