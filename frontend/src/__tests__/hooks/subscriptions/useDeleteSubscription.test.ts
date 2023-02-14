import { HttpStatusCode } from "axios";

import { renderHook } from "@testing-library/react";

import * as useDeleteModule from "../../../hooks/requests/useDelete";
import { useDeleteSubscription } from "../../../hooks/subscriptions/useDeleteSubscription";

describe("useDeleteSubscription", () => {
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

        const { result } = renderHook(() => useDeleteSubscription());

        const { callDeleteSubscription } = result.current;
        const response = await callDeleteSubscription({
            subscriptionUrl:
                "http://localhost:8080/paw-2022a-06/debates/1/subscriptions/1",
        });

        expect(response).toEqual(mockResponse.status);
        expect(result.current.loading).toBe(false);
    });
});
