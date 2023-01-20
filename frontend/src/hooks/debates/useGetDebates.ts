import { useState } from "react";
import DebateDto from "../../types/dto/DebateDto";
import DebateCategory from "../../types/enums/DebateCategory";
import DebateOrder from "../../types/enums/DebateOrder";
import { useGet } from "../requests/useGet";
import { DEBATES_ENDPOINT } from "./constants";

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

export const useGetDebates = () => {
    const [data, setData] = useState<DebateDto[]>([]);
    const { loading, callGet } = useGet();

    async function getDebates(inData: GetDebatesInput) {
        let endpoint = DEBATES_ENDPOINT;

        if (inData) {
            endpoint += "?";
            for (const [key, value] of Object.entries(inData)) {
                if (value) {
                    endpoint += key + "=" + value + "&";
                }
            }
        }

        const response = await callGet(endpoint, {}, false);

        if (response.status === 200) {
            setData(response.data as DebateDto[]);
        }
    }
    return { data, loading, getDebates };
};
