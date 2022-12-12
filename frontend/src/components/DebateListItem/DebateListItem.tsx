import Debate from "../../model/types/Debate";
import NonClickableChip from "../NonClickableChip/NonClickableChip";
import "./DebateListItem.css";
import defaultDebatePhoto from "../../assets/debate_stock.png";
import { useTranslation } from "react-i18next";
import "../../locales/index";

interface DebateListItemProps {
    debate: Debate;
}

// TODO: Check if creator is received like a string or an URI
const DebateListItem = ({ debate }: DebateListItemProps) => {
    const { t } = useTranslation();

    return (
        <div className="list-item">
            <a
                className="card black-text hoverable"
                href={"/debates/" + debate.id}
            >
                <div className="card-content debate-info-holder">
                    <div className="debate-holder-separator">
                        <div className="debate-text-holder">
                            <h5 className="debate-title word-wrap">
                                {debate.name}
                            </h5>
                            <h6>
                                <b>{t("debate.creator")}:</b>{" "}
                                {debate.creator.username
                                    ? debate.creator.username
                                    : "[deleted user]"}
                            </h6>
                        </div>
                        <div className="debate-footer">
                            <NonClickableChip name={debate.category} />
                            <NonClickableChip
                                name={
                                    t("debate.created") +
                                    " " +
                                    debate.createdDate
                                }
                            />
                            <NonClickableChip
                                name={t("debate.status") + ": " + debate.status}
                            />
                            <NonClickableChip
                                name={
                                    t("debate.subscribedUsers") +
                                    ": " +
                                    debate.subscriptions
                                }
                            />
                        </div>
                    </div>
                    <div className="image-width">
                        <img
                            src={
                                debate.image
                                    ? "/images/" + debate.image
                                    : defaultDebatePhoto
                            }
                            alt="Debate photo"
                            className="limit-img-sm responsive-img"
                        />
                    </div>
                </div>
            </a>
        </div>
    );
};

export default DebateListItem;
