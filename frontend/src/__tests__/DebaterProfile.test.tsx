import { AxiosResponse } from "axios";
import { t } from "i18next";
import { MemoryRouter } from "react-router-dom";

import { render, waitFor } from "@testing-library/react";

import {
    mockedDebate1,
    mockedDebate2,
    mockedDebate3,
    mockedUser,
} from "../__mocks__/mockedData";
import defaultProfilePhoto from "../assets/default-profile-photo.png";
import DebateListItem from "../components/DebateListItem/DebateListItem";
import { GetUserByUrlOutput } from "../hooks/users/useGetUserByUrl";
import { DebaterProfile } from "../views/DebaterProfile/DebaterProfile";

const mockUseGetUserByUrl = jest.fn();
const mockGetUserByUrl = jest.fn();

const mockUseGetUser = jest.fn();
const mockGetUser = jest.fn();

const mockUseGetUserImage = jest.fn();
const mockGetUserImage = jest.fn();

const mockUseGetDebatesByUrl = jest.fn();
const mockGetDebatesByUrl = jest.fn();

afterEach(() => {
    mockUseGetUserByUrl.mockClear();
    mockGetUserByUrl.mockClear();

    mockUseGetUser.mockClear();
    mockGetUser.mockClear();

    mockUseGetUserImage.mockClear();
    mockGetUserImage.mockClear();

    mockUseGetDebatesByUrl.mockClear();
    mockGetDebatesByUrl.mockClear();
});

test("Debater Profile with loading", async () => {
    jest.mock("../hooks/users/useGetUserByUsername", async () => ({
        useGetUserByUsername: () =>
            mockUseGetUser().mockReturnValue({
                loading: true,
                getUserByUsername: mockGetUser,
            }),
    }));

    const { getByTestId } = render(
        <MemoryRouter initialEntries={["/paw-2022a-06/user/test"]}>
            <DebaterProfile />
        </MemoryRouter>
    );

    waitFor(() => {
        expect(getByTestId("loading")).toBeInTheDocument();
    });
});

test("Debater Profile with no debates", async () => {
    jest.mock("../hooks/users/useGetUserByUrl", async () => ({
        useGetUserByUrl: () =>
            mockUseGetUserByUrl().mockReturnValue({
                loading: false,
                getUserByUrl: mockGetUserByUrl,
            }),
        getUserByUrl: () =>
            jest.fn().mockReturnValue(
                new Promise((resolve) =>
                    resolve({
                        status: 200,
                        data: mockedUser,
                    } as AxiosResponse)
                )
            ),
    }));

    jest.mock("../hooks/users/useGetUserByUsername", async () => ({
        useGetUserByUsername: () =>
            mockUseGetUser().mockReturnValue({
                loading: false,
                getUserByUsername: mockGetUser,
            }),
        getUserByUsername: () =>
            jest.fn().mockReturnValue({
                status: 200,
                data: mockedUser,
            } as GetUserByUrlOutput),
    }));

    jest.mock("../hooks/debates/useGetDebatesByUrl", async () => ({
        useGetDebatesByUrl: () =>
            mockUseGetDebatesByUrl().mockReturnValue({
                loading: false,
                getDebatesByUrl: mockGetDebatesByUrl,
            }),
        getDebatesByUrl: () =>
            jest.fn().mockReturnValue(
                new Promise((resolve) =>
                    resolve({
                        status: 204,
                    } as AxiosResponse)
                )
            ),
    }));

    jest.mock("../hooks/users/useGetUserImage", async () => ({
        useGetUserImage: () =>
            mockUseGetUserImage().mockReturnValue({
                loading: false,
                getUserImage: mockGetUserImage,
            }),
        getUserImage: () =>
            jest.fn().mockReturnValue(
                new Promise((resolve) =>
                    resolve({
                        status: 404,
                    } as AxiosResponse)
                )
            ),
    }));

    const { getByRole, getByText } = render(
        <MemoryRouter initialEntries={["/paw-2022a-06/user/test"]}>
            <DebaterProfile />
        </MemoryRouter>
    );
    waitFor(() => {
        expect(getByRole("heading", { level: 5 })).toHaveTextContent(
            t("components.debatesList.noDebates").toString()
        );
        expect(getByRole("image")).toHaveValue(defaultProfilePhoto);
        expect(getByText("test")).toBeInTheDocument();
    });
});

test("Debater Profile with debates", async () => {
    jest.mock("../hooks/users/useGetUserByUrl", async () => ({
        useGetUserByUrl: () =>
            mockUseGetUserByUrl().mockReturnValue({
                loading: false,
                getUserByUrl: mockGetUserByUrl,
            }),
        getUserByUrl: () =>
            jest.fn().mockReturnValue(
                new Promise((resolve) =>
                    resolve({
                        status: 200,
                        data: mockedUser,
                    } as AxiosResponse)
                )
            ),
    }));

    jest.mock("../hooks/users/useGetUserByUsername", async () => ({
        useGetUserByUsername: () =>
            mockUseGetUser().mockReturnValue({
                loading: false,
                getUserByUsername: mockGetUser,
            }),
        getUserByUsername: () =>
            jest.fn().mockReturnValue(
                new Promise((resolve) =>
                    resolve({
                        status: 200,
                        data: mockedUser,
                    } as AxiosResponse)
                )
            ),
    }));

    jest.mock("../hooks/users/useGetUserImage", async () => ({
        useGetUserImage: () =>
            mockUseGetUserImage().mockReturnValue({
                loading: false,
                getUserImage: mockGetUserImage,
            }),
        getUserImage: () =>
            jest.fn().mockReturnValue(
                new Promise((resolve) =>
                    resolve({
                        status: 404,
                    } as AxiosResponse)
                )
            ),
    }));

    jest.mock("../hooks/debates/useGetDebatesByUrl", async () => ({
        useGetDebatesByUrl: () =>
            mockUseGetDebatesByUrl().mockReturnValue({
                loading: false,
                getDebatesByUrl: mockGetDebatesByUrl,
            }),
        getDebatesByUrl: () =>
            jest.fn().mockReturnValue(
                new Promise((resolve) =>
                    resolve({
                        status: 200,
                        data: [mockedDebate1, mockedDebate2, mockedDebate3],
                    } as AxiosResponse)
                )
            ),
    }));

    const { getByTestId, getByRole, getByText } = render(
        <MemoryRouter initialEntries={["/paw-2022a-06/user/test"]}>
            <DebaterProfile />
        </MemoryRouter>
    );
    waitFor(() => {
        expect(getByTestId("debater-debates-list")).toBeInTheDocument();
        expect(getByTestId("debater-debates-list")).toContain(
            <DebateListItem debate={mockedDebate1} />
        );
        expect(getByTestId("debater-debates-list")).toContain(
            <DebateListItem debate={mockedDebate2} />
        );
        expect(getByTestId("debater-debates-list")).toContain(
            <DebateListItem debate={mockedDebate3} />
        );
        expect(getByRole("image")).toHaveValue(defaultProfilePhoto);
        expect(getByText("test")).toBeInTheDocument();
    });
});
