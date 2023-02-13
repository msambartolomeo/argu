import { AxiosResponse } from "axios";
import { t } from "i18next";

import { fireEvent, render, waitFor } from "@testing-library/react";

import CreateDebate from "../views/CreateDebate/CreateDebate";

const mockUseCreateDebate = jest.fn();
const mockCreateDebate = jest.fn();

const mockEnqueue = jest.fn();

jest.mock("notistack", () => ({
    ...jest.requireActual("notistack"),
    useSnackbar: () => {
        return {
            enqueueSnackbar: mockEnqueue,
        };
    },
}));

const mockedUsedNavigate = jest.fn();

jest.mock("react-router-dom", () => ({
    ...jest.requireActual("react-router-dom"),
    useNavigate: () => mockedUsedNavigate,
}));

test("create debate without image successfully", async () => {
    jest.mock("../hooks/debates/useCreateDebate", async () => ({
        useCreateDebate: () =>
            mockUseCreateDebate().mockReturnValue({
                loading: false,
                createDebate: mockCreateDebate,
            }),
        createDebate: () =>
            jest.fn().mockReturnValue(
                new Promise((resolve) =>
                    resolve({
                        status: 201,
                    } as AxiosResponse)
                )
            ),
    }));
    const { getByTestId } = render(<CreateDebate />);

    const submitButton = getByTestId("submit-btn");

    fireEvent.click(submitButton);

    waitFor(() => {
        expect(mockCreateDebate).toHaveBeenCalled();
        expect(mockEnqueue).toHaveBeenCalled();
    });
});

test("create debate with image successfully", async () => {
    jest.mock("../hooks/debates/useCreateDebateWithImage", async () => ({
        useCreateDebateWithImage: () =>
            mockUseCreateDebate().mockReturnValue({
                loading: false,
                createDebate: mockCreateDebate,
            }),
        createDebateWithImage: () =>
            jest.fn().mockReturnValue(
                new Promise((resolve) =>
                    resolve({
                        status: 201,
                    } as AxiosResponse)
                )
            ),
    }));
    const { getByTestId } = render(<CreateDebate />);

    const submitButton = getByTestId("submit-btn");

    fireEvent.click(submitButton);

    waitFor(() => {
        expect(mockCreateDebate).toHaveBeenCalled();
        expect(mockEnqueue).toHaveBeenCalled();
    });
});

test("create debate with bad request", async () => {
    jest.mock("../hooks/debates/useCreateDebate", async () => ({
        useCreateDebate: () =>
            mockUseCreateDebate().mockReturnValue({
                loading: false,
                createDebate: mockCreateDebate,
            }),
        createDebate: () =>
            jest.fn().mockReturnValue(
                new Promise((resolve) =>
                    resolve({
                        status: 400,
                    } as AxiosResponse)
                )
            ),
    }));
    const { getByTestId, getByText } = render(<CreateDebate />);
    const submitButton = getByTestId("submit-btn");

    fireEvent.click(submitButton);

    waitFor(() => {
        expect(mockCreateDebate).toHaveBeenCalled();
        expect(
            getByText(
                t("createDebate.errors.opponentUsernameNotFound").toString()
            )
        ).toBeInTheDocument();
    });
});
