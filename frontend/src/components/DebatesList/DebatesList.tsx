import { useTranslation } from "react-i18next";
import { useLocation } from "react-router-dom";

import "../../locales/index";
import DebateDto from "../../types/dto/DebateDto";
import DebateListItem from "../DebateListItem/DebateListItem";

interface DebatesListProps {
    debates: DebateDto[];
}

const DebatesList = ({ debates }: DebatesListProps) => {
    const { t } = useTranslation();

    if (!debates || debates.length === 0) {
        return <h5>{t("components.debatesList.noDebates")}</h5>;
    }

    return (
        <>
            {debates.map((debate: DebateDto) => (
                <DebateListItem debate={debate} key={debate.id} />
            ))}
        </>
    );
};

export default DebatesList;
