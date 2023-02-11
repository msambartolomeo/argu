import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";

interface DebaterDisplayProps {
    debater?: string;
    position: string;
}

const DebaterDisplay = ({ debater, position }: DebaterDisplayProps) => {
    const navigate = useNavigate();
    const { t } = useTranslation();

    return (
        <div className="username-container">
            <h6>
                <b>{position}</b>
            </h6>
            {debater ? (
                <i
                    className="link"
                    onClick={() => navigate(`/user/${encodeURI(debater)}`)}
                >
                    {debater}
                </i>
            ) : (
                <i>{t("debate.userDeleted")}</i>
            )}
        </div>
    );
};

export default DebaterDisplay;
