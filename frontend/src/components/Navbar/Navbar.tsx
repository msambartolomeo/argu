import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";

import NavButton from "./NavButton";
import "./Navbar.css";

import arguLogo from "../../assets/argu-logo-2.jpeg";
import { useSharedAuth } from "../../hooks/useAuth";
import "../../locales/index";
import UserRole from "../../types/enums/UserRole";
import SearchBar from "../SearchBar/SearchBar";

interface ArguLogoProps {
    image?: string;
}

// TODO: Add other buuttons depending if user is logged in

const Navbar = ({ image = arguLogo }: ArguLogoProps) => {
    const { t } = useTranslation();

    const { userInfo } = useSharedAuth();

    const searchBarPlaceholder: string = t("navbar.searchBar");

    return (
        <div className="navbar-fixed">
            <nav className="nav-bar">
                <div className="nav-wrapper">
                    <Link to="/" className="brand-logo">
                        <img src={image} alt="logo" className="logo-image" />
                    </Link>
                    <ul className="right">
                        <NavButton
                            text={t("navbar.explore")}
                            to="/discover"
                            icon="explore"
                        />
                        {userInfo ? (
                            <>
                                {userInfo.role === UserRole.MODERATOR ? (
                                    <NavButton
                                        text={t("navbar.createDebate")}
                                        to="/create-debate"
                                        icon="add"
                                    />
                                ) : (
                                    <NavButton
                                        text={t("navbar.moderator")}
                                        to="/moderator"
                                        icon="supervisor_account"
                                    />
                                )}
                                <NavButton
                                    text={t("navbar.profile")}
                                    to="/profile"
                                    icon="account_circle"
                                />
                            </>
                        ) : (
                            <>
                                <NavButton
                                    text={t("navbar.login")}
                                    to="/login"
                                    icon="check"
                                />
                                <NavButton
                                    text={t("navbar.register")}
                                    to="/register"
                                    icon="person_add_alt"
                                />
                            </>
                        )}
                    </ul>
                    <SearchBar placeholder={searchBarPlaceholder} />
                </div>
            </nav>
        </div>
    );
};

export default Navbar;
