import { useState } from "react";

import { HttpStatusCode } from "axios";

import DebateDto from "../../types/dto/DebateDto";
import { useGet } from "../requests/useGet";

export interface GetDebatesByUrlInput {
    url: string;
}

export const useGetDebatesByUrl = () => {
    const [data, setData] = useState<DebateDto[]>([]);
    const { loading, callGet } = useGet();

    async function getDebatesByUrl({ url }: GetDebatesByUrlInput) {
        const response = await callGet(url, {}, false);

        if (response.status === HttpStatusCode.Ok) {
            setData(response.data as DebateDto[]);
        }
    }
    return { data, loading, getDebatesByUrl };
};
