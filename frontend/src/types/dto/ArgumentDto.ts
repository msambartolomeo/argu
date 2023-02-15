import ArgumentStatus from "../enums/ArgumentStatus";

export default interface ArgumentDto {
    content: string;
    createdDate: string;
    status: ArgumentStatus;
    likes: number;
    likedByUser: boolean;
    deleted: boolean;
    creatorName: string;

    self: string;
    creator: string;
    debate: string;
    image?: string;
    like?: string;
}
