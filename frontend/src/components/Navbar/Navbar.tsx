import React, { useState } from "react";
import {
    BrowserRouter as Router,
    Routes,
    Route,
    Link,
    NavLink,
} from "react-router-dom";
import "./Navbar.css";
import { FaBars, FaTimes } from "react-icons/fa";
import { IconContext } from "react-icons/lib";
import arguLogo from "../../assets/argu-logo-2.jpeg";
import SearchBar from "../SearchBar/SearchBar";

// TODO: Internationalize
// TODO: Upgrade search bar

interface ArguLogoProps {
    image?: string;
}

const Navbar = ({ image = arguLogo }: ArguLogoProps) => {
    const [click] = useState(false);

    // const handleClick = () => setClick(!click);
    // const closeMobileMenu = () => setClick(false);

    return (
        <>
            <IconContext.Provider value={{ color: "#fff" }}>
                <nav className="navbar">
                    <div className="navbar-container container">
                        <Link to="/" className="navbar-logo">
                            <img
                                src={image}
                                alt="logo"
                                className="navbar-logo-img"
                            />
                        </Link>
                        <SearchBar placeholder="Search for a debate" />
                        <ul className={click ? "nav-menu active" : "nav-menu"}>
                            <li className="nav-item">
                                <NavLink
                                    to="/debates"
                                    className={({ isActive }) =>
                                        "nav-links" +
                                        (isActive ? " activated" : "")
                                    }
                                >
                                    Explore
                                </NavLink>
                            </li>
                            <li className="nav-item">
                                <NavLink
                                    to="/login"
                                    className={({ isActive }) =>
                                        "nav-links" +
                                        (isActive ? " activated" : "")
                                    }
                                >
                                    Login
                                </NavLink>
                            </li>
                            <li className="nav-item">
                                <NavLink
                                    to="/register"
                                    className={({ isActive }) =>
                                        "nav-links" +
                                        (isActive ? " activated" : "")
                                    }
                                >
                                    Register
                                </NavLink>
                            </li>
                        </ul>
                    </div>
                </nav>
            </IconContext.Provider>
        </>
    );
};

export default Navbar;
