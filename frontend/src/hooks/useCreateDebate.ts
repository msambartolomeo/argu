import DebateDto from "../types/dto/DebateDto";
import DebateCategory from "../types/enums/DebateCategory";
import { useRequestApi } from "./useRequestApi";

const API_URL = process.env.REACT_APP_API_ENDPOINT;

export interface CreateDebateInput {
    title: string;
    description: string;
    category: DebateCategory;
    isCreatorFor: boolean;
    opponentUsername: string;
}

const DEBATES_ENDPOINT = "debates";

export const useCreateDebate = () => {
    const { data, error, loading, requestApi } = useRequestApi();

    async function createDebate(
        inData: CreateDebateInput
    ): Promise<Array<DebateDto> | null> {
        // TODO: Properly throw errors
        if (inData.title.length > 100) {
            throw new Error("Title is too long");
        }
        if (inData.description.length > 280) {
            throw new Error("Description is too long");
        }
        if (inData.opponentUsername.length > 64) {
            throw new Error("Opponent URL is too long");
        }

        await requestApi(API_URL + DEBATES_ENDPOINT, "POST", inData, {
            "Content-Type": "application/json",
            Authorization: "Basic cGVwZTpwZXBl",
        });

        return null;
    }

    return { data, error, loading, createDebate };
};
