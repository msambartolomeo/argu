import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";

import PostArgumentCard from "./PostArgumentCard";

import { useSharedAuth } from "../../hooks/useAuth";
import { PaginatedList } from "../../types/PaginatedList";
import ArgumentDto from "../../types/dto/ArgumentDto";
import DebateDto from "../../types/dto/DebateDto";

interface Props {
    debate: DebateDto;
    argumentList: PaginatedList<ArgumentDto>;
    setArgumentList: (list: PaginatedList<ArgumentDto>) => void;
}

function PostArgument({ debate, argumentList, setArgumentList }: Props) {
    const { t } = useTranslation();
    const navigate = useNavigate();

    const { userInfo } = useSharedAuth();

    const handlePostArgument = () => {
        // TODO: Implement post argument call
        setArgumentList;
    };

    return (
        <>
            {debate.status !== t("debate.statuses.statusClosed") &&
                debate.status !== t("debate.statuses.statusVoting") && (
                    <div className="card no-top-margin">
                        <div className="card-content">
                            {userInfo &&
                                (argumentList.data[-1]?.creatorName ===
                                userInfo.username ? (
                                    <PostArgumentCard
                                        handleSubmit={handlePostArgument}
                                        // TODO: get Real last argument from api as it may not be the final page
                                        lastArgument={argumentList.data[-1]}
                                        debateCreator={debate?.creatorName}
                                    />
                                ) : (
                                    <div className="card-title card-title-margins">
                                        {t("debate.waitTurn")}
                                    </div>
                                ))}
                            {!userInfo && (
                                <div className="card-title card-title-margins">
                                    {t("debate.needToLogin")}
                                    <i
                                        className="link"
                                        onClick={() =>
                                            navigate("/login", {
                                                state: {
                                                    from: window.location.pathname.substring(
                                                        13
                                                    ),
                                                },
                                            })
                                        }
                                    >
                                        {t("debate.firstLogin")}
                                    </i>
                                </div>
                            )}
                        </div>
                    </div>
                )}
        </>
    );
}

export default PostArgument;
