import { HttpStatusCode } from "axios";

import { renderHook } from "@testing-library/react";

import * as useDeleteModule from "../../../hooks/requests/useDelete";
import { useRemoveUserImage } from "../../../hooks/users/useRemoveUserImage";

describe("useRemoveUserImage", () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    it("should return the status when the response status is HttpStatusCode.Ok", async () => {
        const mockResponse = {
            status: HttpStatusCode.Ok,
        };

        jest.spyOn(useDeleteModule, "useDelete").mockReturnValueOnce({
            loading: false,
            callDelete: jest.fn().mockResolvedValueOnce(mockResponse),
        });

        const { result } = renderHook(() => useRemoveUserImage());

        const { removeUserImage } = result.current;

        const response = await removeUserImage(
            "http://localhost:8080/paw-2022a-06/users/1/image"
        );

        expect(response).toEqual(mockResponse.status);
        expect(result.current.loading).toBe(false);
    });
});
