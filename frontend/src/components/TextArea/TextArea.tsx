import { useState } from "react";

type Props = {
    text?: string;
};

function TextArea({ text }: Props) {
    const [value, setValue] = useState(text || "");

    return (
        <div className="input-field">
            <input
                type="text"
                value={value}
                onChange={(e) => setValue(e.target.value)}
            />
            <label className="active">Text</label>
        </div>
    );
}

export default TextArea;
