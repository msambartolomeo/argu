import React, { useEffect, useState } from "react";
import "./TrendingDebatesComponent.css";
import { useTranslation } from "react-i18next";
import "../../locales/index";
import DebateCategory from "../../types/enums/DebateCategory";
import Debate from "../../types/Debate";
import DebatesList from "../DebatesList/DebatesList";
import DebateDto from "../../types/dto/DebateDto";
import { useGetDebates } from "../../hooks/debates/useGetDebates";
import DebateOrder from "../../types/enums/DebateOrder";
import DebateStatus from "../../types/enums/DebateStatus";

// TODO: Add icons
// TODO: Add debate list item component with API calls

const TrendingDebatesComponent = () => {
    // const debatesList: DebateDto[] = [
    //     {
    //         id: 1,
    //         name: "Is the earth flat?",
    //         description:
    //             "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec auctor, nisl eget ultricies tincidunt, nunc nisl aliquam nisl, eget aliquam nunc nisl euismod nunc. Donec auctor, nisl eget ultricies tincidunt, nunc nisl aliquam nisl, eget aliquam nunc nisl euismod nunc.",
    //         isCreatorFor: true,
    //         category: DebateCategory.SCIENCE,
    //         status: DebateStatus.OPEN,
    //         createdDate: new Date("2021-05-01"),
    //         subscriptionsCount: 3,
    //         votesFor: 2,
    //         votesAgainst: 1,
    //         creator: "user1",
    //         self: "",
    //         image: "",
    //         opponent: "",
    //         arguments: "",
    //         chats: "",
    //     },
    //     {
    //         id: 2,
    //         name: "The best programming language is Java",
    //         description:
    //             "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec auctor, nisl eget ultricies tincidunt, nunc nisl aliquam nisl, eget aliquam nunc nisl euismod nunc. Donec auctor, nisl eget ultricies tincidunt, nunc nisl aliquam nisl, eget aliquam nunc nisl euismod nunc.",
    //         isCreatorFor: false,
    //         category: DebateCategory.TECHNOLOGY,
    //         status: DebateStatus.OPEN,
    //         createdDate: new Date("2021-05-01"),
    //         subscriptionsCount: 5,
    //         votesFor: 2,
    //         votesAgainst: 3,
    //         creator: "user2",
    //         self: "",
    //         image: "",
    //         opponent: "",
    //         arguments: "",
    //         chats: "",
    //     },
    //     {
    //         id: 10,
    //         name: "The best programming language is ASM",
    //         description:
    //             "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec auctor, nisl eget ultricies tincidunt, nunc nisl aliquam nisl, eget aliquam nunc nisl euismod nunc. Donec auctor, nisl eget ultricies tincidunt, nunc nisl aliquam nisl, eget aliquam nunc nisl euismod nunc.",
    //         isCreatorFor: true,
    //         category: DebateCategory.TECHNOLOGY,
    //         status: DebateStatus.CLOSED,
    //         createdDate: new Date("2021-05-01"),
    //         subscriptionsCount: 50,
    //         votesFor: 1,
    //         votesAgainst: 20,
    //         creator: "user3",
    //         self: "",
    //         image: "",
    //         opponent: "",
    //         arguments: "",
    //         chats: "",
    //     },
    // ];

    const { t } = useTranslation();

    // const [debatesList, setDebatesList] = useState<DebateDto[]>([]);

    const {
        data: debatesList,
        loading: isLoading,
        getDebates: getDebates,
    } = useGetDebates();

    useEffect(() => {
        getDebates({
            order: DebateOrder.SUBS_DESC,
            status: "open",
            page: 0,
            size: 3,
        })
            .then(() => {
                console.log("Debates list loaded: ", debatesList);
            })
            .catch((error) => {
                console.log("Error loading debates list: ", error);
            });
    }, []);

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
