import DebateCategory from "../model/enums/DebateCategory";
import DebateOrder from "../model/enums/DebateOrder";
import { useFetch } from "./useFetch";

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

export interface GetDebatesOut {
    id: number;
    name: string;
    description: string;
    isCreatorFor: boolean;
    createdDate: Date;
    category: DebateCategory;
    status: string;
    subscriptions: number;
    votesFor: number;
    votesAgainst: number;

    self: string;
    image: string;
    creator: string;
    opponent: string;
    arguments: string;
    chats: string;
}

const DEBATES_ENDPOINT = "debates";

export const useGetDebates = () => {
    const { data, error, loading, fetchData } = useFetch();

    async function getDebates(
        inData: GetDebatesIn
    ): Promise<Array<GetDebatesOut> | null> {
        let endpoint = DEBATES_ENDPOINT;

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

        return data as Array<GetDebatesOut>;
    }

    return { data, error, loading, getDebates };
};
