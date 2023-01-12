import DebateCategory from "../../types/enums/DebateCategory";
import { usePost } from "../requests/usePost";
import { DEBATES_ENDPOINT } from "./constants";
import { useState } from "react";

export interface CreateDebateInput {
    title: string;
    description: string;
    category: DebateCategory;
    isCreatorFor: boolean;
    opponentUsername: string;
}

export const useCreateDebate = () => {
    const { loading, callPost } = usePost();
    const [data, setData] = useState<string>();

    async function createDebate(inData: CreateDebateInput): Promise<void> {
        const response = await callPost(
            DEBATES_ENDPOINT,
            inData,
            {
                "Content-Type": "application/json",
            },
            true
        );

        // TODO: Validate return objects
        switch (response.status) {
            case 400:
            case 404:
                setData(response.data[0]?.message);
                break;
            case 201: {
                const location = response.headers.location;
                if (location) {
                    setData(location);
                }
                break;
            }
        }
    }

    return { data, loading, createDebate };
};
