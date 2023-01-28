import InputField from "../../components/InputField/InputField";
import SubmitButton from "../../components/SubmitButton/SubmitButton";

import { useNavigate } from "react-router-dom";
import { useTranslation } from "react-i18next";

const Register = () => {
    const navigate = useNavigate();
    const { t } = useTranslation();

    const handleRedirect = () => {
        navigate("/login");
    };

    // TODO: Error handling and api call

    return (
        <div className="card login-container">
            <form method="post" acceptCharset="utf-8">
                <div className="card-content">
                    <span className="card-title center">
                        {t("register.firstTime")}
                    </span>
                    <InputField text={t("register.email")} type="email" />
                    <InputField text={t("register.username")} />
                    <InputField text={t("register.password")} type="password" />
                    <InputField
                        text={t("register.confirmPassword")}
                        type="password"
                    />
                    <SubmitButton text={t("register.register")} />
                </div>
            </form>
            <h6 className="center">
                {t("register.alreadyHaveAccount")}
                <a onClick={handleRedirect}>{t("register.login")}</a>
            </h6>
        </div>
    );
};

export default Register;
