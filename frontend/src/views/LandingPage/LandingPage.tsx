import { Parallax, ParallaxLayer } from "@react-spring/parallax";
import React from "react";
import parallaxBackgroud from "../../assets/speech-bubbles.jpeg";
import NavBar from "../../components/NavBar/NavBar";

interface ParallaxBackgroundProps {
    background?: string;
}

const LandingPage = ({
    background = parallaxBackgroud,
}: ParallaxBackgroundProps) => {
    return (
        <React.Fragment>
            <div>
                <Parallax pages={4}>
                    <NavBar></NavBar>
                    {/* <ParallaxLayer>
                        <h2>Welcome</h2>
                    </ParallaxLayer>
                    <ParallaxLayer offset={1}>
                        <h2>Goodbye</h2>
                    </ParallaxLayer>
                    <ParallaxLayer
                        offset={0}
                        speed={0.4}
                        factor={1}
                        style={{
                            backgroundImage: `url(${background})`,
                            backgroundSize: "cover",
                            opacity: 0,
                        }}
                    ></ParallaxLayer> */}
                </Parallax>
            </div>
        </React.Fragment>
    );
};

export default LandingPage;
