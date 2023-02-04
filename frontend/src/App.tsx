// NOTE: import base css
import M from "materialize-css";
import "materialize-css/dist/css/materialize.min.css";

import { useEffect } from "react";

// import router
import { Route, BrowserRouter as Router, Routes } from "react-router-dom";

import "./App.css";
import Footer from "./components/Footer/Footer";
import Navbar from "./components/Navbar/Navbar";
import { useSharedAuth } from "./hooks/useAuth";
import "./root.css";
import UserRole from "./types/enums/UserRole";
import CreateDebate from "./views/CreateDebate/CreateDebate";
import DebateView from "./views/DebateView/DebateView";
import { DebaterProfile } from "./views/DebaterProfile/DebaterProfile";
import Discovery from "./views/Discovery/Discovery";
import LandingPage from "./views/LandingPage/LandingPage";
import Login from "./views/Login/Login";
import Register from "./views/Register/Register";
import RequestModerator from "./views/RequestModerator/RequestModerator";
import UserProfile from "./views/UserProfile/UserProfile";

function App() {
    useEffect(() => {
        M.AutoInit();
    }, []);

    const { userInfo } = useSharedAuth();

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
                            <Route path="/discover" element={<Discovery />} />
                            <Route
                                path="/debate/:id"
                                element={<DebateView />}
                            />
                            {userInfo ? (
                                <>
                                    <Route // Profile Page
                                        path="/profile/"
                                        element={<UserProfile />}
                                    />
                                    {userInfo.role === UserRole.USER ? (
                                        <Route // Maybe this could be a modal, and avoid the extra page
                                            path="/moderator"
                                            element={<RequestModerator />}
                                        />
                                    ) : (
                                        <Route
                                            path="/create-debate"
                                            element={<CreateDebate />}
                                        />
                                    )}
                                </>
                            ) : (
                                <>
                                    <Route path="/login" element={<Login />} />
                                    <Route
                                        path="/register"
                                        element={<Register />}
                                    />
                                </>
                            )}
                            {/* TODO: Add error pages routing */}
                        </Routes>
                    </div>
                </Router>
            </div>
            <Footer />
        </div>
    );
}

export default App;
