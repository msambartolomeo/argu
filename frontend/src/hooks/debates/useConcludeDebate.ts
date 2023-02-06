import { DEBATES_ENDPOINT } from "./constants";

import { usePatch } from "../requests/usePatch";

export const useConcludeDebate = () => {
    const { loading, callPatch } = usePatch();

    async function concludeDebate(id: number) {
        const response = await callPatch(DEBATES_ENDPOINT + id, {}, true);
        return response.status;
    }

    return { loading, concludeDebate };
};
