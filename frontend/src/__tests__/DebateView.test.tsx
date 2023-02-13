import { AxiosResponse } from "axios";
import { t } from "i18next";
import { MemoryRouter } from "react-router-dom";

import { fireEvent, render, waitFor } from "@testing-library/react";

import {
    mockedArgument1,
    mockedArgument2,
    mockedArgument3,
    mockedDebate1,
    mockedDebate2,
    mockedDebate3,
    mockedUserInfo,
} from "../__mocks__/mockedData";
import error404image from "../assets/error404.png";
import ArgumentBubble from "../components/ArgumentBubble/ArgumentBubble";
import { GetDebateOutput } from "../hooks/debates/useGetDebateByUrl";
import DebateView from "../views/DebateView/DebateView";

const mockUseGetDebateByUrl = jest.fn();
const mockGetDebateByUrl = jest.fn();

const mockUseGetDebateById = jest.fn();
const mockGetDebateById = jest.fn();

const mockUseGetArguments = jest.fn();
const mockGetArguments = jest.fn();

const mockUseGetChats = jest.fn();
const mockGetChats = jest.fn();

const mockUseGetDebatesByUrl = jest.fn();
const mockGetDebatesByUrl = jest.fn();

const mockUseCreateArgument = jest.fn();
const mockCreateArgument = jest.fn();

const mockUseGetVote = jest.fn();
const mockGetVote = jest.fn();

const mockUseCreateVote = jest.fn();
const mockCreateVote = jest.fn();

afterEach(() => {
    mockUseGetDebateByUrl.mockClear();
    mockGetDebateByUrl.mockClear();

    mockUseGetDebateById.mockClear();
    mockGetDebateById.mockClear();

    mockUseGetArguments.mockClear();
    mockGetArguments.mockClear();

    mockUseGetChats.mockClear();
    mockGetChats.mockClear();

    mockUseGetDebatesByUrl.mockClear();
    mockGetDebatesByUrl.mockClear();

    mockUseCreateArgument.mockClear();
    mockCreateArgument.mockClear();

    mockUseGetVote.mockClear();
    mockGetVote.mockClear();

    mockUseCreateVote.mockClear();
    mockCreateVote.mockClear();
});

test("show debate view with debate", async () => {
    jest.mock("../hooks/debates/useGetDebateByUrl", async () => ({
        useGetDebateByUrl: () =>
            mockUseGetDebateByUrl().mockReturnValue({
                loading: false,
                getDebateByUrl: mockGetDebateByUrl,
            }),
        getDebateByUrl: () =>
            jest.fn().mockReturnValue(
                new Promise((resolve) =>
                    resolve({
                        status: 200,
                        data: mockedDebate1,
                    } as GetDebateOutput)
                )
            ),
    }));

    jest.mock("../hooks/debates/useGetDebateById", async () => ({
        useGetDebateById: () =>
            mockUseGetDebateById().mockReturnValue({
                loading: false,
                getDebateById: mockGetDebateById,
            }),
        getDebateById: () =>
            jest.fn().mockReturnValue(
                new Promise((resolve) =>
                    resolve({
                        status: 200,
                        data: mockedDebate1,
                    } as GetDebateOutput)
                )
            ),
    }));

    jest.mock("../hooks/arguments/useGetArguments", async () => ({
        useGetArguments: () =>
            mockUseGetArguments().mockReturnValue({
                loading: false,
                getArguments: mockGetArguments,
            }),
        getArguments: () =>
            jest.fn().mockReturnValue(
                new Promise((resolve) =>
                    resolve({
                        status: 200,
                        data: [
                            mockedArgument1,
                            mockedArgument2,
                            mockedArgument3,
                        ],
                    } as AxiosResponse)
                )
            ),
    }));

    jest.mock("../hooks/chats/useGetChats", async () => ({
        useGetChats: () =>
            mockUseGetChats().mockReturnValue({
                loading: false,
                getChats: mockGetChats,
            }),
        getChats: () =>
            jest.fn().mockReturnValue(
                new Promise((resolve) =>
                    resolve({
                        status: 204,
                        data: [],
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
                        data: [mockedDebate2, mockedDebate3],
                    } as AxiosResponse)
                )
            ),
    }));

    const { getByTestId } = render(
        <MemoryRouter initialEntries={["/paw-2022a-06/debate/1"]}>
            <DebateView />
        </MemoryRouter>
    );

    waitFor(() => {
        expect(getByTestId("debate-header")).toBeInTheDocument();
        expect(getByTestId("debate-header")).toContain(
            <h5>{mockedDebate1.name}</h5>
        );
        expect(getByTestId("debate-header")).toContain(
            <div>{t("debate.chat.noMessages")}</div>
        );
        expect(getByTestId("argument-list")).toContain(
            <ArgumentBubble
                argumentData={mockedArgument1}
                debateCreator={mockedArgument1.creatorName}
            />
        );
        expect(getByTestId("argument-list")).toContain(
            <ArgumentBubble
                argumentData={mockedArgument2}
                debateCreator={mockedArgument2.creatorName}
            />
        );
        expect(getByTestId("argument-list")).toContain(
            <ArgumentBubble
                argumentData={mockedArgument3}
                debateCreator={mockedArgument3.creatorName}
            />
        );
        expect(getByTestId("recommended-debates")).toBeInTheDocument();
    });
});

test("show debate view with debate but no recommended debates nor arguments", async () => {
    jest.mock("../hooks/debates/useGetDebateByUrl", async () => ({
        useGetDebateByUrl: () =>
            mockUseGetDebateByUrl().mockReturnValue({
                loading: false,
                getDebateByUrl: mockGetDebateByUrl,
            }),
        getDebateByUrl: () =>
            jest.fn().mockReturnValue(
                new Promise((resolve) =>
                    resolve({
                        status: 200,
                        data: mockedDebate1,
                    } as GetDebateOutput)
                )
            ),
    }));

    jest.mock("../hooks/debates/useGetDebateById", async () => ({
        useGetDebateById: () =>
            mockUseGetDebateById().mockReturnValue({
                loading: false,
                getDebateById: mockGetDebateById,
            }),
        getDebateById: () =>
            jest.fn().mockReturnValue(
                new Promise((resolve) =>
                    resolve({
                        status: 200,
                        data: mockedDebate1,
                    } as GetDebateOutput)
                )
            ),
    }));

    jest.mock("../hooks/arguments/useGetArguments", async () => ({
        useGetArguments: () =>
            mockUseGetArguments().mockReturnValue({
                loading: false,
                getArguments: mockGetArguments,
            }),
        getArguments: () =>
            jest.fn().mockReturnValue(
                new Promise((resolve) =>
                    resolve({
                        status: 204,
                        data: [],
                    } as AxiosResponse)
                )
            ),
    }));

    jest.mock("../hooks/chats/useGetChats", async () => ({
        useGetChats: () =>
            mockUseGetChats().mockReturnValue({
                loading: false,
                getChats: mockGetChats,
            }),
        getChats: () =>
            jest.fn().mockReturnValue(
                new Promise((resolve) =>
                    resolve({
                        status: 204,
                        data: [],
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
                        status: 204,
                        data: [],
                    } as AxiosResponse)
                )
            ),
    }));

    const { getByTestId, getByRole } = render(
        <MemoryRouter initialEntries={["/paw-2022a-06/debate/1"]}>
            <DebateView />
        </MemoryRouter>
    );

    waitFor(() => {
        expect(getByTestId("debate-header")).toBeInTheDocument();
        expect(getByTestId("debate-header")).toContain(
            <h5>{mockedDebate1.name}</h5>
        );
        expect(getByTestId("debate-header")).toContain(
            <div>{t("debate.chat.noMessages")}</div>
        );
        expect(
            getByRole("heading", {
                name: t("debate.noArguments").toString(),
                level: 5,
            })
        );

        expect(getByTestId("recommended-debates")).not.toBeInTheDocument();
    });
});

test("show debate view laoding", async () => {
    jest.mock("../hooks/debates/useGetDebateById", async () => ({
        useGetDebateById: () =>
            mockUseGetDebateById().mockReturnValue({
                loading: true,
                getDebateById: mockGetDebateById,
            }),
    }));

    const { getByTestId } = render(
        <MemoryRouter initialEntries={["/paw-2022a-06/debate/1"]}>
            <DebateView />
        </MemoryRouter>
    );

    waitFor(() => {
        expect(getByTestId("debate-loading")).toBeInTheDocument();
    });
});

test("show debate view not found", async () => {
    jest.mock("../hooks/debates/useGetDebateById", async () => ({
        useGetDebateById: () =>
            mockUseGetDebateById().mockReturnValue({
                loading: false,
                getDebateById: mockGetDebateById,
            }),
        getDebateById: () =>
            jest.fn().mockReturnValue(
                new Promise((resolve) =>
                    resolve({
                        status: 404,
                        data: "Debate not found",
                    } as AxiosResponse)
                )
            ),
    }));

    const { getByTestId, getByRole } = render(
        <MemoryRouter initialEntries={["/paw-2022a-06/debate/1"]}>
            <DebateView />
        </MemoryRouter>
    );

    waitFor(() => {
        expect(getByTestId("debate-not-found")).toBeInTheDocument();
        expect(getByRole("image")).toHaveValue(error404image);
    });
});

test("open modal when clicking on delete debate button", async () => {
    jest.mock("../hooks/debates/useGetDebateById", async () => ({
        useGetDebateById: () =>
            mockUseGetDebateById().mockReturnValue({
                loading: false,
                getDebateById: mockGetDebateById,
            }),
        getDebateById: () =>
            jest.fn().mockReturnValue(
                new Promise((resolve) =>
                    resolve({
                        status: 200,
                        data: mockedDebate1,
                    } as GetDebateOutput)
                )
            ),
    }));

    const { getByTestId, getByRole } = render(
        <MemoryRouter initialEntries={["/paw-2022a-06/debate/1"]}>
            <DebateView />
        </MemoryRouter>
    );

    waitFor(() => {
        expect(getByTestId("debate-header")).toBeInTheDocument();
        expect(getByTestId("debate-header")).toContain(
            <h5>{mockedDebate1.name}</h5>
        );
        expect(getByTestId("debate-header")).toContain(
            <div>{t("debate.chat.noMessages")}</div>
        );
        expect(
            getByRole("button", {
                name: t("debate.deleteConfirmation").toString(),
            })
        ).toBeInTheDocument();
        fireEvent.click(
            getByRole("button", {
                name: t("debate.deleteConfirmation").toString(),
            })
        );
        expect(getByTestId("delete-debate-modal")).toBeInTheDocument();
    });
});

test("post argument when clicking on post argument button", async () => {
    jest.mock("../hooks/useAuth", async () => ({
        useSharedAuth: () =>
            jest.fn().mockReturnValue({
                user: mockedUserInfo,
            }),
    }));

    jest.mock("../hooks/debates/useGetDebateById", async () => ({
        useGetDebateById: () =>
            mockUseGetDebateById().mockReturnValue({
                loading: false,
                getDebateById: mockGetDebateById,
            }),
        getDebateById: () =>
            jest.fn().mockReturnValue(
                new Promise((resolve) =>
                    resolve({
                        status: 200,
                        data: mockedDebate1,
                    } as GetDebateOutput)
                )
            ),
    }));

    jest.mock("../hooks/arguments/useCreateArgument", async () => ({
        usePostArgument: () =>
            mockUseCreateArgument().mockReturnValue({
                loading: false,
                postArgument: mockCreateArgument,
            }),
        postArgument: () =>
            jest.fn().mockReturnValue(
                new Promise((resolve) =>
                    resolve({
                        status: 201,
                        data: 1,
                    } as AxiosResponse)
                )
            ),
    }));

    const { getByTestId, getByRole } = render(
        <MemoryRouter initialEntries={["/paw-2022a-06/debate/1"]}>
            <DebateView />
        </MemoryRouter>
    );

    waitFor(() => {
        expect(getByTestId("debate-header")).toBeInTheDocument();
        expect(getByTestId("debate-header")).toContain(
            <h5>{mockedDebate1.name}</h5>
        );
        expect(getByTestId("debate-header")).toContain(
            <div>{t("debate.chat.noMessages")}</div>
        );
        expect(
            getByRole("button", {
                name: t("debate.argument.postIntro").toString(),
            })
        ).toBeInTheDocument();
        fireEvent.click(
            getByRole("button", {
                name: t("debate.argument.postIntro").toString(),
            })
        );
        expect(mockCreateArgument).toHaveBeenCalled();
    });
});

test("vote call triggered when clicking on vote button", async () => {
    jest.mock("../hooks/useAuth", async () => ({
        useSharedAuth: () =>
            jest.fn().mockReturnValue({
                user: mockedUserInfo,
            }),
    }));

    jest.mock("../hooks/debates/useGetDebateById", async () => ({
        useGetDebateById: () =>
            mockUseGetDebateById().mockReturnValue({
                loading: false,
                getDebateById: mockGetDebateById,
            }),
        getDebateById: () =>
            jest.fn().mockReturnValue(
                new Promise((resolve) =>
                    resolve({
                        status: 200,
                        data: mockedDebate1,
                    } as GetDebateOutput)
                )
            ),
    }));

    jest.mock("../hooks/votes/useGetVote", async () => ({
        useGetVote: () =>
            mockUseGetVote().mockReturnValue({
                loading: false,
                getVote: mockGetVote,
            }),
        getVote: () =>
            jest.fn().mockReturnValue(
                new Promise((resolve) =>
                    resolve({
                        status: 404,
                    } as AxiosResponse)
                )
            ),
    }));

    jest.mock("../hooks/votes/useCreateVote", async () => ({
        useCreateVote: () =>
            mockUseCreateVote().mockReturnValue({
                loading: false,
                createVote: mockCreateVote,
            }),
        createVote: () =>
            jest.fn().mockReturnValue(
                new Promise((resolve) =>
                    resolve({
                        status: 201,
                        data: 1,
                    } as AxiosResponse)
                )
            ),
    }));

    const { getByTestId, getByRole } = render(
        <MemoryRouter initialEntries={["/paw-2022a-06/debate/1"]}>
            <DebateView />
        </MemoryRouter>
    );

    waitFor(() => {
        expect(getByTestId("debate-header")).toBeInTheDocument();
        expect(getByTestId("debate-header")).toContain(
            <h5>{mockedDebate1.name}</h5>
        );
        expect(getByTestId("debate-header")).toContain(
            <div>{t("debate.chat.noMessages")}</div>
        );
        expect(
            getByRole("button", {
                name: t("debate.argument.vote").toString(),
            })
        ).toBeInTheDocument();
        fireEvent.click(
            getByRole("button", {
                name: "test",
            })
        );
        expect(mockCreateVote).toHaveBeenCalled();
    });
});
