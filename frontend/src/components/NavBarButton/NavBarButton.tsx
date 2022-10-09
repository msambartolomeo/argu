// NOTE: Take into account that the format of the button from navbar will be set by the parent component

interface NavBarButtonProps {
    name: string;
    path: string;
    icon: string;
}

const NavBarButton = ({ name, path, icon }: NavBarButtonProps) => {
    return (
        <li>
            <a href={path}>
                <i className="material-icons left">{icon}</i>
                {name}
            </a>
        </li>
    );
};

export default NavBarButton;
