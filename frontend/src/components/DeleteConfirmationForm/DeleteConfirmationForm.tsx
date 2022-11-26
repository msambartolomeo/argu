const DeleteConfirmationForm = () => {
    function handleSubmit(): void {
        console.log("Confirm submition of profile photo");
    }

    // TODO: Add error handling

    return (
        <form
            method="delete"
            acceptCharset="utf-8"
            action="/profile/delete"
            id="confirmationForm"
            encType="multipart/form-data"
        >
            <div className="modal-content">
                <h4>
                    Are you sure you want to delete your account? This action
                    cannot be undone.
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
                    onSubmit={handleSubmit}
                >
                    Confirm
                </button>
            </div>
        </form>
    );
};

export default DeleteConfirmationForm;
