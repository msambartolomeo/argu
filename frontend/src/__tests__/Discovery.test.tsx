import { AxiosResponse } from "axios";
import { MemoryRouter } from "react-router-dom";

import { render, waitFor } from "@testing-library/react";

import Discovery from "../views/Discovery/Discovery";

const mockUseGetDebates = jest.fn();
const mockGetDebates = jest.fn();

test("show discovery page with no debates", async () => {
    jest.mock("../hooks/debates/useGetDebates", async () => ({
        useGetDebates: () =>
            mockUseGetDebates().mockReturnValue({
                loading: false,
                getDebates: mockGetDebates,
            }),
        getDebates: () =>
            jest.fn().mockReturnValue(
                new Promise((resolve) =>
                    resolve({
                        status: 204,
                        data: [],
                    } as AxiosResponse)
                )
            ),
    }));
    const { getByTestId } = render(
        <MemoryRouter initialEntries={["/paw-2022a-06/discover"]}>
            <Discovery />
        </MemoryRouter>
    );

    waitFor(() => {
        expect(getByTestId("no-debates-found")).toBeInTheDocument();
    });
});
