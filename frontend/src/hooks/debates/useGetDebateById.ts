import { HttpStatusCode } from "axios";

import DebateDto from "../../types/dto/DebateDto";
import { useGet } from "../requests/useGet";
import { DEBATES_ENDPOINT } from "./constants";

export interface GetDebateByIdInput {
    id: number;
}

export const useGetDebateById = () => {
    const { loading, callGet } = useGet();

    async function getDebate(inData: GetDebateByIdInput) {
        const response = await callGet(DEBATES_ENDPOINT + inData.id);

        switch (response.status) {
            case HttpStatusCode.Ok:
                return response.data as DebateDto;

            case HttpStatusCode.NotFound:
                return response.data?.message;
        }
    }

    return { loading, getDebate };
};
