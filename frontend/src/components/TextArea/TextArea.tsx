import { ChangeEventHandler } from "react";
import cn from "classnames";

interface Props {
    text: string;
    value?: string;
    error?: string;
    onChange?: ChangeEventHandler<HTMLTextAreaElement>;
}

function TextArea({ text, value, error, onChange }: Props) {
    return (
        <div className="input-field">
            <textarea
                value={value}
                onChange={onChange}
                className={cn({
                    invalid: error !== null,
                    "materialize-textarea": true,
                })}
            />
            <label className="active">{text}</label>
            {error && <span className="helper-text error">error</span>}
        </div>
    );
}

export default TextArea;
