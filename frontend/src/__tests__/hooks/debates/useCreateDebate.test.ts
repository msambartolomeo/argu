import { HttpStatusCode } from "axios";

import { renderHook } from "@testing-library/react";

import { useCreateDebate } from "../../../hooks/debates/useCreateDebate";
import * as usePostModule from "../../../hooks/requests/usePost";
import DebateCategory from "../../../types/enums/DebateCategory";

describe("useCreateDebate", () => {
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

        const { result } = renderHook(() => useCreateDebate());

        const { createDebate } = result.current;

        const response = await createDebate({
            title: "Test title",
            description: "Test description",
            category: DebateCategory.POLITICS,
            isCreatorFor: true,
            opponentUsername: "TestOpponent",
        });

        expect(response).toEqual({
            status: mockResponse.status,
            location: mockResponse.headers.location,
        });
        expect(result.current.loading).toBe(false);
    });

    it("should return status code 400 when debate creation failed", async () => {
        const mockResponse = {
            status: HttpStatusCode.BadRequest,
            data: [
                {
                    code: "title",
                    message: "Title is required",
                },
            ],
        };
        jest.spyOn(usePostModule, "usePost").mockReturnValueOnce({
            loading: false,
            callPost: jest.fn().mockResolvedValueOnce(mockResponse),
        });

        const { result } = renderHook(() => useCreateDebate());

        const { createDebate } = result.current;

        const response = await createDebate({
            title: "",
            description: "Test description",
            category: DebateCategory.POLITICS,
            isCreatorFor: true,
            opponentUsername: "test2",
        });

        expect(response).toEqual({
            status: mockResponse.status,
            errors: mockResponse.data,
        });
        expect(result.current.loading).toBe(false);
    });

    it("should return status code in other cases", async () => {
        const mockResponse = {
            status: HttpStatusCode.Unauthorized,
        };

        jest.spyOn(usePostModule, "usePost").mockReturnValueOnce({
            loading: false,
            callPost: jest.fn().mockResolvedValueOnce(mockResponse),
        });

        const { result } = renderHook(() => useCreateDebate());

        const { createDebate } = result.current;

        const response = await createDebate({
            title: "Test title",
            description: "Test description",
            category: DebateCategory.POLITICS,
            isCreatorFor: true,
            opponentUsername: "test2",
        });

        expect(response).toEqual({
            status: mockResponse.status,
        });
        expect(result.current.loading).toBe(false);
    });
});
