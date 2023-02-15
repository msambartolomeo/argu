import { BrowserRouter } from "react-router-dom";

import { render } from "@testing-library/react";

import { mockedDebate1 } from "../__mocks__/mockedData";
import DebateListItem from "../components/DebateListItem/DebateListItem";

describe("DebateListItem", () => {
    it("should render correctly", () => {
        const { container } = render(
            <DebateListItem debate={mockedDebate1} />,
            { wrapper: BrowserRouter }
        );

        expect(container).toHaveTextContent(mockedDebate1.name);
    });
});
