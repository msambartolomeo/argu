const ProfilePhotoForm = () => {
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
                <h4>Edit Profile Image</h4>
                <div className="file-field input-field">
                    <div className="btn">
                        <label className="white-text">Upload</label>
                        <input type="file" />
                    </div>
                    <div className="file-path-wrapper">
                        <input className="file-path validate" type="text" />
                    </div>
                </div>
            </div>
            <div className="modal-footer">
                <a href="" className="modal-close waves-effect btn-flat">
                    Close
                </a>
                <button
                    className="modal-close waves-effect btn-flat"
                    type="submit"
                    form="photoForm"
                    id="photoForm"
                    name="editImage"
                    onSubmit={handleSubmit}
                >
                    Confirm
                </button>
            </div>
        </form>
    );
};

export default ProfilePhotoForm;
