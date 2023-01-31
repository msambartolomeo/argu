import { DEBATES_ENDPOINT } from "../debates/constants";

export const chatsEndpoint = (debateId: number) => {
    return DEBATES_ENDPOINT + `${debateId}/chats`;
};
