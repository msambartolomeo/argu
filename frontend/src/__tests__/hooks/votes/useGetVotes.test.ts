import { HttpStatusCode } from "axios";

import { renderHook } from "@testing-library/react";

import * as useGetModule from "../../../hooks/requests/useGet";
import { useGetVote } from "../../../hooks/votes/useGetVote";

describe("useGetVotes", () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    it("should return the votes when the response status is HttpStatusCode.Ok", async () => {
        const mockResponse = {
            status: HttpStatusCode.Ok,
            data: "for",
        };
        jest.spyOn(useGetModule, "useGet").mockReturnValueOnce({
            loading: false,
            callGet: jest.fn().mockResolvedValueOnce(mockResponse),
        });

        const { result } = renderHook(() => useGetVote());

        const { getVote } = result.current;
        const votes = await getVote({
            voteUrl:
                "http://localhost:8080/paw-2022a-06/debates/1/arguments/1/votes",
        });

        expect(votes).toEqual({
            status: HttpStatusCode.Ok,
            vote: "for",
        });
        expect(result.current.loading).toBe(false);
    });

    it("should return status code 404", async () => {
        const mockResponse = {
            status: HttpStatusCode.NotFound,
        };
        jest.spyOn(useGetModule, "useGet").mockReturnValueOnce({
            loading: false,
            callGet: jest.fn().mockResolvedValueOnce(mockResponse),
        });

        const { result } = renderHook(() => useGetVote());

        const { getVote } = result.current;
        const votes = await getVote({
            voteUrl:
                "http://localhost:8080/paw-2022a-06/debates/1/arguments/1/votes",
        });

        expect(votes).toEqual({ status: mockResponse.status });
        expect(result.current.loading).toBe(false);
    });
});
