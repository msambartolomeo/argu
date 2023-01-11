import "./DeleteAccountModal.css";
import { useTranslation } from "react-i18next";
import "../../locales/index";

const DeleteConfirmationForm = () => {
    const { t } = useTranslation();

    function handleSubmit(): void {
        // console.log("Confirm submition of profile photo");
    }

    // TODO: Add error handling

    return (
        <form
            method="delete"
            acceptCharset="utf-8"
            id="confirmationForm"
            encType="multipart/form-data"
            onSubmit={handleSubmit}
        >
            <div className="modal-content">
                <h4>
                    {t("profile.areYouSure")}
                </h4>
                <div className="input-field">
                    <label>Introduce password</label>
                    <input type="password" className="validate" />
                </div>
            </div>
            <div className="modal-footer">
                <a href="" className="modal-close waves-effect btn-flat">
                    Close
                </a>
                <button
                    className="modal-close waves-effect btn-flat"
                    type="submit"
                    form="confirmationForm"
                    id="confirmationForm"
                    name="deleteAccount"
                >
                    Confirm
                </button>
            </div>
        </form>
    );
};

const DeleteAccountModal = () => {
    const { t } = useTranslation();

    return (
        <>
            <a
                className="waves-effect waves-light btn modal-trigger delete-account-btn"
                href="#delete-account"
            >
                {t("profile.deleteAccount")}
            </a>
            <div id="delete-account" className="modal">
                <DeleteConfirmationForm />
            </div>
        </>
    );
};

export default DeleteAccountModal;
