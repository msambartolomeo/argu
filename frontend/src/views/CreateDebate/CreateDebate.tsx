import "./CreateDebate.css";

import { useRef } from "react";

import TextArea from "../../components/TextArea/TextArea";
import InputField from "../../components/InputField/InputField";
import SelectDropdown from "../../components/SelectDropdown/SelectDropdown";
import DebateCategory from "../../model/enums/DebateCategory";
import SubmitButton from "../../components/SubmitButton/SubmitButton";

const CreateDebate = () => {
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
                    <span className="card-title center">Create debate</span>
                    <TextArea text={"Debate title:"} />
                    <TextArea text={"Debate description:"} />
                    <table className="no-borders radio-button-class">
                        <tr>
                            <td>
                                <label>
                                    What is your position in this debate?
                                </label>
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
                                    <span>For</span>
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
                                    <span>Against</span>
                                </label>
                            </td>
                        </tr>
                    </table>
                    <InputField text="Your opponent's username:" />
                    <table className="no-borders">
                        <tr>
                            <td>Debate category:</td>
                            <td>
                                <SelectDropdown suppliers={suppliers} />
                            </td>
                        </tr>
                    </table>
                    <table className="no-borders">
                        <tr>
                            <td>
                                Debate image (optional):
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
                                            Upload Image
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
                    </table>
                    <SubmitButton text="Create debate" />
                </div>
            </form>
        </div>
    );
};

export default CreateDebate;
