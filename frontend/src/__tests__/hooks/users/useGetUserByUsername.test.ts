import { HttpStatusCode } from "axios";

import { renderHook } from "@testing-library/react";

import { mockedUser } from "../../../__mocks__/mockedData";
import * as useGetUserByUrlModule from "../../../hooks/users/useGetUserByUrl";

describe("useGetUserByUsername", () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    it("should return the user when the response status is HttpStatusCode.Ok", async () => {
        const mockResponse = {
            status: HttpStatusCode.Ok,
            data: mockedUser,
        };

        jest.spyOn(
            useGetUserByUrlModule,
            "useGetUserByUrl"
        ).mockReturnValueOnce({
            loading: false,
            getUserByUrl: jest.fn().mockResolvedValueOnce(mockResponse),
        });

        const { result } = renderHook(() =>
            useGetUserByUrlModule.useGetUserByUrl()
        );

        const { getUserByUrl } = result.current;

        const response = await getUserByUrl({
            url: "http://localhost:8080/paw-2022a-06/users/1",
        });

        expect(response).toEqual(mockResponse);
        expect(result.current.loading).toBe(false);
    });

    it("should return the status and message when the response status is not HttpStatusCode.Ok", async () => {
        const mockResponse = {
            status: HttpStatusCode.BadRequest,
            data: {
                message: "Bad request",
            },
        };

        jest.spyOn(
            useGetUserByUrlModule,
            "useGetUserByUrl"
        ).mockReturnValueOnce({
            loading: false,
            getUserByUrl: jest.fn().mockResolvedValueOnce(mockResponse),
        });

        const { result } = renderHook(() =>
            useGetUserByUrlModule.useGetUserByUrl()
        );

        const { getUserByUrl } = result.current;

        const response = await getUserByUrl({
            url: "http://localhost:8080/paw-2022a-06/users/1",
        });

        expect(response).toEqual(mockResponse);
        expect(result.current.loading).toBe(false);
    });
});
