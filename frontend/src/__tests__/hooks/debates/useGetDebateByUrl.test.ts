import { HttpStatusCode } from "axios";

import { renderHook } from "@testing-library/react";

import { mockedArgument1 } from "../../../__mocks__/mockedData";
import { useGetDebateByUrl } from "../../../hooks/debates/useGetDebateByUrl";
import * as useGetModule from "../../../hooks/requests/useGet";

describe("useGetDebateByUrl", () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    it("should return the debate when the response status is HttpStatusCode.Ok", async () => {
        const mockResponse = {
            status: HttpStatusCode.Ok,
            data: mockedArgument1,
        };

        jest.spyOn(useGetModule, "useGet").mockReturnValueOnce({
            loading: false,
            callGet: jest.fn().mockResolvedValueOnce(mockResponse),
        });

        const { result } = renderHook(() => useGetDebateByUrl());

        const { getDebateByUrl } = result.current;

        const response = await getDebateByUrl({
            url: "http://localhost:8080/paw-2022a-06/debates/1",
        });

        expect(response).toEqual({
            status: mockResponse.status,
            data: mockResponse.data,
        });

        expect(result.current.loading).toBe(false);
    });

    it("should return the message when the response status is HttpStatusCode.NotFound", async () => {
        const mockResponse = {
            status: HttpStatusCode.NotFound,
            data: {
                message: "Debate not found",
            },
        };

        jest.spyOn(useGetModule, "useGet").mockReturnValueOnce({
            loading: false,
            callGet: jest.fn().mockResolvedValueOnce(mockResponse),
        });

        const { result } = renderHook(() => useGetDebateByUrl());

        const { getDebateByUrl } = result.current;

        const response = await getDebateByUrl({
            url: "http://localhost:8080/paw-2022a-06/debates/1",
        });

        expect(response).toEqual({
            status: mockResponse.status,
            message: mockResponse.data.message,
        });

        expect(result.current.loading).toBe(false);
    });
});
