import { RefObject, useEffect, useRef, useState } from "react";

import M from "materialize-css";
import { useTranslation } from "react-i18next";
import { useSearchParams } from "react-router-dom";

import "./DatePicker.css";

import { useNonInitialEffect } from "../../hooks/useNonInitialEffect";
import "../../locales/index";

type Props = {
    label: string;
    placeholder?: string;
};

function DatePicker({ label, placeholder }: Props) {
    const { t } = useTranslation();

    const date = useRef() as RefObject<HTMLInputElement>;

    const [queryParams, setQueryParams] = useSearchParams();

    const [update, setUpdate] = useState(false);

    useNonInitialEffect(() => {
        queryParams.delete("date");
        queryParams.delete("page");
        if (date.current && date.current.value.toString() !== "") {
            queryParams.append("date", date.current.value);
        }
        setQueryParams(queryParams);
        setUpdate(false);
    }, [update]);

    const datePickerCancel: string = t("discovery.orderBy.datePicker.cancel");
    const datePickerClear: string = t("discovery.orderBy.datePicker.clear");
    const datePickerDone: string = t("discovery.orderBy.datePicker.done");

    const queryDate = queryParams.get("date");
    let defaultDate: Date | null;
    if (queryDate) {
        const queryDateArray = queryDate.split("-");
        const dateString = `${queryDateArray[2]}-${queryDateArray[1]}-${
            Number(queryDateArray[0]) + 1
        }`;
        defaultDate = new Date(dateString);
    } else {
        defaultDate = null;
    }

    const monthsArray = [
        "january",
        "february",
        "march",
        "april",
        "may",
        "june",
        "july",
        "august",
        "september",
        "october",
        "november",
        "december",
    ];

    const daysArray = [
        "sunday",
        "monday",
        "tuesday",
        "wednesday",
        "thursday",
        "friday",
        "saturday",
    ];

    useEffect(() => {
        M.Datepicker.init(document.querySelectorAll(".datepicker"), {
            format: "dd-mm-yyyy",
            showClearBtn: true,
            onClose: () => setUpdate(true),
            setDefaultDate: true,
            defaultDate: defaultDate,
            i18n: {
                cancel: datePickerCancel,
                clear: datePickerClear,
                done: datePickerDone,
                months: monthsArray.map((month) => {
                    return t(`discovery.orderBy.datePicker.months.${month}`);
                }),
                monthsShort: monthsArray.map((month) => {
                    return t(
                        `discovery.orderBy.datePicker.monthsShort.${month}`
                    );
                }),
                weekdays: daysArray.map((day) => {
                    return t(`discovery.orderBy.datePicker.weekdays.${day}`);
                }),
                weekdaysShort: daysArray.map((day) => {
                    return t(
                        `discovery.orderBy.datePicker.weekdaysShort.${day}`
                    );
                }),
                weekdaysAbbrev: daysArray.map((day) => {
                    return t(
                        `discovery.orderBy.datePicker.weekdaysAbbrev.${day}`
                    );
                }),
            },
        });
    }, []);

    return (
        <>
            <div
                className="input-field margin-left"
                style={{ display: "flex" }}
            >
                <label htmlFor="datepicker" className="active">
                    {label}
                </label>
                <input
                    id="datepicker"
                    placeholder={placeholder}
                    type="text"
                    className="datepicker white-text no-autoinit"
                    ref={date}
                />
            </div>
        </>
    );
}

export default DatePicker;
