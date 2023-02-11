import { usePatch } from "../requests/usePatch";

export const useConcludeDebate = () => {
    const { loading, callPatch } = usePatch();

    async function concludeDebate(url: string) {
        const response = await callPatch(
            url,
            {
                conclusion: true,
            },
            {},
            true
        );
        return response.status;
    }

    return { loading, concludeDebate };
};
