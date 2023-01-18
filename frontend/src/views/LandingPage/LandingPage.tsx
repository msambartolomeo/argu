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
// TODO: Align text and images in the about section

interface ParallaxBackgroundProps {
    background?: string;
}

const LandingPage = ({
    background = parallaxBackgroud,
}: ParallaxBackgroundProps) => {
    const debatesList: Debate[] = [
        {
            id: 1,
            name: "Is the earth flat?",
            description:
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec auctor, nisl eget ultricies tincidunt, nunc nisl aliquam nisl, eget aliquam nunc nisl euismod nunc. Donec auctor, nisl eget ultricies tincidunt, nunc nisl aliquam nisl, eget aliquam nunc nisl euismod nunc.",
            isCreatorFor: true,
            category: DebateCategory.SCIENCE,
            status: "open",
            createdDate: "2021-05-01",
            subscriptions: 3,
            votesFor: 2,
            votesAgainst: 1,
            creator: {
                username: "user1",
                email: "user1@gmail.com",
                createdDate: "2021-05-01",
            },
        },
        {
            id: 2,
            name: "The best programming language is Java",
            description:
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec auctor, nisl eget ultricies tincidunt, nunc nisl aliquam nisl, eget aliquam nunc nisl euismod nunc. Donec auctor, nisl eget ultricies tincidunt, nunc nisl aliquam nisl, eget aliquam nunc nisl euismod nunc.",
            isCreatorFor: false,
            category: DebateCategory.TECHNOLOGY,
            status: "open",
            createdDate: "2022-05-01",
            subscriptions: 5,
            votesFor: 2,
            votesAgainst: 3,
            creator: {
                username: "user2",
                email: "user2@gmail.com",
                createdDate: "2022-05-01",
            },
        },
        {
            id: 10,
            name: "The best programming language is ASM",
            description:
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec auctor, nisl eget ultricies tincidunt, nunc nisl aliquam nisl, eget aliquam nunc nisl euismod nunc. Donec auctor, nisl eget ultricies tincidunt, nunc nisl aliquam nisl, eget aliquam nunc nisl euismod nunc.",
            isCreatorFor: true,
            category: DebateCategory.TECHNOLOGY,
            status: "closed",
            createdDate: "2022-05-01",
            subscriptions: 50,
            votesFor: 1,
            votesAgainst: 20,
            creator: {
                username: "user3",
                email: "user3@gmail.com",
                createdDate: "2022-05-01",
            },
        },
    ];

    const parallaxPages = 5;
    const categories = Object.values(DebateCategory);

    return (
        <div className="landing-container">
            <div className="explore-container">
                <LandingPageExploreComponent categories={categories} />
            </div>
            {/* <div className="trending-debates-container">
                <TrendingDebatesComponent />
            </div> */}
            {/* <div className="explore-container">
                <LandingPageExploreComponent categories={categories} />
            </div> */}
            {/* <Parallax pages={parallaxPages}>
                <ParallaxLayer
                    offset={0.2}
                    style={{
                        zIndex: 3,
                    }}
                >
                    <div className="explore-container">
                        <LandingPageExploreComponent categories={categories} />
                    </div>
                </ParallaxLayer>
                <ParallaxLayer
                    offset={0.65}
                    style={{
                        zIndex: 3,
                    }}
                >
                    <div className="trending-debates-container">
                        <TrendingDebatesComponent />
                    </div>
                </ParallaxLayer>
                <ParallaxLayer
                    offset={0.65}
                    factor={0.55}
                    style={{
                        backgroundColor: "#212D40",
                        zIndex: 1,
                    }}
                ></ParallaxLayer>
                <ParallaxLayer
                    offset={1.65}
                    style={{
                        backgroundColor: "#D66853",
                        zIndex: 1,
                    }}
                ></ParallaxLayer>
                <ParallaxLayer
                    offset={1.25}
                    style={{
                        zIndex: 3,
                    }}
                >
                    <div className="about-container">
                        <LandingPageAboutComponent />
                    </div>
                </ParallaxLayer>
                <ParallaxLayer
                    offset={0}
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
                    factor={0.65}
                    style={{
                        backgroundColor: "black",
                        backgroundSize: "cover",
                        zIndex: -1,
                        opacity: 1,
                    }}
                ></ParallaxLayer>
            </Parallax> */}
        </div>
    );
};

export default LandingPage;
