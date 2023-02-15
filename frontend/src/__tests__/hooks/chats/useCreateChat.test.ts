import { HttpStatusCode } from "axios";

import { renderHook } from "@testing-library/react";

import { useCreateChat } from "../../../hooks/chats/useCreateChat";
import * as usePostModule from "../../../hooks/requests/usePost";

describe("useCreateChat", () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    it("should return the chat location when the response status is HttpStatusCode.Created", async () => {
        const mockResponse = {
            status: HttpStatusCode.Created,
            headers: {
                location: "http://localhost:8080/paw-2022a-06/debates/1/chats",
            },
        };
        jest.spyOn(usePostModule, "usePost").mockReturnValueOnce({
            loading: false,
            callPost: jest.fn().mockResolvedValueOnce({
                ...mockResponse,
            }),
        });

        const { result } = renderHook(() => useCreateChat());

        const { createChat } = result.current;
        const chatResponse = await createChat({
            chatUrl: "http://localhost:8080/paw-2022a-06/debates/1/chats",
            message: "test",
        });

        expect(chatResponse).toEqual({
            status: mockResponse.status,
            location: mockResponse.headers.location,
        });
        expect(result.current.loading).toBe(false);
    });

    it("should return status code in any case other than Created", async () => {
        const mockResponse = {
            status: HttpStatusCode.NotFound,
        };
        jest.spyOn(usePostModule, "usePost").mockReturnValueOnce({
            loading: false,
            callPost: jest.fn().mockResolvedValueOnce({
                ...mockResponse,
            }),
        });

        const { result } = renderHook(() => useCreateChat());

        const { createChat } = result.current;
        const chatResponse = await createChat({
            chatUrl: "http://localhost:8080/paw-2022a-06/debates/1/chats",
            message: "test",
        });

        expect(chatResponse).toEqual({
            status: mockResponse.status,
        });
        expect(result.current.loading).toBe(false);
    });
});
