import { useEffect, useState } from "react";

import { HttpStatusCode } from "axios";
import { useSnackbar } from "notistack";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";

import { CircularProgress } from "@mui/material";

import Chip from "../../../components/Chip/Chip";
import DebatePhoto from "../../../components/DebatePhoto/DebatePhoto";
import DeleteDialog from "../../../components/DeleteDialog/DeleteDialog";
import NonClickableChip from "../../../components/NonClickableChip/NonClickableChip";
import { useConcludeDebate } from "../../../hooks/debates/useConcludeDebate";
import { useDeleteDebate } from "../../../hooks/debates/useDeleteDebate";
import { useCreateSubscription } from "../../../hooks/subscriptions/useCreateSubscription";
import { useDeleteSubscription } from "../../../hooks/subscriptions/useDeleteSubscription";
import { useGetSubscription } from "../../../hooks/subscriptions/useGetSubscription";
import { useSharedAuth } from "../../../hooks/useAuth";
import DebateDto from "../../../types/dto/DebateDto";
import DebateStatus from "../../../types/enums/DebateStatus";
import DebaterDisplay from "../DebaterDisplay/DebaterDisplay";

interface Props {
    debate: DebateDto;
}

function DebateHeader({ debate }: Props) {
    const { t } = useTranslation();
    const { enqueueSnackbar } = useSnackbar();

    const { userInfo } = useSharedAuth();
    const navigate = useNavigate();

    const [subscribed, setSubscribed] = useState<boolean>(false);
    const [subscriptionCount, setSubscriptionCount] = useState<number>(
        debate.subscriptionsCount
    );
    const [status, setStatus] = useState<DebateStatus>(debate.status);

    const { loading: getSubLoading, getSubscription } = useGetSubscription();
    const { loading: subLoading, createSubscription: callSubscribe } =
        useCreateSubscription();
    const { loading: unsubLoading, callDeleteSubscription: callUnsubscribe } =
        useDeleteSubscription();
    const { deleteDebate } = useDeleteDebate();
    const { loading: concludeLoading, concludeDebate } = useConcludeDebate();

    useEffect(() => {
        if (userInfo) {
            getSubscription({
                subscriptionUrl: debate.subscription as string,
            }).then((code) => {
                switch (code) {
                    case HttpStatusCode.Ok:
                        setSubscribed(true);
                        break;
                    case HttpStatusCode.NotFound:
                        setSubscribed(false);
                        break;
                    default:
                        enqueueSnackbar(t("errors.unexpected"), {
                            variant: "error",
                        });
                }
            });
        }
    }, []);

    useEffect(() => {
        setSubscriptionCount(debate.subscriptionsCount);
        setStatus(debate.status);
    }, [debate]);

    const subscribe = () => {
        callSubscribe({
            subscriptionUrl: debate.subscription as string,
        }).then((code) => {
            switch (code) {
                case HttpStatusCode.Created:
                    setSubscriptionCount(subscriptionCount + 1);
                    setSubscribed(true);
                    break;
                default:
                    enqueueSnackbar(t("errors.unexpected"), {
                        variant: "error",
                    });
            }
        });
    };

    const unsubscribe = () => {
        callUnsubscribe({
            subscriptionUrl: debate.subscription as string,
        }).then((code) => {
            switch (code) {
                case HttpStatusCode.NoContent:
                    setSubscriptionCount(subscriptionCount - 1);
                    setSubscribed(false);
                    break;
                default:
                    enqueueSnackbar(t("errors.unexpected"), {
                        variant: "error",
                    });
            }
        });
    };

    const handleDeleteDebate = () => {
        deleteDebate(debate.self).then((code) => {
            switch (code) {
                case HttpStatusCode.NoContent:
                    enqueueSnackbar(t("debate.deleteSuccess"), {
                        variant: "success",
                    });
                    navigate("/");
                    break;
                default:
                    enqueueSnackbar(t("errors.unexpected"), {
                        variant: "error",
                    });
            }
        });
    };

    const handleConcludeDebate = () => {
        concludeDebate(debate.self).then((code) => {
            switch (code) {
                case HttpStatusCode.NoContent:
                    setStatus(DebateStatus.CLOSING);
                    break;
                default:
                    enqueueSnackbar(t("errors.unexpected"), {
                        variant: "error",
                    });
            }
        });
    };

    const navigateDiscoverUrl = (path?: string) => {
        const url = new URL(path as string);
        navigate({
            pathname: "/discover",
            search: url.search,
        });
    };

    return (
        <div className="card normalized-margins">
            {getSubLoading ? (
                <CircularProgress size={300} />
            ) : (
                <div className="card-content debate-info-holder">
                    <div className="debate-holder-separator">
                        <div className="debate-text-organizer">
                            <div className="debate-info-holder">
                                <h4 className="debate-title word-wrap">
                                    {debate.name}
                                </h4>
                                {userInfo && (
                                    <div className="right debate-buttons-display">
                                        <div className="col">
                                            {status === DebateStatus.OPEN &&
                                                (userInfo.username ===
                                                    debate.creatorName ||
                                                    userInfo.username ===
                                                        debate.opponentName) && (
                                                    <Chip
                                                        disabled={
                                                            concludeLoading
                                                        }
                                                        onClick={
                                                            handleConcludeDebate
                                                        }
                                                    >
                                                        {t("debate.close")}
                                                        <i className="material-icons right">
                                                            close
                                                        </i>
                                                    </Chip>
                                                )}
                                            {status !== DebateStatus.DELETED &&
                                                userInfo.username ===
                                                    debate.creatorName && (
                                                    <DeleteDialog
                                                        id="delete-debate"
                                                        handleDelete={
                                                            handleDeleteDebate
                                                        }
                                                        title={t(
                                                            "debate.deleteConfirmation"
                                                        )}
                                                        name={t(
                                                            "debate.delete"
                                                        )}
                                                    />
                                                )}
                                        </div>
                                    </div>
                                )}
                            </div>
                            <hr className="dashed" />
                            <h5 className="debate-description word-wrap">
                                {debate.description}
                            </h5>
                            {debate.creatorFor ? (
                                <>
                                    <DebaterDisplay
                                        debater={debate.creatorName}
                                        position={t("debate.for")}
                                    />
                                    <DebaterDisplay
                                        debater={debate.opponentName}
                                        position={t("debate.against")}
                                    />
                                </>
                            ) : (
                                <>
                                    <DebaterDisplay
                                        debater={debate.opponentName}
                                        position={t("debate.for")}
                                    />
                                    <DebaterDisplay
                                        debater={debate.creatorName}
                                        position={t("debate.against")}
                                    />
                                </>
                            )}
                        </div>
                        <div className="debate-footer">
                            {userInfo && !getSubLoading && (
                                <>
                                    {subscribed ? (
                                        <Chip
                                            disabled={unsubLoading}
                                            onClick={unsubscribe}
                                        >
                                            {t("debate.unsubscribe")}
                                            <i className="material-icons right">
                                                notifications_off
                                            </i>
                                        </Chip>
                                    ) : (
                                        <Chip
                                            disabled={subLoading}
                                            onClick={subscribe}
                                        >
                                            {t("debate.subscribe")}
                                            <i className="material-icons right">
                                                notifications_active
                                            </i>
                                        </Chip>
                                    )}
                                </>
                            )}
                            <Chip
                                onClick={() =>
                                    navigateDiscoverUrl(debate.sameCategory)
                                }
                            >
                                {t(`categories.${debate.category}`)}
                            </Chip>
                            <Chip
                                onClick={() =>
                                    navigateDiscoverUrl(debate.afterSameDate)
                                }
                            >
                                {debate.createdDate.toString()}
                            </Chip>
                            <Chip
                                onClick={() =>
                                    navigateDiscoverUrl(debate.sameStatus)
                                }
                            >
                                {t(`debate.statuses.${status}`)}
                            </Chip>
                            <NonClickableChip
                                name={
                                    t("debate.subscribed") + subscriptionCount
                                }
                            />
                        </div>
                    </div>
                    <DebatePhoto id={debate.image} alt="debate photo" />
                </div>
            )}
        </div>
    );
}

export default DebateHeader;
