import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";

import "./DebateListItem.css";

import defaultDebatePhoto from "../../assets/debate_stock.png";
import "../../locales/index";
import DebateDto from "../../types/dto/DebateDto";
import NonClickableChip from "../NonClickableChip/NonClickableChip";

interface DebateListItemProps {
    debate: DebateDto;
}

const DebateListItem = ({ debate }: DebateListItemProps) => {
    const { t } = useTranslation();
    const navigate = useNavigate();

    const debateCategory = t(`categories.${debate.category}`);
    const debateStatus = t(`debate.statuses.${debate.status}`);

    const handleNavigateToDebate = () => {
        navigate("/debate/" + debate.id);
    };

    return (
        <div className="list-item">
            <a
                className="card black-text hoverable"
                onClick={handleNavigateToDebate}
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
                                    : t("debate.userDeleted")}
                            </h6>
                        </div>
                        <div className="debate-footer">
                            <NonClickableChip name={debateCategory} />
                            <NonClickableChip
                                name={
                                    t("debate.created") +
                                    " " +
                                    debate.createdDate
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
                                debate.image ? debate.image : defaultDebatePhoto
                            }
                            defaultValue={defaultDebatePhoto}
                            alt={t("debate.debatePhotoAlt").toString()}
                            onError={({ target }) =>
                                ((target as HTMLImageElement).src =
                                    defaultDebatePhoto)
                            }
                            className="limit-img-sm responsive-img"
                        />
                    </div>
                </div>
            </a>
        </div>
    );
};

export default DebateListItem;
