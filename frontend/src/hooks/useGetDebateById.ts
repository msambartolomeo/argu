import DebateCategory from "../model/enums/DebateCategory";
import { useFetch } from "./useFetch";

export interface GetDebateByIdIn {
    id: number;
}

export interface GetDebateByIdOut {
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

const DEBATES_ENDPOINT = "debates/";

export const useGetDebateById = () => {
    const { data, error, loading, fetchData } = useFetch();

    async function getDebate(
        inData: GetDebateByIdIn
    ): Promise<GetDebateByIdOut | null> {
        await fetchData(DEBATES_ENDPOINT + inData.id);

        if (!error && data) {
            return data as GetDebateByIdOut;
        }

        return null;
    }

    return { data, error, loading, getDebate };
};
