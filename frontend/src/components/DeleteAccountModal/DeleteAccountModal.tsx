import "./DeleteAccountModal.css";

const DeleteConfirmationForm = () => {
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
                >
                    Confirm
                </button>
            </div>
        </form>
    );
};

const DeleteAccountModal = () => {
    return (
        <>
            <a
                className="waves-effect waves-light btn modal-trigger delete-account-btn"
                href="#delete-account"
            >
                Delete Account
            </a>
            <div id="delete-account" className="modal">
                <DeleteConfirmationForm />
            </div>
        </>
    );
};

export default DeleteAccountModal;
