import { useState } from "react";

import { useTranslation } from "react-i18next";

import User from "../../types/User";
import DebateDto from "../../types/dto/DebateDto";

// VOTES SECTION
interface VotesSectionProps {
    debate?: DebateDto | null;
    userData?: User;
}

const VoteSection = ({ debate, userData }: VotesSectionProps) => {
    // TODO: View user vote logic
    const { t } = useTranslation();
    const [vote, setVote] = useState("for");

    const handleUserVoted = (vote: string) => {
        if (vote === "for") {
            if (debate?.isCreatorFor) return debate.creatorName;
            return debate?.opponentName;
        }
        if (debate?.isCreatorFor) return debate.opponentName;
        return debate?.creatorName;
    };

    const handleVoteButton = () => {
        // TODO: Implement vote call
    };

    const handleVoteResult = () => {
        const result = (debate?.votesFor || 0) - (debate?.votesAgainst || 0);
        if (result === 0) return t("debate.votes.draw");

        let winner: string | undefined;
        if (result > 0) {
            winner = debate?.isCreatorFor
                ? debate.creatorName
                : debate?.opponentName;
        } else {
            winner = debate?.isCreatorFor
                ? debate.opponentName
                : debate?.creatorName;
        }
        winner = winner ? winner : t("debate.userDeleted").toString();
        return t("debate.votes.winner") + winner;
    };

    return (
        <div className="card vote-section no-top-margin">
            <h5>{t("debate.votes.votes")}</h5>
            {userData &&
                !vote &&
                (debate?.status === t("debate.statuses.statusOpen") ||
                    debate?.status === t("debate.statuses.statusVoting") ||
                    debate?.status === t("debate.statuses.statusClosing")) && (
                    <>
                        <h5>{t("debate.votes.whoWins")}</h5>
                        <div className="vote-buttons">
                            <button
                                onClick={handleVoteButton}
                                className="btn waves-effect"
                            >
                                {debate?.creatorName}
                            </button>
                            <button
                                onClick={handleVoteButton}
                                className="btn waves-effect"
                            >
                                {debate.opponentName}
                            </button>
                        </div>
                    </>
                )}
            {vote && (
                <h6>
                    {t("debate.votes.voted")} {handleUserVoted(vote)}
                </h6>
            )}
            {((userData && vote) || !userData) && (
                <div className="progress red">
                    {(debate?.votesFor || 0) > 0 && (
                        <div
                            className="votes-format blue"
                            style={{ width: debate?.votesFor + "%" }}
                        >
                            <span>
                                {debate?.isCreatorFor
                                    ? debate.creatorName
                                    : debate?.opponentName}
                            </span>
                            <span>{debate?.votesFor}%</span>
                        </div>
                    )}
                    {(debate?.votesAgainst || 0) > 0 && (
                        <div
                            className="votes-format"
                            style={{ width: debate?.votesAgainst + "%" }}
                        >
                            <span>
                                {debate?.isCreatorFor
                                    ? debate.opponentName
                                    : debate?.creatorName}
                            </span>
                            <span>{debate?.votesAgainst}%</span>
                        </div>
                    )}
                </div>
            )}
            {debate?.status === t("debate.statuses.statusClosed") && (
                <h6>{handleVoteResult()}</h6>
            )}
            {userData &&
                vote &&
                (debate?.status === t("debate.statuses.statusOpen") ||
                    debate?.status === t("debate.statuses.statusVoting")) && (
                    <>
                        <h6>{t("debate.votes.changeVote")}</h6>
                        <button className="btn waves-effect">
                            {t("debate.votes.unvote")}
                        </button>
                    </>
                )}
            {(debate?.votesFor || 0) + (debate?.votesAgainst || 0) === 0 && (
                <h5 className="center">{t("debate.votes.noVotes")}</h5>
            )}
            {debate?.status === t("debate.statuses.statusVoting") && (
                <h6 className="center">
                    {t("debate.votes.votingEnds")} 17/02/2023
                </h6>
            )}
        </div>
    );
};

export default VoteSection;
