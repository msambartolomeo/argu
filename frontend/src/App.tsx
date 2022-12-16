// NOTE: import base css
import "materialize-css/dist/css/materialize.min.css";
import "./root.css";

import logo from "./logo.svg";
import "./App.css";

import { useEffect } from "react";
import M from "materialize-css";

// import router
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import LandingPage from "./views/LandingPage/LandingPage";
import Navbar from "./components/Navbar/Navbar";
import UserProfile from "./views/UserProfile/UserProfile";

function App() {
    useEffect(() => {
        M.AutoInit();
    }, []);

    return (
        <Router basename="/paw-2022a-06">
            <div className="App">
                <Navbar />
                <Routes>
                    <Route path="/" element={<LandingPage />} />

                    <Route // If :url == user.url, then show profile page
                        path="/user/:url"
                        element={
                            <div>
                                {/* TODO */}
                                <p>User</p>
                            </div>
                        }
                    />
                    <Route // Profile Page
                        path="/profile/"
                        element={<UserProfile />}
                    />

                    <Route
                        path="/debates"
                        element={
                            <div>
                                {/* TODO */}
                                <p>Debates</p>
                            </div>
                        }
                    />
                    <Route
                        path="/debates/:id"
                        element={
                            <div>
                                {/* TODO */}
                                <p>Debate with ID equal to :id</p>
                            </div>
                        }
                    />

                    <Route // Maybe this could be a modal, and avoid the extra page
                        path="/moderator"
                        element={
                            <div>
                                {/* TODO */}
                                <p>Moderator</p>
                            </div>
                        }
                    />

                    <Route
                        path="/create_debate"
                        element={
                            <div>
                                {/* TODO */}
                                <p>Create Debate</p>
                            </div>
                        }
                    />

                    <Route
                        path="/login"
                        element={
                            <div>
                                {/* TODO */}
                                <p>Login</p>
                            </div>
                        }
                    />
                    <Route
                        path="/register"
                        element={
                            <div>
                                {/* TODO */}
                                <p>Register</p>
                            </div>
                        }
                    />
                </Routes>
            </div>
        </Router>
    );
}

export default App;
