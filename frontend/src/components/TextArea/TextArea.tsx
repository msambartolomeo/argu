import cn from "classnames";
import { UseFormRegisterReturn } from "react-hook-form/dist/types/form";

interface Props {
    text: string;
    value?: string;
    error?: string;
    register?: UseFormRegisterReturn;
}

function TextArea({ text, value, error, register }: Props) {
    return (
        <div className="input-field">
            <textarea
                value={value}
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
