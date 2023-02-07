import { usePatch } from "../requests/usePatch";

export const useDeleteArgument = () => {
    const { loading, callPatch } = usePatch();

    async function deleteArgument(argumentsURL: string): Promise<number> {
        const response = await callPatch(
            argumentsURL,
            {
                delete: true,
            },
            {},
            true
        );
        return response.status;
    }

    return { loading, deleteArgument };
};
