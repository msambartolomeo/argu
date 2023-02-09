import cn from "classnames";
import { UseFormRegisterReturn } from "react-hook-form/dist/types/form";

interface Props {
    type?: "text" | "email" | "password";
    text: string;
    value?: string;
    error?: string;
    register?: UseFormRegisterReturn;
}

const InputField = ({ type, text, value, error, register }: Props) => {
    if (type === undefined) {
        type = "text";
    }

    return (
        <div className="input-field">
            <input
                type={type}
                value={value}
                className={cn({ invalid: error !== undefined })}
                {...register}
            />
            <label className="input-label active">{text}</label>
            {error && <span className="helper-text error">{error}</span>}
        </div>
    );
};

export default InputField;
