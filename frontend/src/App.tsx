// NOTE: import base css
import "materialize-css/dist/css/materialize.min.css";
import "./root.css";

import logo from "./logo.svg";
import "./App.css";

import { useEffect } from "react";
import M from "materialize-css";
import SelectDropdown from "./components/SelectDropdown/SelectDropdown";

const suppliersList: { value: string, label: string }[] = [
    { value: "supplier1", label: "Supplier 1" },
    { value: "supplier2", label: "Supplier 2" },
    { value: "supplier3", label: "Supplier 3" },
];

function App() {
    useEffect(() => {
        M.AutoInit();
    }, []);

    return (
        <div className="App">
            <header className="App-header">
                <img src={logo} className="App-logo" alt="logo" />
                <p>
                    Edit <code>src/App.tsx</code> and save to reload.
                </p>
                <SelectDropdown suppliers={suppliersList} />
                <a
                    className="App-link"
                    href="https://reactjs.org"
                    target="_blank"
                    rel="noopener noreferrer"
                >
                    Learn React
                </a>
            </header>
        </div>
    );
}

export default App;
