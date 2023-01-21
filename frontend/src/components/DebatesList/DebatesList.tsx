import Debate from "../../types/Debate";
import DebateListItem from "../DebateListItem/DebateListItem";
import { useLocation } from "react-router-dom";
import "../../locales/index";
import { useTranslation } from "react-i18next";

interface DebatesListProps {
    debates: Debate[];
}

const DebatesList = ({ debates }: DebatesListProps) => {
    const { t } = useTranslation();

    const search = useLocation().search;
    const selectedCategory = new URLSearchParams(search).get("category");
    const selectedOrder = new URLSearchParams(search).get("order");
    const selectedStatus = new URLSearchParams(search).get("status");

    if (debates.length === 0) {
        return <h5>{t("components.debatesList.noDebates")}</h5>;
    }

    let debatesToShow = debates;

    if (selectedCategory) {
        debatesToShow = debatesToShow.filter(
            (debate) => debate.category === selectedCategory
        );
    }
    if (selectedStatus && selectedStatus !== "all") {
        debatesToShow = debatesToShow.filter(
            (debate) => debate.status === selectedStatus
        );
    }
    if (selectedOrder) {
        debatesToShow = debatesToShow.sort((a, b) => {
            if (selectedOrder === "date_desc") {
                return (
                    new Date(b.createdDate).getTime() -
                    new Date(a.createdDate).getTime()
                );
            } else if (selectedOrder === "date_asc") {
                return (
                    new Date(a.createdDate).getTime() -
                    new Date(b.createdDate).getTime()
                );
            } else if (selectedOrder === "alpha_asc") {
                return a.name.localeCompare(b.name);
            } else if (selectedOrder === "alpha_desc") {
                return b.name.localeCompare(a.name);
            } else if (selectedOrder === "subs_asc") {
                return a.subscriptions - b.subscriptions;
            }
            return b.subscriptions - a.subscriptions;
        });
    }

    return (
        <>
            {debatesToShow.map((debate: Debate) => (
                <DebateListItem debate={debate} key={debate.id} />
            ))}
        </>
    );
};

export default DebatesList;
