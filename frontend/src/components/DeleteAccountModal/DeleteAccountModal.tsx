import "./DeleteAccountModal.css";
import DeleteConfirmationForm from "../DeleteConfirmationForm/DeleteConfirmationForm";

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
