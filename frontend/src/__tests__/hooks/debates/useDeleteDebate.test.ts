import { HttpStatusCode } from "axios";

import { renderHook } from "@testing-library/react";

import { useDeleteDebate } from "../../../hooks/debates/useDeleteDebate";
import * as useDeleteModule from "../../../hooks/requests/useDelete";

describe("useDeleteDebate", () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    it("should return status code 204 when debate deleted successfully", async () => {
        const mockResponse = {
            status: HttpStatusCode.NoContent,
        };
        jest.spyOn(useDeleteModule, "useDelete").mockReturnValueOnce({
            loading: false,
            callDelete: jest.fn().mockResolvedValueOnce(mockResponse),
        });

        const { result } = renderHook(() => useDeleteDebate());

        const { deleteDebate } = result.current;

        const response = await deleteDebate(
            "http://localhost:8080/paw-2022a-06/debates/1"
        );

        expect(response).toEqual(mockResponse.status);
        expect(result.current.loading).toBe(false);
    });
});
