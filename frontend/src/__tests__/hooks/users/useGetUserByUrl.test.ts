import { HttpStatusCode } from "axios";

import { renderHook } from "@testing-library/react";

import { mockedUser } from "../../../__mocks__/mockedData";
import * as useGetModule from "../../../hooks/requests/useGet";
import { useGetUserByUrl } from "../../../hooks/users/useGetUserByUrl";

describe("useGetUserByUrl", () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    it("should return the user when the response status is HttpStatusCode.Ok", async () => {
        const mockResponse = {
            status: HttpStatusCode.Ok,
            data: mockedUser,
        };

        jest.spyOn(useGetModule, "useGet").mockReturnValueOnce({
            loading: false,
            callGet: jest.fn().mockResolvedValueOnce(mockResponse),
        });

        const { result } = renderHook(() => useGetUserByUrl());

        const { getUserByUrl } = result.current;

        const response = await getUserByUrl({
            url: "http://localhost:8080/paw-2022a-06/users/1",
        });

        expect(response).toEqual({
            status: mockResponse.status,
            data: mockResponse.data,
        });

        expect(result.current.loading).toBe(false);
    });

    it("should return the status and message when the response status is not HttpStatusCode.Ok", async () => {
        const mockResponse = {
            status: HttpStatusCode.BadRequest,
            data: {
                message: "Bad request",
            },
        };

        jest.spyOn(useGetModule, "useGet").mockReturnValueOnce({
            loading: false,
            callGet: jest.fn().mockResolvedValueOnce(mockResponse),
        });

        const { result } = renderHook(() => useGetUserByUrl());

        const { getUserByUrl } = result.current;

        const response = await getUserByUrl({
            url: "http://localhost:8080/paw-2022a-06/users/1",
        });

        expect(response).toEqual({
            status: mockResponse.status,
            message: mockResponse.data.message,
        });

        expect(result.current.loading).toBe(false);
    });
});
