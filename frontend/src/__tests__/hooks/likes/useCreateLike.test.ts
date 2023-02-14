import { HttpStatusCode } from "axios";

import { renderHook } from "@testing-library/react";

import { useCreateLike } from "../../../hooks/likes/useCreateLike";
import * as usePostModule from "../../../hooks/requests/usePost";

describe("useCreateLike", () => {
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

        const { result } = renderHook(() => useCreateLike());

        const { createLike } = result.current;
        const response = await createLike({
            likeUrl:
                "http://localhost:8080/paw-2022a-06/debates/1/arguments/1/likes",
        });

        expect(response).toEqual(mockResponse.status);
        expect(result.current.loading).toBe(false);
    });
});
