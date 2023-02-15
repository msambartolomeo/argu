import { HttpStatusCode } from "axios";

import { renderHook } from "@testing-library/react";

import { useGetDebatesByUrl } from "../../../hooks/debates/useGetDebatesByUrl";
import * as useGetModule from "../../../hooks/requests/useGet";

describe("useGetDebatesByUrl", () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    it("should return status code in any other case (not Ok)", async () => {
        const mockResponse = {
            status: HttpStatusCode.NotFound,
        };

        jest.spyOn(useGetModule, "useGet").mockReturnValueOnce({
            loading: false,
            callGet: jest.fn().mockResolvedValueOnce(mockResponse),
        });

        const { result } = renderHook(() => useGetDebatesByUrl());

        const { getDebatesByUrl } = result.current;

        const response = await getDebatesByUrl({
            url: "http://localhost:8080/paw-2022a-06/debates/1",
        });

        expect(response).toEqual({
            status: mockResponse.status,
        });

        expect(result.current.loading).toBe(false);
    });
});
