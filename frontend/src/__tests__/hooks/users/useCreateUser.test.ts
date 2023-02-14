import { HttpStatusCode } from "axios";

import { renderHook } from "@testing-library/react";

import * as usePostModule from "../../../hooks/requests/usePost";
import { useCreateUser } from "../../../hooks/users/useCreateUser";

describe("useCreateUser", () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    it("should return the user's location when the response status is HttpStatusCode.Created", async () => {
        const mockResponse = {
            status: HttpStatusCode.Created,
            headers: {
                location: "http://localhost:8080/paw-2022a-06/users/1",
            },
        };

        jest.spyOn(usePostModule, "usePost").mockReturnValueOnce({
            loading: false,
            callPost: jest.fn().mockResolvedValueOnce(mockResponse),
        });

        const { result } = renderHook(() => useCreateUser());

        const { createUser } = result.current;

        const response = await createUser({
            username: "test",
            email: "test@mail.com",
            password: "test",
        });

        expect(response).toEqual({
            status: mockResponse.status,
            location: mockResponse.headers.location,
        });

        expect(result.current.loading).toBe(false);
    });

    it("should return the errors when the response status is HttpStatusCode.BadRequest", async () => {
        const mockResponse = {
            status: HttpStatusCode.BadRequest,
            data: [
                {
                    message: "Invalid email",
                    messageCode: "invalidEmail",
                },
            ],
        };

        jest.spyOn(usePostModule, "usePost").mockReturnValueOnce({
            loading: false,
            callPost: jest.fn().mockResolvedValueOnce(mockResponse),
        });

        const { result } = renderHook(() => useCreateUser());

        const { createUser } = result.current;

        const response = await createUser({
            username: "test",
            email: "test@mail.com",
            password: "test",
        });

        expect(response).toEqual({
            status: mockResponse.status,
            errors: mockResponse.data,
        });

        expect(result.current.loading).toBe(false);
    });
});
