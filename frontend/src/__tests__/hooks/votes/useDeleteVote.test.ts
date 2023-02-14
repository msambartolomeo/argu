import { HttpStatusCode } from "axios";

import { renderHook } from "@testing-library/react";

import * as useDeleteModule from "../../../hooks/requests/useDelete";
import { useDeleteVote } from "../../../hooks/votes/useDeleteVote";

describe("useDeleteVote", () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    it("should return status code 204 when the response status is HttpStatusCode.NoContent", async () => {
        const mockResponse = {
            status: HttpStatusCode.NoContent,
        };
        jest.spyOn(useDeleteModule, "useDelete").mockReturnValueOnce({
            loading: false,
            callDelete: jest.fn().mockResolvedValueOnce(mockResponse),
        });

        const { result } = renderHook(() => useDeleteVote());

        const { callDeleteVote } = result.current;
        const response = await callDeleteVote({
            voteUrl:
                "http://localhost:8080/paw-2022a-06/debates/1/arguments/1/votes/1",
        });

        expect(response).toEqual(mockResponse.status);
        expect(result.current.loading).toBe(false);
    });
});
