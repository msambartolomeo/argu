import DebateDto from "../types/dto/DebateDto";
import DebateCategory from "../types/enums/DebateCategory";
import DebateOrder from "../types/enums/DebateOrder";
import { useRequestApi } from "./useRequestApi";

const API_URL = process.env.REACT_APP_API_ENDPOINT;

export interface GetDebatesInput {
    search?: string;

    // TODO: Should we verify that the category is valid?
    category?: DebateCategory;
    order?: DebateOrder;
    status?: string;
    date?: Date;
    recommendToDebate?: number;
    userUrl?: string;
    subscribed?: boolean;

    page?: number;

    // TODO: Size < 10, should we verify this or just catch the error?
    size?: number;
}

const DEBATES_ENDPOINT = "debates";

export const useGetDebates = () => {
    const { data, error, loading, requestApi } = useRequestApi();

    async function getDebates(
        inData: GetDebatesInput
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

        await requestApi(endpoint);

        if (error || !data) {
            return null;
        }

        return data as Array<DebateDto>;
    }

    return { data, error, loading, getDebates };
};
