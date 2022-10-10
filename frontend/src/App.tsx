// NOTE: import base css
import "materialize-css/dist/css/materialize.min.css";
import "./root.css";

import logo from "./logo.svg";
import "./App.css";

import { useEffect } from "react";
import M from "materialize-css";

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
