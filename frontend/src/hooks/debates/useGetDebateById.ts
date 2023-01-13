import { useState } from "react";
import DebateDto from "../../types/dto/DebateDto";
import { useGet } from "../requests/useGet";
import { DEBATES_ENDPOINT } from "./constants";

export interface GetDebateByIdInput {
    id: number;
}

export const useGetDebateById = () => {
    const { loading, callGet } = useGet();
    const [data, setData] = useState<DebateDto | null>();

    async function getDebate(inData: GetDebateByIdInput) {
        const response = await callGet(DEBATES_ENDPOINT + inData.id);

        switch (response.status) {
            case 200:
                setData(response.data[0] as DebateDto);
                break;
            case 404:
                setData(response.data[0]?.message);
                break;
        }
    }

    return { loading, getDebate };
};
