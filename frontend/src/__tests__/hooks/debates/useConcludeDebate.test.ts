import { HttpStatusCode } from "axios";

import { renderHook } from "@testing-library/react";

import { useConcludeDebate } from "../../../hooks/debates/useConcludeDebate";
import * as usePatchModule from "../../../hooks/requests/usePatch";

describe("useConcludeDebate", () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    it("should return no content when conclude debate successfully", async () => {
        const mockResponse = {
            status: HttpStatusCode.NoContent,
        };
        jest.spyOn(usePatchModule, "usePatch").mockReturnValueOnce({
            loading: false,
            callPatch: jest.fn().mockReturnValue(mockResponse),
        });

        const { result } = renderHook(() => useConcludeDebate());

        const { concludeDebate } = result.current;

        const response = await concludeDebate(
            "http://localhost:8080/paw-2022a-06/debates/1"
        );

        expect(response).toEqual(mockResponse.status);
        expect(result.current.loading).toBe(false);
    });
});
