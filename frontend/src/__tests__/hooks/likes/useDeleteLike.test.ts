import { HttpStatusCode } from "axios";

import { renderHook } from "@testing-library/react";

import { useDeleteLike } from "../../../hooks/likes/useDeleteLike";
import * as useDeleteModule from "../../../hooks/requests/useDelete";

describe("useDeleteLike", () => {
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

        const { result } = renderHook(() => useDeleteLike());

        const { callDeleteLike } = result.current;
        const response = await callDeleteLike({
            likeUrl:
                "http://localhost:8080/paw-2022a-06/debates/1/arguments/1/likes/1",
        });

        expect(response).toEqual(mockResponse.status);
        expect(result.current.loading).toBe(false);
    });
});
