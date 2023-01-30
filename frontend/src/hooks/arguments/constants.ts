import { DEBATES_ENDPOINT } from "../debates/constants";

export const argumentsEndpoint = (debateId: number) => {
    return DEBATES_ENDPOINT + `/${debateId}/arguments`;
};
