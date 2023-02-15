import { HttpStatusCode } from "axios";

import { renderHook } from "@testing-library/react";

import * as usePostModule from "../../../hooks/requests/usePost";
import { useCreateSubscription } from "../../../hooks/subscriptions/useCreateSubscription";

describe("useCreateSubscription", () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    it("should return status code 201 when subscription created successfully", async () => {
        const mockResponse = {
            status: HttpStatusCode.Created,
        };
        jest.spyOn(usePostModule, "usePost").mockReturnValueOnce({
            loading: false,
            callPost: jest.fn().mockResolvedValueOnce(mockResponse),
        });

        const { result } = renderHook(() => useCreateSubscription());

        const { createSubscription } = result.current;
        const createSubscriptionResponse = await createSubscription({
            subscriptionUrl:
                "http://localhost:8080/paw-2022a-06/debates/1/subscriptions",
        });

        expect(createSubscriptionResponse).toEqual(mockResponse.status);
        expect(result.current.loading).toBe(false);
    });
});
