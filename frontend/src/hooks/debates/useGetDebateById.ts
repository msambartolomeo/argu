import { useState } from "react";
import DebateDto from "../../types/dto/DebateDto";
import { useGet } from "../requests/useGet";
import { DEBATES_ENDPOINT } from "./constants";
import { HttpStatusCode } from "axios";

export interface GetDebateByIdInput {
    id: number;
}

export const useGetDebateById = () => {
    const { loading, callGet } = useGet();
    const [data, setData] = useState<DebateDto | null>();

    async function getDebate(inData: GetDebateByIdInput) {
        const response = await callGet(DEBATES_ENDPOINT + inData.id);

        switch (response.status) {
            case HttpStatusCode.Ok:
                setData(response.data as DebateDto);
                break;
            case HttpStatusCode.NotFound:
                setData(response.data?.message);
                break;
        }
    }

    return { data, loading, getDebate };
};
