import { HttpStatusCode } from "axios";

import { renderHook } from "@testing-library/react";

import * as useGetModule from "../../../hooks/requests/useGet";
import { useGetUserImage } from "../../../hooks/users/useGetUserImage";

describe("useGetUserImage", () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    it("should return the status when the response status is HttpStatusCode.Ok", async () => {
        const mockResponse = {
            status: HttpStatusCode.Ok,
        };

        jest.spyOn(useGetModule, "useGet").mockReturnValueOnce({
            loading: false,
            callGet: jest.fn().mockResolvedValueOnce(mockResponse),
        });

        const { result } = renderHook(() => useGetUserImage());

        const { getUserImage } = result.current;

        const response = await getUserImage(
            "http://localhost:8080/paw-2022a-06/users/1/image"
        );

        expect(response).toEqual(mockResponse.status);
        expect(result.current.loading).toBe(false);
    });
});
