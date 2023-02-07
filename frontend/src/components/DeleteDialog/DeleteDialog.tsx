import { useEffect } from "react";

import M from "materialize-css";
import { useTranslation } from "react-i18next";

import "./DeleteDialog.css";

import "../../locales/index";

interface DeleteDialogProps {
    id: string;
    handleDelete: () => void;
    title: string;
    name?: string | null;
}

const DeleteDialog = ({ id, handleDelete, title, name }: DeleteDialogProps) => {
    const { t } = useTranslation();

    useEffect(() => {
        M.Modal.init(document.querySelectorAll(".modal"));
    }, []);

    return (
        <>
            <a
                className="btn waves-effect chip chip-delete modal-trigger"
                href={`#${id}`}
            >
                {name}
                <i className="material-icons">delete</i>
            </a>
            <div id={id} className="modal">
                <div className="modal-content">
                    <h4>{title}</h4>
                </div>
                <div className="modal-footer">
                    <a className="modal-close waves-effect btn-flat">
                        {t("components.deleteDialog.cancel")}
                    </a>
                    <button
                        onClick={handleDelete}
                        className="modal-close waves-effect btn-flat"
                    >
                        {t("components.deleteDialog.yes")}
                    </button>
                </div>
            </div>
        </>
    );
};

export default DeleteDialog;
