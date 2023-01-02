import DebateCategory from "../enums/DebateCategory";

export default interface DebateDto {
    id: number;
    name: string;
    description: string;
    isCreatorFor: boolean;
    createdDate: Date;
    category: DebateCategory;
    status: string;
    subscriptions: number;
    votesFor: number;
    votesAgainst: number;

    self: string;
    image: string;
    creator: string;
    opponent: string;
    arguments: string;
    chats: string;
}
