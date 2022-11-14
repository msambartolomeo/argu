import { ChangeEventHandler } from "react";
import cn from "classnames";

interface Props {
    text: string;
    value?: string;
    error?: string;
    onChange?: ChangeEventHandler<HTMLInputElement>;
}

function InputField({ text, value, error, onChange }: Props) {
    return (
        <div className="input-field">
            <input
                type="text"
                value={value}
                onChange={onChange}
                className={cn({ invalid: error !== null })}
            />
            <label className="active">{text}</label>
            {error && <span className="helper-text error">error</span>}
        </div>
    );
}

export default InputField;
