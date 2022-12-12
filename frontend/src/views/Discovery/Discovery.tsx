import { SelectContainer } from "react-select/dist/declarations/src/components/containers";
import CategoryFilters from "../../components/CategoryFilters/CategoryFilters";
import DatePicker from "../../components/DatePicker/DatePicker";
import DebatesList from "../../components/DebatesList/DebatesList";
import OrderByComponent from "../../components/OrderByComponent/OrderByComponent";
import Pagination from "../../components/Pagination/Pagination";
import SelectDropdown from "../../components/SelectDropdown/SelectDropdown";
import DebateCategory from "../../model/enums/DebateCategory";
import Debate from "../../model/types/Debate";
import "./Discovery.css";

const Discovery = () => {
    document.title = "Argu | Debates";

    const debatesList: Debate[] = [
        {
            id: 1,
            name: "Is the earth flat?",
            description:
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec auctor, nisl eget ultricies tincidunt, nunc nisl aliquam nisl, eget aliquam nunc nisl euismod nunc. Donec auctor, nisl eget ultricies tincidunt, nunc nisl aliquam nisl, eget aliquam nunc nisl euismod nunc.",
            isCreatorFor: true,
            category: DebateCategory.Science,
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
            category: DebateCategory.Technology,
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
    ];

    return (
        <div className="discovery-container">
            <div className="filters-container">
                <CategoryFilters />
            </div>
            <div className="debates-container">
                <div className="discovery-order-container">
                    <OrderByComponent />
                </div>
                <div className="debates-list-container">
                    <DebatesList debates={debatesList} />
                </div>
            </div>
        </div>
    );
};

export default Discovery;
