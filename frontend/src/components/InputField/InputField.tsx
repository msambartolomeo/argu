import cn from "classnames";
import { UseFormRegisterReturn } from "react-hook-form/dist/types/form";

interface Props {
    name?: string;
    type?: "text" | "email" | "password";
    text: string;
    value?: string;
    error?: string;
    register?: UseFormRegisterReturn;
    className?: string;
}

const InputField = ({
    name,
    type,
    text,
    value,
    error,
    register,
    className,
}: Props) => {
    if (type === undefined) {
        type = "text";
    }

    return (
        <div className={`input-field ${className}`}>
            <input
                data-testid={name}
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
