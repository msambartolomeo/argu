import Debate from "../../types/Debate";
import NonClickableChip from "../NonClickableChip/NonClickableChip";
import "./DebateListItem.css";
import defaultDebatePhoto from "../../assets/debate_stock.png";
import { useTranslation } from "react-i18next";
import "../../locales/index";
import DebateDto from "../../types/dto/DebateDto";

interface DebateListItemProps {
    debate: DebateDto;
}

// TODO: Check if creator is received like a string or an URI
const DebateListItem = ({ debate }: DebateListItemProps) => {
    const { t } = useTranslation();

    const debateCategory = t("discovery.categories." + debate.category);
    const debateStatus = t("debate.statuses." + debate.status);

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
                                {debate.creatorName
                                    ? debate.creatorName
                                    : "[deleted user]"}
                            </h6>
                        </div>
                        <div className="debate-footer">
                            <NonClickableChip name={debateCategory} />
                            <NonClickableChip
                                name={
                                    t("debate.created") +
                                    " " +
                                    debate.createdDate
                                        .toISOString()
                                        .split("T")[0]
                                }
                            />
                            <NonClickableChip
                                name={t("debate.status") + ": " + debateStatus}
                            />
                            <NonClickableChip
                                name={
                                    t("debate.subscribedUsers") +
                                    ": " +
                                    debate.subscriptionsCount
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
