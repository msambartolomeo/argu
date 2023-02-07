import { ChangeEventHandler } from "react";

import cn from "classnames";
import { UseFormRegisterReturn } from "react-hook-form/dist/types/form";

interface Props {
    text: string;
    value?: string;
    error?: string;
    onChange?: ChangeEventHandler<HTMLTextAreaElement>;
    register?: UseFormRegisterReturn;
}

function TextArea({ text, value, error, onChange, register }: Props) {
    return (
        <div className="input-field">
            <textarea
                value={value}
                onChange={onChange}
                className={cn({
                    invalid: error !== undefined,
                    "materialize-textarea": true,
                })}
                {...register}
            />
            <label className="input-label active">{text}</label>
            {error && <span className="helper-text error">{error}</span>}
        </div>
    );
}

export default TextArea;
