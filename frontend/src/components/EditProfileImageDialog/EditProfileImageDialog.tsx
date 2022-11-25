import ProfilePhotoForm from "../ProfilePhotoForm/ProfilePhotoForm";
import "./EditProfileImageDialog.css";

const EditImageDialog = () => {
    return (
        <>
            {/* Modal Trigger */}
            <a
                className="waves-effect waves-light btn modal-trigger"
                href="#edit-profile-image"
            >
                Edit Profile Image
            </a>
            {/* Modal Structure */}
            <div id="edit-profile-image" className="modal">
                <ProfilePhotoForm />
            </div>
        </>
    );
};

export default EditImageDialog;
