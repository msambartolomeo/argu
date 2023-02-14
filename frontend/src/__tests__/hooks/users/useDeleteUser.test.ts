import { HttpStatusCode } from "axios";

import { renderHook } from "@testing-library/react";

import * as useDeleteModule from "../../../hooks/requests/useDelete";
import { useDeleteUser } from "../../../hooks/users/useDeleteUser";

describe("useDeleteUser", () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    it("should return the status and message when the response status is HttpStatusCode.NoContent", async () => {
        const mockResponse = {
            status: HttpStatusCode.NoContent,
            data: {
                message: "User deleted successfully",
            },
        };

        jest.spyOn(useDeleteModule, "useDelete").mockReturnValueOnce({
            loading: false,
            callDelete: jest.fn().mockResolvedValueOnce(mockResponse),
        });

        const { result } = renderHook(() => useDeleteUser());

        const { deleteUser } = result.current;

        const response = await deleteUser(
            "http://localhost:8080/paw-2022a-06/users/1"
        );

        expect(response).toEqual({
            status: mockResponse.status,
            message: mockResponse.data.message,
        });

        expect(result.current.loading).toBe(false);
    });
});
