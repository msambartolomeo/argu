import { RefObject, useEffect, useRef, useState } from "react";

import M from "materialize-css";
import { useTranslation } from "react-i18next";
import { useSearchParams } from "react-router-dom";

import "./DatePicker.css";

import { useNonInitialEffect } from "../../hooks/useNonInitialEffect";
import "../../locales/index";

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

type Props = {
    label: string;
};

function DatePicker({ label }: Props) {
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

    function getDefaultDate() {
        const queryDate = queryParams.get("date");
        if (queryDate) {
            const queryDateArray = queryDate.split("-");
            const dateString = `${queryDateArray[2]}-${queryDateArray[1]}-${
                Number(queryDateArray[0]) + 1
            }`;
            return new Date(dateString);
        }
        return null;
    }

    useEffect(() => {
        M.Datepicker.init(document.querySelectorAll(".datepicker"), {
            format: "dd-mm-yyyy",
            showClearBtn: true,
            onClose: () => setUpdate(true),
            setDefaultDate: true,
            defaultDate: getDefaultDate(),
            i18n: {
                cancel: t("discovery.orderBy.datePicker.cancel") as string,
                clear: t("discovery.orderBy.datePicker.clear") as string,
                done: t("discovery.orderBy.datePicker.done") as string,
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
                    placeholder={
                        t("discovery.orderBy.datePicker.placeholder") as string
                    }
                    type="text"
                    className="datepicker white-text no-autoinit"
                    ref={date}
                />
            </div>
        </>
    );
}

export default DatePicker;
