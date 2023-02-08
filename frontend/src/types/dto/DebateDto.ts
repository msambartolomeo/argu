import DebateCategory from "../enums/DebateCategory";
import DebateStatus from "../enums/DebateStatus";

export default interface DebateDto {
    id: number;
    name: string;
    description: string;
    isCreatorFor: boolean;
    createdDate: Date;
    category: DebateCategory;
    status: DebateStatus;
    subscriptionsCount: number;
    votesFor: number;
    votesAgainst: number;
    creatorName: string;
    opponentName?: string;

    self: string;
    image: string;
    creator?: string;
    opponent: string;
    arguments: string;
    chats: string;
    // TODO: Remove question mark from the following 4, cant do it now or else user profile breaks
    recommendations?: string;
    sameCategory?: string;
    sameStatus?: string;
    afterSameDate?: string;
    vote?: string;
    subscription?: string;
}
