import { HttpStatusCode } from "axios";

import { renderHook } from "@testing-library/react";

import * as useGetModule from "../../../hooks/requests/useGet";
import { useGetSubscription } from "../../../hooks/subscriptions/useGetSubscription";

describe("useGetSubscription", () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    it("should return status code 200 when the response status is HttpStatusCode.Ok", async () => {
        const mockResponse = {
            status: HttpStatusCode.Ok,
        };
        jest.spyOn(useGetModule, "useGet").mockReturnValueOnce({
            loading: false,
            callGet: jest.fn().mockResolvedValueOnce(mockResponse),
        });

        const { result } = renderHook(() => useGetSubscription());

        const { getSubscription } = result.current;
        const response = await getSubscription({
            subscriptionUrl:
                "http://localhost:8080/paw-2022a-06/debates/1/subscriptions?user=test",
        });

        expect(response).toEqual(mockResponse.status);
        expect(result.current.loading).toBe(false);
    });
});
