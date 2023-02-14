import { HttpStatusCode } from "axios";

import { renderHook } from "@testing-library/react";

import { useDeleteArgument } from "../../../hooks/arguments/useDeleteArgument";
import * as usePatchModule from "../../../hooks/requests/usePatch";

describe("useDeleteArgument", () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    it("should return status code 204 when the argument is deleted successfully", async () => {
        const mockResponse = {
            status: HttpStatusCode.NoContent,
        };
        jest.spyOn(usePatchModule, "usePatch").mockReturnValueOnce({
            loading: false,
            callPatch: jest.fn().mockResolvedValueOnce(mockResponse),
        });

        const { result } = renderHook(() => useDeleteArgument());

        const { deleteArgument } = result.current;
        const deleteArgumentResponse = await deleteArgument(
            "http://localhost:8080/paw-2022a-06/debates/1/arguments/1"
        );

        expect(deleteArgumentResponse).toEqual(mockResponse.status);
        expect(result.current.loading).toBe(false);
    });
});
