import Debate from "./Debate";
import User from "./User";

// TODO: Check for URIs (creator, debate, image)
export default interface Argument {
    content: string;
    createdDate: string;
    status: string;
    likes: number;
    likedByUser: boolean;
    deleted: boolean;
    creator: User;
    debate: Debate;
    image?: string;
}
