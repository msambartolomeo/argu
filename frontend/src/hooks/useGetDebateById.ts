import DebateDto from "../types/dto/DebateDto";
import { useRequestApi } from "./useRequestApi";

export interface GetDebateByIdInput {
    id: number;
}

const DEBATES_ENDPOINT = "debates/";

export const useGetDebateById = () => {
    const { data, error, loading, requestApi } = useRequestApi();

    async function getDebate(
        inData: GetDebateByIdInput
    ): Promise<DebateDto | null> {
        await requestApi(DEBATES_ENDPOINT + inData.id);

        if (!error && data) {
            return data as DebateDto;
        }

        return null;
    }

    return { data, error, loading, getDebate };
};
