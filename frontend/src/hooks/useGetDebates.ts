import DebateDto from "../types/dto/DebateDto";
import DebateCategory from "../types/enums/DebateCategory";
import DebateOrder from "../types/enums/DebateOrder";
import { useFetch } from "./useFetch";

const API_URL = process.env.REACT_APP_API_ENDPOINT;

export interface GetDebatesIn {
    search?: string;
    category?: DebateCategory;
    order?: DebateOrder;
    status?: string;
    date?: Date;
    recommendToDebate?: number;
    userUrl?: string;
    subscribed?: boolean;

    page?: number;
    size?: number;
}

const DEBATES_ENDPOINT = "debates";

export const useGetDebates = () => {
    const { data, error, loading, fetchData } = useFetch();

    async function getDebates(
        inData: GetDebatesIn
    ): Promise<Array<DebateDto> | null> {
        let endpoint = API_URL + DEBATES_ENDPOINT;

        if (inData) {
            endpoint += "?";
            for (const [key, value] of Object.entries(inData)) {
                if (value) {
                    endpoint += key + "=" + value + "&";
                }
            }
        }

        await fetchData(endpoint);

        if (error || !data) {
            return null;
        }

        return data as Array<DebateDto>;
    }

    return { data, error, loading, getDebates };
};
