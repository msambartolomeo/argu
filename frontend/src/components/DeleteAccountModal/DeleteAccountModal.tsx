import { useCallback, useEffect } from "react";

import { HttpStatusCode } from "axios";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";

import "./DeleteAccountModal.css";

import { useSharedAuth } from "../../hooks/useAuth";
import { useDeleteUser } from "../../hooks/users/useDeleteUser";
import "../../locales/index";
import { Error } from "../../views/Error/Error";

const DeleteAccountModal = () => {
    const { t } = useTranslation();

    const { loading: isDeleteUserLoading, deleteUser } = useDeleteUser();
    const { callLogout, userInfo } = useSharedAuth();
    const navigate = useNavigate();

    useEffect(() => {
        M.Modal.init(document.querySelectorAll(".modal"));
    }, []);

    const handleDeleteUser = useCallback(async () => {
        const res = await deleteUser(userInfo?.username || "");
        switch (res.status) {
            case HttpStatusCode.NoContent:
                callLogout();
                navigate("/");
                break;
            case HttpStatusCode.NotFound:
                return <Error status={res.status} message={res.message} />;
            default:
                break;
        }
    }, []);

    return (
        <>
            <a
                className="waves-effect waves-light btn modal-trigger delete-account-btn"
                href="#delete-profile"
            >
                {t("profile.deleteAccount")}
            </a>
            <div id="delete-profile" className="modal">
                <div className="modal-content">
                    <h4>{t("profile.areYouSure")}</h4>
                </div>
                <div className="modal-footer">
                    <a className="modal-close waves-effect btn-flat">
                        {t("profile.close")}
                    </a>
                    <button
                        className="modal-close waves-effect btn-flat"
                        onClick={handleDeleteUser}
                    >
                        {t("profile.confirm")}
                    </button>
                </div>
            </div>
        </>
    );
};

export default DeleteAccountModal;
