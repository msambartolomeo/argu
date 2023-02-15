import { HttpStatusCode } from "axios";

import { renderHook } from "@testing-library/react";

import * as usePatchModule from "../../../hooks/requests/usePatch";
import { useRequestModerator } from "../../../hooks/users/useRequestModerator";

describe("useRequestModerator", () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    it("should return the status when the response status is HttpStatusCode.Ok", async () => {
        const mockResponse = {
            status: HttpStatusCode.NoContent,
        };

        jest.spyOn(usePatchModule, "usePatch").mockReturnValueOnce({
            loading: false,
            callPatch: jest.fn().mockResolvedValueOnce(mockResponse),
        });

        const { result } = renderHook(() => useRequestModerator());

        const { requestModerator } = result.current;

        const response = await requestModerator({
            userUrl: "http://localhost:8080/paw-2022a-06/users/1",
            reason: "I want to be a moderator",
        });

        expect(response).toEqual(HttpStatusCode.NoContent);
        expect(result.current.loading).toBe(false);
    });
});
