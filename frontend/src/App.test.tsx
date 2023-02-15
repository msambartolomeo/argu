import { render } from "@testing-library/react";

import App from "./App";

const mockEnqueue = jest.fn();

jest.mock("notistack", () => ({
    ...jest.requireActual("notistack"),
    useSnackbar: () => {
        return {
            enqueueSnackbar: mockEnqueue,
        };
    },
}));

test("renders learn react link", () => {
    window.history.pushState({}, "Test page", "/paw-2022a-06");
    const { getByTestId } = render(<App />);
    const linkElement = getByTestId("landing-page");
    expect(linkElement).toBeInTheDocument();
});
