import DebateCategory from "../enums/DebateCategory";
import DebateStatus from "../enums/DebateStatus";

export default interface DebateDto {
    id: number;
    name: string;
    description: string;
    isCreatorFor: boolean;
    createdDate: string;
    dateToClose?: string;
    category: DebateCategory;
    status: DebateStatus;
    subscriptionsCount: number;
    votesFor: number;
    votesAgainst: number;
    creatorName: string;
    opponentName?: string;

    self: string;
    image?: string;
    creator?: string;
    opponent: string;
    arguments: string;
    chats: string;
    recommendations: string;
    sameCategory: string;
    sameStatus: string;
    afterSameDate: string;
    vote?: string;
    subscription?: string;
}
