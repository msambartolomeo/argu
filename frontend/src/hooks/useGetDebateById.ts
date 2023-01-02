import DebateDto from "../types/dto/DebateDto";
import { useFetch } from "./useFetch";

export interface GetDebateByIdIn {
    id: number;
}

const DEBATES_ENDPOINT = "debates/";

export const useGetDebateById = () => {
    const { data, error, loading, fetchData } = useFetch();

    async function getDebate(
        inData: GetDebateByIdIn
    ): Promise<DebateDto | null> {
        await fetchData(DEBATES_ENDPOINT + inData.id);

        if (!error && data) {
            return data as DebateDto;
        }

        return null;
    }

    return { data, error, loading, getDebate };
};
