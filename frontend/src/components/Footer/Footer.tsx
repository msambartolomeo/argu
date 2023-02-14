import "./Footer.css";

import i18n from "../../locales";
import SupportedLanguages from "../../types/enums/SupportedLanguages";

const Footer = () => {
    return (
        <div className="footer-component-container">
            <p>&copy; Copyright 2023, Argu</p>
            <div>
                {Object.values(SupportedLanguages).map((language) => (
                    <button
                        key={language}
                        onClick={() => i18n.changeLanguage(language)}
                    >
                        {language.toUpperCase()}
                    </button>
                ))}
            </div>
        </div>
    );
};

export default Footer;
