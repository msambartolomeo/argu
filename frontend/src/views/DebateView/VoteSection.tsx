import { useEffect, useState } from "react";

import { HttpStatusCode } from "axios";
import { useTranslation } from "react-i18next";

import { CircularProgress } from "@mui/material";

import { useSharedAuth } from "../../hooks/useAuth";
import { useCreateVote } from "../../hooks/votes/useCreateVote";
import { useDeleteVote } from "../../hooks/votes/useDeleteVote";
import { useGetVote } from "../../hooks/votes/useGetVote";
import DebateDto from "../../types/dto/DebateDto";
import DebateVote from "../../types/enums/DebateVote";

interface VotesSectionProps {
    debate: DebateDto;
    // TODO: Do not refresh everything
    refreshDebate: () => void;
}

const VoteSection = ({ debate, refreshDebate }: VotesSectionProps) => {
    const { t } = useTranslation();

    const { userInfo } = useSharedAuth();
    const [vote, setVote] = useState<DebateVote | null>(null);

    const { loading, getVote } = useGetVote();
    const { loading: postLoading, createVote } = useCreateVote();
    const { loading: deleteLoading, callDeleteVote } = useDeleteVote();

    const userFor = debate.isCreatorFor
        ? debate.creatorName
        : debate.opponentName;
    const userAgainst = debate.isCreatorFor
        ? debate.opponentName
        : debate.creatorName;

    useEffect(() => {
        if (userInfo) {
            getVote({ voteUrl: debate.vote as string }).then((output) => {
                switch (output.status) {
                    case HttpStatusCode.Ok:
                        setVote(output.vote?.vote ?? null);
                        break;
                }
            });
        }
    }, [userInfo]);

    const handleVoteButton = (type: DebateVote) => {
        createVote({ voteUrl: debate.vote as string, type }).then((output) => {
            switch (output) {
                case HttpStatusCode.Created:
                    refreshDebate();
                    break;
            }
        });
    };

    const handleUnvoteButton = () => {
        callDeleteVote({ voteUrl: debate.vote as string }).then((output) => {
            switch (output) {
                case HttpStatusCode.NoContent:
                    refreshDebate();
                    break;
            }
        });
    };

    const calculateVoteResult = () => {
        const result = (debate.votesFor || 0) - (debate.votesAgainst || 0);
        if (result === 0) return t("debate.votes.draw");

        let winner = result > 0 ? userFor : userAgainst;
        winner ??= t("debate.userDeleted").toString();
        return t("debate.votes.winner") + winner;
    };

    return (
        <div className="card vote-section no-top-margin">
            {loading && <CircularProgress size={100} />}
            <h5>{t("debate.votes.votes")}</h5>
            {userInfo &&
                (!vote ? (
                    (debate.status === t("debate.statuses.statusOpen") ||
                        debate.status === t("debate.statuses.statusVoting") ||
                        debate.status ===
                            t("debate.statuses.statusClosing")) && (
                        <>
                            <h5>{t("debate.votes.whoWins")}</h5>
                            <div className="vote-buttons">
                                <button
                                    onClick={() =>
                                        handleVoteButton(DebateVote.FOR)
                                    }
                                    className="btn waves-effect"
                                    disabled={postLoading}
                                >
                                    {userFor}
                                </button>
                                <button
                                    onClick={() =>
                                        handleVoteButton(DebateVote.AGAINST)
                                    }
                                    className="btn waves-effect"
                                    disabled={postLoading}
                                >
                                    {userAgainst}
                                </button>
                            </div>
                            {(debate.votesFor || 0) +
                                (debate.votesAgainst || 0) ===
                                0 && (
                                <h5 className="center">
                                    {t("debate.votes.noVotes")}
                                </h5>
                            )}
                        </>
                    )
                ) : (
                    <h6>
                        {t("debate.votes.voted")}{" "}
                        {vote === DebateVote.FOR ? userFor : userAgainst}
                    </h6>
                ))}
            {(!userInfo || vote) && (
                <div className="progress red">
                    {(debate.votesFor || 0) > 0 && (
                        <div
                            className="votes-format blue"
                            style={{ width: debate.votesFor + "%" }}
                        >
                            <span>
                                {debate.isCreatorFor
                                    ? debate.creatorName
                                    : debate.opponentName}
                            </span>
                            <span>{debate.votesFor}%</span>
                        </div>
                    )}
                    {(debate.votesAgainst || 0) > 0 && (
                        <div
                            className="votes-format"
                            style={{ width: debate.votesAgainst + "%" }}
                        >
                            <span>
                                {debate.isCreatorFor
                                    ? debate.opponentName
                                    : debate.creatorName}
                            </span>
                            <span>{debate.votesAgainst}%</span>
                        </div>
                    )}
                </div>
            )}
            {debate.status === t("debate.statuses.statusClosed") && (
                <h6>{calculateVoteResult()}</h6>
            )}
            {userInfo &&
                vote &&
                (debate.status === t("debate.statuses.statusOpen") ||
                    debate.status === t("debate.statuses.statusClosing") ||
                    debate.status === t("debate.statuses.statusVoting")) && (
                    <>
                        <h6>{t("debate.votes.changeVote")}</h6>
                        <button
                            className="btn waves-effect"
                            onClick={handleUnvoteButton}
                            disabled={deleteLoading}
                        >
                            {t("debate.votes.unvote")}
                        </button>
                    </>
                )}
            {debate.status === t("debate.statuses.statusVoting") && (
                <h6 className="center">
                    {t("debate.votes.votingEnds") + debate.dateToClose}
                </h6>
            )}
        </div>
    );
};

export default VoteSection;
