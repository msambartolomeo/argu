import { useRef } from "react";
import { FaBars, FaTimes } from "react-icons/fa";
import "./NavBar.css";
import arguLogo from "../../assets/argu-logo-2.jpeg";

interface ArguLogoProps {
    image?: string;
}

const NavBar = ({ image = arguLogo }: ArguLogoProps) => {
    const navRef = useRef<null | HTMLDivElement>(null);

    const showNavBar = () => {
        navRef.current?.classList.toggle("responsive_nav");
    };

    return (
        <>
            <nav>
                <a href="/#">
                    <img src={image} alt="logo" />
                </a>
                <div>
                    <ul id="navbar">
                        <li>
                            <a href="/#">Explore</a>
                        </li>
                        <li>
                            <a href="/#">Login</a>
                        </li>
                        <li>
                            <a href="/#">Register</a>
                        </li>
                    </ul>
                </div>
            </nav>
        </>
        // <header>
        //     <h3>LOGO</h3>
        //     <nav ref={navRef}>
        //         <a href="/#">Home</a>
        //         <a href="/#">My work</a>
        //         <a href="/#">Blog</a>
        //         <a href="/#">About me</a>
        //         <button className="nav-btn nav-close-btn" onClick={showNavBar}>
        //             <FaTimes />
        //         </button>
        //     </nav>
        //     <button className="nav-btn" onClick={showNavBar}>
        //         <FaBars />
        //     </button>
        // </header>
    );
};

export default NavBar;
