import { useState } from "react";

import { useTranslation } from "react-i18next";

import DebaterDisplay from "./DebaterDisplay";

import Chip from "../../components/Chip/Chip";
import DebatePhoto from "../../components/DebatePhoto/DebatePhoto";
import DeleteDialog from "../../components/DeleteDialog/DeleteDialog";
import NonClickableChip from "../../components/NonClickableChip/NonClickableChip";
import User from "../../types/User";
import DebateDto from "../../types/dto/DebateDto";

interface Props {
    debate?: DebateDto | null;
    userData?: User;
}

function DebateHeader({ debate, userData }: Props) {
    const { t } = useTranslation();

    const [subscribed, setSubscribed] = useState<boolean>(false);

    const handleCloseDebate = () => {
        // TODO: Implement close debate call
    };

    const handleDeleteDebate = () => {
        // TODO: Implement delete debate call
    };

    const handleSubscribe = () => {
        // TODO: Implement subscribe call
        // consider subscribed state for the call
    };

    return (
        <div className="card normalized-margins">
            <div className="card-content debate-info-holder">
                <div className="debate-holder-separator">
                    <div className="debate-text-organizer">
                        <div className="debate-info-holder">
                            <h4 className="debate-title word-wrap">
                                {debate?.name}
                            </h4>
                            {userData && (
                                <div className="right debate-buttons-display">
                                    <div className="col">
                                        {debate?.status ===
                                            t("debate.statuses.statusOpen") &&
                                            (userData?.username ===
                                                debate?.creatorName ||
                                                userData?.username ===
                                                    debate?.opponentName) && (
                                                <button
                                                    className="btn waves-effect chip"
                                                    onClick={handleCloseDebate}
                                                >
                                                    {t("debate.close")}
                                                    <i className="material-icons right">
                                                        close
                                                    </i>
                                                </button>
                                            )}
                                        {debate?.status !==
                                            t(
                                                "debate.statuses.statusDeleted"
                                            ) &&
                                            userData?.username ===
                                                debate?.creatorName && (
                                                <DeleteDialog
                                                    id="delete-debate"
                                                    handleDelete={
                                                        handleDeleteDebate
                                                    }
                                                    title={t(
                                                        "debate.deleteConfirmation"
                                                    )}
                                                    name={t("debate.delete")}
                                                />
                                            )}
                                    </div>
                                </div>
                            )}
                        </div>
                        <hr className="dashed" />
                        <h5 className="debate-description word-wrap">
                            {debate?.description}
                        </h5>
                        {debate?.isCreatorFor ? (
                            <>
                                <DebaterDisplay
                                    debater={debate?.creatorName}
                                    position={t("debate.for")}
                                />
                                <DebaterDisplay
                                    debater={debate?.opponentName}
                                    position={t("debate.against")}
                                />
                            </>
                        ) : (
                            <>
                                <DebaterDisplay
                                    debater={debate?.opponentName}
                                    position={t("debate.for")}
                                />
                                <DebaterDisplay
                                    debater={debate?.creatorName}
                                    position={t("debate.against")}
                                />
                            </>
                        )}
                    </div>
                    <div className="debate-footer">
                        {userData && (
                            <>
                                {subscribed ? (
                                    <button
                                        className="btn waves-effect chip"
                                        onClick={handleSubscribe}
                                    >
                                        {t("debate.unsubscribe")}
                                        <i className="material-icons right">
                                            notifications_off
                                        </i>
                                    </button>
                                ) : (
                                    <button
                                        className="btn waves-effect chip"
                                        onClick={handleSubscribe}
                                    >
                                        {t("debate.subscribe")}
                                        <i className="material-icons right">
                                            notifications_active
                                        </i>
                                    </button>
                                )}
                            </>
                        )}
                        <Chip name={debate?.category} />
                        <Chip name={debate?.createdDate.toString()} />
                        <Chip name={debate?.status} />
                        <NonClickableChip
                            name={
                                t("debate.subscribed") +
                                debate?.subscriptionsCount
                            }
                        />
                    </div>
                </div>
                <DebatePhoto id={undefined} alt="debate photo" />
            </div>
        </div>
    );
}

export default DebateHeader;
