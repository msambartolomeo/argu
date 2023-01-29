import { usePatch } from "../requests/usePatch";
import { DEBATES_ENDPOINT } from "./constants";

export const useConcludeDebate = () => {
    const { loading, callPatch } = usePatch();

    async function concludeDebate(id: number) {
        const response = await callPatch(DEBATES_ENDPOINT + id, {}, true);
        return response.status;
    }

    return { loading, concludeDebate };
};
