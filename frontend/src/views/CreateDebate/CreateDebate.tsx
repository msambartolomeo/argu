import { useRef } from "react";

import { useTranslation } from "react-i18next";

import "./CreateDebate.css";

import InputField from "../../components/InputField/InputField";
import SelectDropdown from "../../components/SelectDropdown/SelectDropdown";
import SubmitButton from "../../components/SubmitButton/SubmitButton";
import TextArea from "../../components/TextArea/TextArea";
import "../../locales/index";
import DebateCategory from "../../types/enums/DebateCategory";

const CreateDebate = () => {
    const { t } = useTranslation();

    const suppliers = Object.values(DebateCategory).map((category) => ({
        value: category,
        label: category,
    }));

    const imageRef = useRef<HTMLInputElement>(null);
    const imageNameRef = useRef<HTMLInputElement>(null);

    const clearImage = () => {
        if (imageRef.current && imageNameRef.current) {
            imageRef.current.value = "";
            imageNameRef.current.value = "";
        }
    };

    // TODO: Validations and submit action

    return (
        <div className="card debate-form-container">
            <form
                method="post"
                acceptCharset="utf-8"
                encType="multipart/form-data"
            >
                <div className="card-content">
                    <span className="card-title center">
                        {t("createDebate.createDebate")}
                    </span>
                    <TextArea text={t("createDebate.title")} />
                    <TextArea text={t("createDebate.description")} />
                    <table className="no-borders radio-button-class">
                        <tbody>
                            <tr>
                                <td>
                                    <label>{t("createDebate.position")}</label>
                                </td>
                            </tr>
                            <tr>
                                <td className="radio-button-label">
                                    <label>
                                        <input
                                            name="position"
                                            type="radio"
                                            value="true"
                                            id="for"
                                        />
                                        <span>{t("createDebate.for")}</span>
                                    </label>
                                </td>
                            </tr>
                            <tr>
                                <td className="radio-button-label">
                                    <label>
                                        <input
                                            name="position"
                                            type="radio"
                                            value="false"
                                            id="against"
                                        />
                                        <span>{t("createDebate.against")}</span>
                                    </label>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <InputField text="Your opponent's username:" />
                    <table className="no-borders">
                        <tbody>
                            <tr>
                                <td>{t("createDebate.category")}</td>
                                <td>
                                    <SelectDropdown suppliers={suppliers} />
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <table className="no-borders">
                        <tbody>
                            <tr>
                                <td>
                                    {t("createDebate.image")}
                                    <a
                                        className="material-icons"
                                        onClick={clearImage}
                                    >
                                        close
                                    </a>
                                </td>
                                <td>
                                    <div className="file-field input-field">
                                        <div className="btn">
                                            <label className="white-text">
                                                {t("createDebate.uploadImage")}
                                            </label>
                                            <input
                                                ref={imageRef}
                                                id="image"
                                                type="file"
                                            />
                                        </div>
                                        <div className="file-path-wrapper">
                                            <input
                                                className="file-path"
                                                ref={imageNameRef}
                                                type="text"
                                            />
                                        </div>
                                    </div>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <SubmitButton text={t("createDebate.createDebate")} />
                </div>
            </form>
        </div>
    );
};

export default CreateDebate;
