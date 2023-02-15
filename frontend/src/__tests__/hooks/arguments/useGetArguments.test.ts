import { HttpStatusCode } from "axios";

import { renderHook } from "@testing-library/react";

import {
    mockedArgument1,
    mockedArgument2,
} from "../../../__mocks__/mockedData";
import { useGetArguments } from "../../../hooks/arguments/useGetArguments";
import * as useGetModule from "../../../hooks/requests/useGet";

describe("useGetArguments", () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    it("should return status code in any case other than Ok", async () => {
        const mockArgumentsDto = [mockedArgument1, mockedArgument2];
        const mockResponse = {
            status: HttpStatusCode.NotFound,
        };
        jest.spyOn(useGetModule, "useGet").mockReturnValueOnce({
            loading: false,
            callGet: jest.fn().mockResolvedValueOnce({
                ...mockResponse,
            }),
        });

        const { result } = renderHook(() => useGetArguments());

        const { getArguments } = result.current;
        const argumentsResponse = await getArguments({
            argumentsUrl:
                "http://localhost:8080/paw-2022a-06/debates/1/arguments/",
            page: 0,
        });

        expect(argumentsResponse).toEqual({
            status: mockResponse.status,
        });
        expect(result.current.loading).toBe(false);
    });
});
