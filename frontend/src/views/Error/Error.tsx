import "./Error.css";
import error400 from "../../assets/error400.png";
import error404 from "../../assets/error404.png";
import error500 from "../../assets/error500.png";

import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";

interface ErrorProps {
    status: number;
    message: string;
}

export const Error = ({ status, message }: ErrorProps) => {
    const { t } = useTranslation();

    let image = "";
    switch (status) {
        case 400:
            image = error400;
            break;
        case 404:
            image = error404;
            break;
        default:
            image = error500;
            break;
    }

    return (
        <div className="error-container">
            <div className="error-div">
                <img src={image} alt="Error 404" />
                <h1>{t("error.title")}</h1>
                <h6>{message}</h6>
                <Link to={"/"} className="link-underlined">
                    {t("error.goBack")}
                </Link>
            </div>
        </div>
    );
};
