import { HttpStatusCode } from "axios";
import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";

import "./Error.css";

import error400 from "../../assets/error400.png";
import error404 from "../../assets/error404.png";
import error500 from "../../assets/error500.png";

interface ErrorProps {
    status: number;
    message?: string;
}

export const Error = ({ status, message }: ErrorProps) => {
    const { t } = useTranslation();

    typeof message === "undefined" ? t("errors.message") : message;

    let image = "";
    switch (status) {
        case HttpStatusCode.BadRequest:
            image = error400;
            break;
        case HttpStatusCode.NotFound:
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
                <h1>{t("errors.title")}</h1>
                <h6>{message}</h6>
                <Link to={"/"} className="link-underlined">
                    {t("errors.goBack")}
                </Link>
            </div>
        </div>
    );
};
