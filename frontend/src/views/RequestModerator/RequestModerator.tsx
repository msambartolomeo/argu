import TextArea from "../../components/TextArea/TextArea";
import "./RequestModerator.css";
import "../../locales/index";

import { useTranslation } from "react-i18next";

const RequestModerator = () => {
    const { t } = useTranslation();
    // TODO: Error handling and connect to api
    return (
        <div className="card moderator-container">
            <form method="post" acceptCharset="utf-8">
                <div className="card-content">
                    <span className="card-title">
                        {t("requestModerator.requestModerator")}
                    </span>
                    <p>{t("requestModerator.message")}</p>
                    <TextArea text={t("requestModerator.label")} />
                    <button
                        className="btn waves-effect center-block"
                        type="submit"
                        name="action"
                    >
                        {t("requestModerator.submit")}
                        <i className="material-icons right">send</i>
                    </button>
                </div>
            </form>
        </div>
    );
};

export default RequestModerator;
