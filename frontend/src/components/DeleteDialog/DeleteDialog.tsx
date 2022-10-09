import { useEffect } from "react";
import "./DeleteDialog.css";
import M from "materialize-css";

interface DeleteDialogProps {
    id: string;
    path: string;
    title: string;
}

const DeleteDialog = ({ id, path, title }: DeleteDialogProps) => {
    useEffect(() => {
        M.Modal.init(document.querySelectorAll(".modal"));
    }, []);

    return (
        <>
            <a
                className="btn waves-effect chip chip-delete modal-trigger"
                href={`#${id}`}
            >
                <i className="material-icons">delete</i>
            </a>
            <div id={id} className="modal">
                <form method="delete" action={path}>
                    <div className="modal-content">
                        <h4>{title}</h4>
                    </div>
                    <div className="modal-footer">
                        <a
                            href=""
                            className="modal-close waves-effect btn-flat"
                        >
                            Cancel
                        </a>
                        <button
                            type="submit"
                            className="modal-close waves-effect btn-flat"
                        >
                            Yes, I&apos;m sure
                        </button>
                    </div>
                </form>
            </div>
        </>
    );
};

export default DeleteDialog;
