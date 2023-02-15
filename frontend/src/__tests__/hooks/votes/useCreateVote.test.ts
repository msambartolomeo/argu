import { HttpStatusCode } from "axios";

import { renderHook } from "@testing-library/react";

import * as usePostModule from "../../../hooks/requests/usePost";
import { useCreateVote } from "../../../hooks/votes/useCreateVote";
import DebateVote from "../../../types/enums/DebateVote";

describe("useCreateVote", () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    it("should return status code 201 when the response status is HttpStatusCode.Created", async () => {
        const mockResponse = {
            status: HttpStatusCode.Created,
        };
        jest.spyOn(usePostModule, "usePost").mockReturnValueOnce({
            loading: false,
            callPost: jest.fn().mockResolvedValueOnce(mockResponse),
        });

        const { result } = renderHook(() => useCreateVote());

        const { createVote } = result.current;
        const response = await createVote({
            voteUrl:
                "http://localhost:8080/paw-2022a-06/debates/1/arguments/1/votes",
            type: DebateVote.FOR,
        });

        expect(response).toEqual(mockResponse.status);
        expect(result.current.loading).toBe(false);
    });
});
