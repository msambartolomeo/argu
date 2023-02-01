import { useTranslation } from "react-i18next";

import { useNavigate } from "react-router-dom";

import InputField from "../../components/InputField/InputField";
import SubmitButton from "../../components/SubmitButton/SubmitButton";
import "../../locales/index";
import "./Login.css";

const Login = () => {
    const navigate = useNavigate();
    const { t } = useTranslation();

    const handleRedirect = () => {
        navigate("/register");
    };

    return (
        <div className="card login-container">
            <form method="post" acceptCharset="utf-8">
                <div className="card-content container-with-space">
                    <span className="card-title center">
                        {t("login.welcomeBack")}
                    </span>
                    <InputField text={t("login.username")} />
                    <InputField text={t("login.password")} type="password" />
                    <div className="left">
                        <label>
                            <input
                                type="checkbox"
                                className="filled-in"
                                name="rememberme"
                                id="rememberme"
                            />
                            <span>{t("login.rememberMe")}</span>
                        </label>
                    </div>
                    <SubmitButton text={t("login.login")} />
                </div>
            </form>
            <h6 className="center">
                {t("login.noAccount")}&nbsp;
                <a onClick={handleRedirect}>{t("login.register")}</a>
            </h6>
        </div>
    );
};

export default Login;
