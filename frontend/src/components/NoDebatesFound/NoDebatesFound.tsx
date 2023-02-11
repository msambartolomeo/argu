import { useTranslation } from "react-i18next";

import "./NoDebatesFound.css";

import "../../locales/index";
import "../ArgumentBubble/ArgumentBubble.css";

const NoDebatesFound = () => {
    const { t } = useTranslation();

    return (
        <div className="no-debates-found-container">
            <div className="speech-bubble sb-left speech-bubble-modified">
                <div className="comment-info">
                    <h6 className="comment-owner">
                        {t("discovery.noDebates.arguTeam")}:
                    </h6>
                </div>
                <div>
                    <p>{t("discovery.noDebates.arguTeamText")}</p>
                </div>
            </div>
            <div className="speech-bubble sb-right speech-bubble-modified">
                <div className="comment-info">
                    <h6 className="comment-owner">
                        {t("discovery.noDebates.arguCommunity")}:
                    </h6>
                </div>
                <div>
                    <p>{t("discovery.noDebates.arguCommunityText")}</p>
                </div>
            </div>
        </div>
    );
};

export default NoDebatesFound;
