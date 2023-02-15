import { NavLink } from "react-router-dom";

interface Props {
    text: string;
    to: string;
    icon: string;
}

function NavButton({ text, to, icon }: Props) {
    return (
        <li className="nav-item">
            <NavLink
                to={to}
                className={({ isActive }) => (isActive ? "activated" : "")}
            >
                <i className="material-icons left">{icon}</i>
                {text}
            </NavLink>
        </li>
    );
}

export default NavButton;
