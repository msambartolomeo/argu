import { useTranslation } from "react-i18next";
import "../../locales/index";

const ProfilePhotoForm = () => {
    const { t } = useTranslation();

    function handleSubmit(): void {
        // console.log("Confirm submition of profile photo");
    }

    // TODO: Add error handling

    return (
        <form
            id="photoForm"
            method="post"
            action="/profile/photo"
            encType="multipart/form-data"
            acceptCharset="utf-8"
        >
            <div className="modal-content">
                <h4>{t("profile.editProfileImg")}</h4>
                <div className="file-field input-field">
                    <div className="btn">
                        <label className="white-text">
                            {t("profile.upload")}
                        </label>
                        <input type="file" />
                    </div>
                    <div className="file-path-wrapper">
                        <input className="file-path validate" type="text" />
                    </div>
                </div>
            </div>
            <div className="modal-footer">
                <a href="" className="modal-close waves-effect btn-flat">
                    {t("profile.close")}
                </a>
                <button
                    className="modal-close waves-effect btn-flat"
                    type="submit"
                    form="photoForm"
                    id="photoForm"
                    name="editImage"
                    onSubmit={handleSubmit}
                >
                    {t("profile.confirm")}
                </button>
            </div>
        </form>
    );
};

const EditImageDialog = () => {
    const { t } = useTranslation();

    return (
        <>
            {/* Modal Trigger */}
            <a
                className="waves-effect waves-light btn modal-trigger"
                href="#edit-profile-image"
            >
                {t("profile.editProfileImg")}
            </a>
            {/* Modal Structure */}
            <div id="edit-profile-image" className="modal">
                <ProfilePhotoForm />
            </div>
        </>
    );
};

export default EditImageDialog;
