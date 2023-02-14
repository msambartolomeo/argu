import { AxiosResponse } from "axios";
import { MemoryRouter } from "react-router-dom";

import { render, waitFor } from "@testing-library/react";

import {
    mockedDebate1,
    mockedDebate2,
    mockedDebate3,
} from "../__mocks__/mockedData";
import DebateListItem from "../components/DebateListItem/DebateListItem";
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

test("show discovery page with all debates", async () => {
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
                        status: 200,
                        data: [mockedDebate1, mockedDebate2, mockedDebate3],
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
        expect(getByTestId("discovery-debate-list")).toBeInTheDocument();
        expect(getByTestId("discovery-debate-list")).toContain(
            <DebateListItem debate={mockedDebate1} />
        );
        expect(getByTestId("discovery-debate-list")).toContain(
            <DebateListItem debate={mockedDebate2} />
        );
        expect(getByTestId("discovery-debate-list")).toContain(
            <DebateListItem debate={mockedDebate3} />
        );
    });
});

test("show discovery page with debates in category science", async () => {
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
                        status: 200,
                        data: [mockedDebate3],
                    } as AxiosResponse)
                )
            ),
    }));

    const { getByTestId } = render(
        <MemoryRouter
            initialEntries={["/paw-2022a-06/discover?category=science"]}
        >
            <Discovery />
        </MemoryRouter>
    );

    waitFor(() => {
        expect(getByTestId("discovery-debate-list")).toBeInTheDocument();
        expect(getByTestId("discovery-debate-list")).toContain(
            <DebateListItem debate={mockedDebate3} />
        );
    });
});

test("show discovery page with debates loading", async () => {
    jest.mock("../hooks/debates/useGetDebates", async () => ({
        useGetDebates: () =>
            mockUseGetDebates().mockReturnValue({
                loading: true,
                getDebates: mockGetDebates,
            }),
    }));
    const { getByTestId } = render(
        <MemoryRouter initialEntries={["/paw-2022a-06/discover"]}>
            <Discovery />
        </MemoryRouter>
    );

    waitFor(() => {
        expect(getByTestId("loading")).toBeInTheDocument();
    });
});
