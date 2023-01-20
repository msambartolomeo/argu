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

    self: string;
    image: string;
    creator: string;
    opponent: string;
    arguments: string;
    chats: string;
}
