import { HttpStatusCode } from "axios";

import { renderHook } from "@testing-library/react";

import { mockedArgument1 } from "../__mocks__/mockedData";
import { useGetArgumentByUrl } from "../hooks/arguments/useGetArgumentByUrl";
import * as useGetModule from "../hooks/requests/useGet";

const mockedUsedNavigate = jest.fn();
jest.mock("react-router-dom", () => ({
    ...jest.requireActual("react-router-dom"),
    useNavigate: () => mockedUsedNavigate,
}));

describe("useGetArgumentByUrl", () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    it("should return the argument when the response status is HttpStatusCode.Ok", async () => {
        const mockArgumentDto = mockedArgument1;
        const mockResponse = {
            status: HttpStatusCode.Ok,
            data: mockArgumentDto,
        };
        jest.spyOn(useGetModule, "useGet").mockReturnValueOnce({
            loading: false,
            callGet: jest.fn().mockResolvedValueOnce(mockResponse),
        });

        const { result } = renderHook(() => useGetArgumentByUrl());

        const { getArgumentByUrl } = result.current;
        const argumentByUrl = await getArgumentByUrl({ url: "/test" });

        expect(argumentByUrl).toEqual({
            status: mockResponse.status,
            data: mockResponse.data,
        });
        expect(result.current.loading).toBe(false);
    });

    it("should return status code 404 with message Argument not found when the response status is NotFound", async () => {
        const mockResponse = {
            status: HttpStatusCode.NotFound,
            data: {
                message: "Argument not found",
            },
        };
        jest.spyOn(useGetModule, "useGet").mockReturnValueOnce({
            loading: false,
            callGet: jest.fn().mockResolvedValueOnce(mockResponse),
        });

        const { result } = renderHook(() => useGetArgumentByUrl());

        const { getArgumentByUrl } = result.current;
        const argumentByUrl = await getArgumentByUrl({ url: "/test" });

        expect(argumentByUrl).toEqual({
            status: mockResponse.status,
            message: mockResponse.data.message,
        });
        expect(result.current.loading).toBe(false);
    });
});
