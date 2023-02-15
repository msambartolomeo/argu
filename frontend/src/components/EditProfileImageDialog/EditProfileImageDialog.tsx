import { useCallback, useRef } from "react";

import { HttpStatusCode } from "axios";
import { useForm } from "react-hook-form";
import { useTranslation } from "react-i18next";
import { z } from "zod";

import { zodResolver } from "@hookform/resolvers/zod";

import { useUpdateUserImage } from "../../hooks/users/useUpdateUserImage";
import "../../locales/index";
import { Error } from "../../views/Error/Error";

interface EditImageDialogProps {
    imageUrl: string;
    imageChange: () => void;
}

type FieldValues = {
    filename: string;
    image: FileList;
};

const EditImageDialog = ({ imageUrl, imageChange }: EditImageDialogProps) => {
    const { t } = useTranslation();

    const MAX_FILE_SIZE = 10 * 1024 * 1024;

    const { loading: isUpdateImageLoading, updateUserImage } =
        useUpdateUserImage();

    const schema = z.object({
        image: z
            .any()
            .refine(
                (file) => file !== undefined && file[0] !== undefined,
                t("profile.imageEmpty").toString()
            )
            .refine(
                (file) => file[0]?.size <= MAX_FILE_SIZE,
                t("profile.imageTooBig").toString()
            ),
    });

    const {
        register,
        handleSubmit,
        formState: { errors: formErrors },
        setError,
    } = useForm<FieldValues>({
        resolver: zodResolver(schema),
    });

    const handleEditProfile = useCallback(async (data: FieldValues) => {
        const res = await updateUserImage({
            imageUrl: imageUrl,
            image: data.image[0],
        });
        switch (res.status) {
            case HttpStatusCode.Created:
                imageChange();
                break;
            case HttpStatusCode.NotFound:
                <Error status={res.status} message={res.message} />;
                break;
            case HttpStatusCode.PayloadTooLarge:
                setError("image", {
                    message: t("profile.imageTooBig").toString(),
                });
        }
    }, []);

    const modalRef = useRef<HTMLDivElement>(null);
    if (modalRef.current && (formErrors.filename || formErrors.image)) {
        const modal = M.Modal.init(modalRef.current);
        if (!modal.isOpen) {
            modal.open();
        }
    }

    return (
        <>
            <a
                className="waves-effect waves-light btn modal-trigger"
                href="#edit-profile-image"
            >
                {t("profile.editProfileImg")}
            </a>
            <div ref={modalRef} id="edit-profile-image" className="modal">
                <form
                    method="put"
                    encType="multipart/form-data"
                    acceptCharset="utf-8"
                    onSubmit={handleSubmit((data) => {
                        handleEditProfile(data as FieldValues);
                    })}
                >
                    <div className="modal-content">
                        <h4>{t("profile.editProfileImg")}</h4>
                        <div className="file-field input-field">
                            <div className="btn">
                                <label className="white-text">
                                    {t("profile.upload")}
                                </label>
                                <input
                                    type="file"
                                    accept="image/*"
                                    {...register("image")}
                                />
                            </div>
                            <div className="file-path-wrapper">
                                <input
                                    className="file-path validate"
                                    type="text"
                                />
                            </div>
                            {formErrors.filename && (
                                <span className="helper-text error">
                                    {formErrors.filename?.message as string}
                                </span>
                            )}
                            {formErrors.image && (
                                <span className="helper-text error">
                                    {formErrors.image?.message as string}
                                </span>
                            )}
                        </div>
                    </div>
                    <div className="modal-footer">
                        <a className="modal-close waves-effect btn-flat">
                            {t("profile.close")}
                        </a>
                        <button
                            className="modal-close waves-effect btn-flat"
                            type="submit"
                            disabled={isUpdateImageLoading}
                        >
                            {t("profile.confirm")}
                        </button>
                    </div>
                </form>
            </div>
        </>
    );
};

export default EditImageDialog;
