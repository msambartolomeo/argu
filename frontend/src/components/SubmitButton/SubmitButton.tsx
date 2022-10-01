import { FC } from "react";
import "./SubmitButton.css";

interface ButtonProps {
    text: string;
}

const SubmitButton: FC<ButtonProps> = ({ text }): JSX.Element => {
    return (
        <>
            <button type="submit" className="btn waves-effect center-block">
                {text}
                <i className="material-icons right">send</i>
            </button>
        </>
    );
};

export default SubmitButton;
