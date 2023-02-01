// TODO: Complete missing fields
// TODO: Change image data type
import User from "./User";

export default interface Debate {
    id: number;
    name: string;
    description: string;
    isCreatorFor: boolean;
    createdDate: string;
    category: string;
    status: string;
    subscriptions: number;
    votesFor: number;
    votesAgainst: number;
    // TODO: Change to required data
    url?: string;
    imageURL?: string;
    creatorURL?: string;
    opponentURL?: string;
    argumentsURL?: string;
    // TODO: Delete when API calls are implemented
    creator: User;
    opponent?: User;
    image?: string;
}
