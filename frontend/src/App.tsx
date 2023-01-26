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
import RequestModerator from "./views/RequestModerator/RequestModerator";
import Navbar from "./components/Navbar/Navbar";
import Login from "./views/Login/Login";
import UserProfile from "./views/UserProfile/UserProfile";
import Register from "./views/Register/Register";
import CreateDebate from "./views/CreateDebate/CreateDebate";
import Discovery from "./views/Discovery/Discovery";
import { DebaterProfile } from "./views/DebaterProfile/DebaterProfile";
import Footer from "./components/Footer/Footer";

function App() {
    useEffect(() => {
        M.AutoInit();
    }, []);

    return (
        <div className="page-container">
            <div className="content-wrap">
                <Router basename="/paw-2022a-06">
                    <div className="App">
                        <Navbar />
                        <Routes>
                            <Route path="/" element={<LandingPage />} />

                    <Route // If :url == user.url, then show profile page
                        path="/user/:url"
                        element={<DebaterProfile />}
                    />
                    <Route // Profile Page
                        path="/profile/"
                        element={<UserProfile />}
                    />

                            <Route path="/discover" element={<Discovery />} />
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
                                element={<RequestModerator />}
                            />

                            <Route
                                path="/create_debate"
                                element={<CreateDebate />}
                            />

                            <Route path="/login" element={<Login />} />
                            <Route path="/register" element={<Register />} />
                        </Routes>
                    </div>
                </Router>
            </div>
            <Footer />
        </div>
    );
}

export default App;
