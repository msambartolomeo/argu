import React from "react";

import { render, screen } from "@testing-library/react";

import App from "./App";

test("renders learn react link", () => {
    window.history.pushState({}, "Test page", "/paw-2022a-06");
    const { getByTestId } = render(<App />);
    const linkElement = getByTestId("landing-page");
    expect(linkElement).toBeInTheDocument();
});
