import M from "materialize-css";

import { RefObject, useEffect, useRef } from "react";

import "./DatePicker.css";

// TODO: add i18n to DatePicker

type Props = {
    label: string;
    placeholder?: string;
    currentDate?: string;
};

function DatePicker({ label, placeholder, currentDate }: Props) {
    const date = useRef() as RefObject<HTMLInputElement>;

    function reloadDebates() {
        // TODO: check if it is necesary to reload (maybe it can be done in the store)
        // console.log(date.current?.value);
        // TODO: change debates state
    }

    useEffect(() => {
        M.Datepicker.init(document.querySelectorAll(".datepicker"), {
            // TODO: Add i18n to months and buttons
            format: "dd-mm-yyyy",
            showClearBtn: true,
            onClose: reloadDebates,
        });
    }, []);

    return (
        <>
            <div className="input-field" style={{ display: "flex" }}>
                <label htmlFor="datepicker" className="active">
                    {label}
                </label>
                <input
                    id="datepicker"
                    placeholder={placeholder}
                    type="text"
                    className="datepicker white-text"
                    ref={date}
                    value={currentDate}
                />
            </div>
        </>
    );
}

export default DatePicker;
