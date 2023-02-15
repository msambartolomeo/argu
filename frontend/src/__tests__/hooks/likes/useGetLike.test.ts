import { HttpStatusCode } from "axios";

import { renderHook } from "@testing-library/react";

import { useGetLike } from "../../../hooks/likes/useGetLike";
import * as useGetModule from "../../../hooks/requests/useGet";

describe("useGetLike", () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    it("should return the like when the response status is HttpStatusCode.Ok", async () => {
        const mockResponse = {
            status: HttpStatusCode.Ok,
        };
        jest.spyOn(useGetModule, "useGet").mockReturnValueOnce({
            loading: false,
            callGet: jest.fn().mockResolvedValueOnce(mockResponse),
        });

        const { result } = renderHook(() => useGetLike());

        const { getLike } = result.current;
        const like = await getLike({
            likeUrl:
                "http://localhost:8080/paw-2022a-06/debates/1/arguments/1/likes/1",
            userUrl: "http://localhost:8080/paw-2022a-06/users/1",
        });

        expect(like).toEqual(HttpStatusCode.Ok);
        expect(result.current.loading).toBe(false);
    });
});
