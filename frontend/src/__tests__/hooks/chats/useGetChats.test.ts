import { HttpStatusCode } from "axios";

import { renderHook } from "@testing-library/react";

import { useGetChats } from "../../../hooks/chats/useGetChats";
import * as useGetModule from "../../../hooks/requests/useGet";

describe("useGetChats", () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    it("should return the chats when the response status is HttpStatusCode.Ok", async () => {
        const mockResponse = {
            status: HttpStatusCode.NotFound,
        };
        jest.spyOn(useGetModule, "useGet").mockReturnValueOnce({
            loading: false,
            callGet: jest.fn().mockResolvedValueOnce(mockResponse),
        });

        const { result } = renderHook(() => useGetChats());

        const { getChats } = result.current;
        const chats = await getChats({
            chatUrl: "http://localhost:8080/paw-2022a-06/debates/1/chats",
        });

        expect(chats).toEqual({
            status: mockResponse.status,
        });
        expect(result.current.loading).toBe(false);
    });
});
