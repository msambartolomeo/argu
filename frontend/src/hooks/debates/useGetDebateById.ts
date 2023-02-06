import { HttpStatusCode } from "axios";

import { DEBATES_ENDPOINT } from "./constants";

import DebateDto from "../../types/dto/DebateDto";
import { useGet } from "../requests/useGet";

export interface GetDebateByIdInput {
    id: number;
}

export interface GetDebateByIdOutput {
    status: HttpStatusCode;
    data?: DebateDto;
    message?: string;
}

export const useGetDebateById = () => {
    const { loading, callGet } = useGet();

    async function getDebate(
        inData: GetDebateByIdInput
    ): Promise<GetDebateByIdOutput> {
        const response = await callGet(DEBATES_ENDPOINT + inData.id);

        switch (response.status) {
            case HttpStatusCode.Ok:
                return {
                    status: response.status,
                    data: response.data as DebateDto,
                };

            case HttpStatusCode.NotFound:
                return {
                    status: response.status,
                    message: response.data?.message,
                };
            default:
                return {
                    status: response.status,
                    message: response.data?.message,
                };
        }
    }

    return { loading, getDebate };
};
