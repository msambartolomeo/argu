import { HttpStatusCode } from "axios";

import { renderHook } from "@testing-library/react";

import * as usePutModule from "../../../hooks/requests/usePut";
import { useUpdateUserImage } from "../../../hooks/users/useUpdateUserImage";

describe("useUpdateUserImage", () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    it("should return the status when the response status is HttpStatusCode.Created", async () => {
        const mockResponse = {
            status: HttpStatusCode.Created,
            data: {
                message: "Image updated successfully",
            },
        };

        jest.spyOn(usePutModule, "usePut").mockReturnValueOnce({
            loading: false,
            callPut: jest.fn().mockResolvedValueOnce(mockResponse),
        });

        const { result } = renderHook(() => useUpdateUserImage());

        const { updateUserImage } = result.current;

        const response = await updateUserImage({
            imageUrl: "http://localhost:8080/paw-2022a-06/users/1/image",
            image: new File([], "image.png"),
        });

        expect(response).toEqual({
            status: HttpStatusCode.Created,
            message: "Image updated successfully",
        });
        expect(result.current.loading).toBe(false);
    });
});
