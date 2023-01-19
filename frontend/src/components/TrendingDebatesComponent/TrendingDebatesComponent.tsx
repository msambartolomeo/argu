import React from "react";
import "./TrendingDebatesComponent.css";
import { useTranslation } from "react-i18next";
import "../../locales/index";
import DebateCategory from "../../types/enums/DebateCategory";
import Debate from "../../types/Debate";
import DebatesList from "../DebatesList/DebatesList";

// TODO: Add icons
// TODO: Add debate list item component with API calls

const TrendingDebatesComponent = () => {
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

    const { t } = useTranslation();

    return (
        <div className="trending-debates-component-container">
            <h2 className="title-container">
                {t("landingPage.trending.title")}
            </h2>
            <div className="debate-list-container">
                <DebatesList debates={debatesList} />
            </div>
        </div>
    );
};

export default TrendingDebatesComponent;
