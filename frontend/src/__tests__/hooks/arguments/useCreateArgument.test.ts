import { HttpStatusCode } from "axios";

import { renderHook } from "@testing-library/react";

import { useCreateArgument } from "../../../hooks/arguments/useCreateArgument";
import * as usePostModule from "../../../hooks/requests/usePost";

describe("useCreateArgument", () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    it("should return Created with location when argument created successfully", async () => {
        const mockResponse = {
            status: HttpStatusCode.Created,
            headers: {
                location:
                    "http://localhost:8080/paw-2022a-06/debates/1/arguments/1",
            },
        };
        jest.spyOn(usePostModule, "usePost").mockReturnValueOnce({
            loading: false,
            callPost: jest.fn().mockResolvedValueOnce(mockResponse),
        });

        const { result } = renderHook(() => useCreateArgument());

        const { createArgument } = result.current;
        const createArgumentResponse = await createArgument({
            argumentsURL:
                "http://localhost:8080/paw-2022a-06/debates/1/arguments",
            content: "test",
        });

        expect(createArgumentResponse).toEqual({
            status: mockResponse.status,
            location: mockResponse.headers.location,
        });
        expect(result.current.loading).toBe(false);
    });

    it("return status code in other cases", async () => {
        const mockResponse = {
            status: HttpStatusCode.NotFound,
        };
        jest.spyOn(usePostModule, "usePost").mockReturnValueOnce({
            loading: false,
            callPost: jest.fn().mockResolvedValueOnce(mockResponse),
        });

        const { result } = renderHook(() => useCreateArgument());

        const { createArgument } = result.current;
        const createArgumentResponse = await createArgument({
            argumentsURL:
                "http://localhost:8080/paw-2022a-06/debates/1/arguments",
            content: "argument test",
        });

        expect(createArgumentResponse).toEqual({
            status: mockResponse.status,
        });
        expect(result.current.loading).toBe(false);
    });
});
