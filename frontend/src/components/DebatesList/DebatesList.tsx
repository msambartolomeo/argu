import Debate from "../../types/Debate";
import DebateListItem from "../DebateListItem/DebateListItem";
import "../../locales/index";
import { useTranslation } from "react-i18next";

interface DebatesListProps {
    debates: Debate[];
}

const DebatesList = ({ debates }: DebatesListProps) => {
    const { t } = useTranslation();

    if (debates.length === 0) {
        return <h5>{t("components.debatesList.noDebates")}</h5>;
    }

    return (
        <>
            {debates.map((debate: Debate) => (
                <DebateListItem debate={debate} key={debate.id} />
            ))}
        </>
    );
};

export default DebatesList;
