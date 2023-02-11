import { DEBATES_ENDPOINT } from "./constants";
import { GetDebateOutput, useGetDebateByUrl } from "./useGetDebateByUrl";

export interface GetDebateByIdInput {
    id: number;
}

export const useGetDebateById = () => {
    const { loading, getDebateByUrl: getDebateByUrl } = useGetDebateByUrl();

    async function getDebate({
        id,
    }: GetDebateByIdInput): Promise<GetDebateOutput> {
        return await getDebateByUrl({
            url: `${DEBATES_ENDPOINT}${id}`,
        });
    }

    return { loading, getDebate };
};
