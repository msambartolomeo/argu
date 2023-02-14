import { HttpStatusCode } from "axios";

import { renderHook } from "@testing-library/react";

import { mockedDebate1 } from "../../../__mocks__/mockedData";
import { useGetDebateById } from "../../../hooks/debates/useGetDebateById";
import * as useGetDebateByUrlModule from "../../../hooks/debates/useGetDebateByUrl";

describe("useGetDebateById", () => {
    it("should return status code 200 when debate found", async () => {
        const mockResponse = {
            status: HttpStatusCode.Ok,
            data: mockedDebate1,
        };

        jest.spyOn(
            useGetDebateByUrlModule,
            "useGetDebateByUrl"
        ).mockReturnValueOnce({
            loading: false,
            getDebateByUrl: jest.fn().mockResolvedValueOnce(mockResponse),
        });

        const { result } = renderHook(() => useGetDebateById());

        const { getDebate } = result.current;

        const response = await getDebate({ id: 1 });

        expect(response).toEqual(mockResponse);
        expect(result.current.loading).toBe(false);
    });

    it("should return status code 404 when debate not found", async () => {
        const mockResponse = {
            status: HttpStatusCode.NotFound,
            message: "Debate not found",
        };

        jest.spyOn(
            useGetDebateByUrlModule,
            "useGetDebateByUrl"
        ).mockReturnValueOnce({
            loading: false,
            getDebateByUrl: jest.fn().mockResolvedValueOnce(mockResponse),
        });

        const { result } = renderHook(() => useGetDebateById());

        const { getDebate } = result.current;

        const response = await getDebate({ id: 1 });

        expect(response).toEqual(mockResponse);
        expect(result.current.loading).toBe(false);
    });
});
