import { AxiosResponse } from "axios";
import { t } from "i18next";
import { MemoryRouter } from "react-router-dom";

import { render, waitFor } from "@testing-library/react";

import {
    mockedDebate1,
    mockedDebate2,
    mockedDebate3,
} from "../__mocks__/mockedData";
import DebateListItem from "../components/DebateListItem/DebateListItem";
import LandingPage from "../views/LandingPage/LandingPage";

const mockUseGetDebates = jest.fn();
const mockGetDebates = jest.fn();

afterEach(() => {
    mockUseGetDebates.mockClear();
    mockGetDebates.mockClear();
});

test("show landing page with trending debates loading", async () => {
    jest.mock("../hooks/debates/useGetDebates", async () => ({
        useGetDebates: () =>
            mockUseGetDebates().mockReturnValue({
                loading: true,
                getDebates: mockGetDebates,
            }),
    }));
    const { getByTestId } = render(
        <MemoryRouter initialEntries={["/paw-2022a-06"]}>
            <LandingPage />
        </MemoryRouter>
    );

    expect(getByTestId("loading")).toBeInTheDocument();
});

test("show landing page with trending debates", async () => {
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
        <MemoryRouter initialEntries={["/paw-2022a-06"]}>
            <LandingPage />
        </MemoryRouter>
    );

    waitFor(() => {
        expect(getByTestId("trending-debates-list")).toBeInTheDocument();
        expect(getByTestId("trending-debates-list")).toContain(
            <DebateListItem debate={mockedDebate1} />
        );
        expect(getByTestId("trending-debates-list")).toContain(
            <DebateListItem debate={mockedDebate2} />
        );
        expect(getByTestId("trending-debates-list")).toContain(
            <DebateListItem debate={mockedDebate3} />
        );
    });
});

test("show landing page with no trending debates", async () => {
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

    const { getByRole } = render(
        <MemoryRouter initialEntries={["/paw-2022a-06"]}>
            <LandingPage />
        </MemoryRouter>
    );

    waitFor(() => {
        expect(getByRole("heading", { level: 5 })).toHaveTextContent(
            t("components.debatesList.noDebates").toString()
        );
    });
});
