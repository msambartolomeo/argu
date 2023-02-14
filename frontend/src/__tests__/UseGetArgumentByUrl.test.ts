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
            status: 200,
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
});
