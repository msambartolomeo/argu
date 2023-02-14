import { HttpStatusCode } from "axios";

import { renderHook } from "@testing-library/react";

import { useGetDebates } from "../../../hooks/debates/useGetDebates";
import * as useGetModule from "../../../hooks/requests/useGet";

describe("useGetDebates", () => {
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

        const { result } = renderHook(() => useGetDebates());

        const { getDebates } = result.current;

        const response = await getDebates({});

        expect(response).toEqual({
            status: mockResponse.status,
        });

        expect(result.current.loading).toBe(false);
    });
});
