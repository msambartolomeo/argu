import { USERS_ENDPOINT } from "./constants";

import { usePatch } from "../requests/usePatch";

export interface RequestModeratorInput {
    userUrl: string;
    reason: string;
}

export const useRequestModerator = () => {
    const { loading, callPatch } = usePatch();

    const requestModerator = async ({
        userUrl,
        reason,
    }: RequestModeratorInput) => {
        const response = await callPatch(
            USERS_ENDPOINT + encodeURI(userUrl),
            {
                reason: reason,
            },
            {
                "Content-Type": "application/json",
            },
            true
        );
        return response.status;
    };

    return { loading, requestModerator };
};
