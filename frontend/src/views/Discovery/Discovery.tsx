import { SelectChangeEvent } from "@mui/material";
import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import CategoryFilters from "../../components/CategoryFilters/CategoryFilters";
import DebatesList from "../../components/DebatesList/DebatesList";
import OrderByComponent from "../../components/OrderByComponent/OrderByComponent";
import DebateCategory from "../../types/enums/DebateCategory";
import Debate from "../../types/Debate";
import "./Discovery.css";
import CategorySelector from "../../components/CategoryButton/CategorySelector";
import DebateDto from "../../types/dto/DebateDto";
import DebateStatus from "../../types/enums/DebateStatus";
import { useTranslation } from "react-i18next";
import "../../locales/index";

const Discovery = () => {
    const { t } = useTranslation();

    document.title = "Argu | " + t("discovery.title");

    const debatesList: DebateDto[] = [
        {
            id: 1,
            name: "Is the earth flat?",
            description:
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec auctor, nisl eget ultricies tincidunt, nunc nisl aliquam nisl, eget aliquam nunc nisl euismod nunc. Donec auctor, nisl eget ultricies tincidunt, nunc nisl aliquam nisl, eget aliquam nunc nisl euismod nunc.",
            isCreatorFor: true,
            category: DebateCategory.SCIENCE,
            status: DebateStatus.OPEN,
            createdDate: new Date("2021-05-01"),
            subscriptionsCount: 3,
            votesFor: 2,
            votesAgainst: 1,
            creatorName: "user1",
            self: "",
            image: "",
            opponent: "",
            arguments: "",
            chats: "",
        },
        {
            id: 2,
            name: "The best programming language is Java",
            description:
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec auctor, nisl eget ultricies tincidunt, nunc nisl aliquam nisl, eget aliquam nunc nisl euismod nunc. Donec auctor, nisl eget ultricies tincidunt, nunc nisl aliquam nisl, eget aliquam nunc nisl euismod nunc.",
            isCreatorFor: false,
            category: DebateCategory.TECHNOLOGY,
            status: DebateStatus.OPEN,
            createdDate: new Date("2021-05-01"),
            subscriptionsCount: 5,
            votesFor: 2,
            votesAgainst: 3,
            creatorName: "user2",
            self: "",
            image: "",
            opponent: "",
            arguments: "",
            chats: "",
        },
    ];

    const search = useLocation().search;
    const selectedCategory = new URLSearchParams(search).get("category");

    const [queryOrder, setQueryOrder] = useState("");
    const [queryStatus, setQueryStatus] = useState("");
    const navigate = useNavigate();

    const handleSelectOrderChange = (event: SelectChangeEvent) => {
        setQueryOrder(event.target.value);
    };

    const handleSelectStatusChange = (event: SelectChangeEvent) => {
        setQueryStatus(event.target.value);
    };

    useEffect(() => {
        const queryParams = new URLSearchParams();
        if (selectedCategory) {
            queryParams.append("category", selectedCategory);
            if (queryOrder) {
                queryParams.append("order", queryOrder);
            }
            if (queryStatus) {
                queryParams.append("status", queryStatus);
            }
        } else {
            if (queryOrder) {
                queryParams.append("order", queryOrder);
            }
            if (queryStatus) {
                queryParams.append("status", queryStatus);
            }
        }
        navigate("/discover?" + queryParams.toString());
    }, [queryOrder, queryStatus, navigate]);

    const orderByProps = {
        queryOrder: queryOrder,
        handleSelectOrderChange: handleSelectOrderChange,
        queryStatus: queryStatus,
        handleSelectStatusChange: handleSelectStatusChange,
        selectedCategory: selectedCategory,
    };

    const categories = Object.values(DebateCategory);

    return (
        <div className="discovery-container">
            <div className="filters-container">
                <CategoryFilters selectedCategory={selectedCategory} />
            </div>
            <div className="debates-container">
                <div className="discovery-order-container">
                    <OrderByComponent {...orderByProps} />
                </div>
                <div className="debates-list-container">
                    <DebatesList debates={debatesList} />
                </div>
            </div>
        </div>
    );
};

export default Discovery;
