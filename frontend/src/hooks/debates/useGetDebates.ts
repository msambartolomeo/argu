import { HttpStatusCode } from "axios";

import { DEBATES_ENDPOINT } from "./constants";

import { PaginatedList } from "../../types/PaginatedList";
import DebateDto from "../../types/dto/DebateDto";
import DebateCategory from "../../types/enums/DebateCategory";
import DebateOrder from "../../types/enums/DebateOrder";
import DebateStatus from "../../types/enums/DebateStatus";
import { useGet } from "../requests/useGet";

export interface GetDebatesInput {
    search?: string;

    category?: DebateCategory;
    order?: DebateOrder;
    status?: DebateStatus;
    date?: string;
    recommendToDebate?: number;
    userUrl?: string;
    subscribed?: boolean;

    page?: number;
    size?: number;
}

export const useGetDebates = () => {
    const { loading, callGet } = useGet();

    async function getDebates(inData: GetDebatesInput) {
        const queryParams: Record<string, string> = {};
        if (inData) {
            for (const [key, value] of Object.entries(inData)) {
                if (value) {
                    queryParams[key] = value.toString();
                }
            }
        }

        const response = await callGet(
            DEBATES_ENDPOINT,
            {},
            false,
            queryParams
        );

        switch (response.status) {
            case HttpStatusCode.Ok:
                return {
                    status: response.status,
                    data: new PaginatedList<DebateDto>(
                        response.data as DebateDto[],
                        response.headers.link as string,
                        response.headers["x-total-pages"] as string
                    ),
                };
            default:
                return {
                    status: response.status,
                };
        }
    }
    return { loading, getDebates };
};
