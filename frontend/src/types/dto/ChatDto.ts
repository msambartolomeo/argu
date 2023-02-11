export default interface ChatDto {
    message: string;
    createdDate: string;
    creatorName?: string;

    self: string;
    creator?: string;
    debate: string;
}
