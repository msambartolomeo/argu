const ProfilePhotoForm = () => {
    return (
        <form
            method="post"
            action="/profile/photo"
            encType="multipart/form-data"
            acceptCharset="utf-8"
        >
            <div className="modal-content">
                <h4>Edit Profile Image</h4>
                <div></div>
            </div>
        </form>
    );
};

export default ProfilePhotoForm;
