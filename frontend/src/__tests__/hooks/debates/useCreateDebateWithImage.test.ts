import { HttpStatusCode } from "axios";

import { renderHook } from "@testing-library/react";

import { useCreateDebateWithImage } from "../../../hooks/debates/useCreateDebateWithImage";
import * as usePostModule from "../../../hooks/requests/usePost";
import DebateCategory from "../../../types/enums/DebateCategory";

describe("useCreateDebateWithImage", () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    it("should return status code 201 when debate created successfully", async () => {
        const mockResponse = {
            status: HttpStatusCode.Created,
            headers: {
                location: "http://localhost:8080/paw-2022a-06/debates/1",
            },
        };
        jest.spyOn(usePostModule, "usePost").mockReturnValueOnce({
            loading: false,
            callPost: jest.fn().mockResolvedValueOnce(mockResponse),
        });

        const { result } = renderHook(() => useCreateDebateWithImage());

        const { createDebateWithImage } = result.current;

        const response = await createDebateWithImage({
            title: "Test title",
            description: "Test description",
            category: DebateCategory.POLITICS,
            isCreatorFor: true,
            opponentUsername: "test2",
            image: new File([], "test.png"),
        });

        expect(response).toEqual({
            status: mockResponse.status,
            location: mockResponse.headers.location,
        });
        expect(result.current.loading).toBe(false);
    });
});
