import { AxiosResponse } from "axios";
import { MemoryRouter } from "react-router-dom";

import { act, fireEvent, render, waitFor } from "@testing-library/react";

import { mockedUser } from "../__mocks__/mockedData";
import Login from "../views/Login/Login";

const mockUseLogin = jest.fn();
const mockCallLogin = jest.fn();

test("login missing fields", async () => {
    jest.mock("../hooks/requests/useLogin", async () => ({
        useLogin: () =>
            mockUseLogin().mockReturnValue({
                loading: false,
                callLogin: mockCallLogin,
            }),
        callLogin: () =>
            jest.fn().mockReturnValue(
                new Promise((resolve) =>
                    resolve({
                        status: 400,
                        data: mockedUser,
                    } as AxiosResponse)
                )
            ),
    }));

    const { findByTestId } = render(
        <MemoryRouter>
            <Login />
        </MemoryRouter>
    );

    const username = await findByTestId("username");

    const password = await findByTestId("password");
    const submit = await findByTestId("submit-btn");

    await act(() => {
        fireEvent.change(username, { target: { value: "test" } });
        fireEvent.change(password, { target: { value: "" } });
        fireEvent.click(submit);
    });

    waitFor(() => {
        expect(mockCallLogin).not.toHaveBeenCalled();
    });
});

test("login successfully", async () => {
    jest.mock("../hooks/requests/useLogin", async () => ({
        useLogin: () =>
            mockUseLogin().mockReturnValue({
                loading: false,
                callLogin: mockCallLogin,
            }),
        callLogin: () =>
            jest.fn().mockReturnValue(
                new Promise((resolve) =>
                    resolve({
                        status: 200,
                        data: mockedUser,
                    } as AxiosResponse)
                )
            ),
    }));

    const { findByTestId } = render(
        <MemoryRouter>
            <Login />
        </MemoryRouter>
    );

    const username = await findByTestId("username");

    const password = await findByTestId("password");
    const submit = await findByTestId("submit-btn");

    act(() => {
        fireEvent.change(username, { target: { value: "test" } });
        fireEvent.change(password, { target: { value: "test" } });
        fireEvent.click(submit);
    });

    waitFor(() => {
        expect(mockCallLogin).toHaveBeenCalled();
    });
});
