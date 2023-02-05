import M from "materialize-css";
import { useTranslation } from "react-i18next";

import { RefObject, useEffect, useRef } from "react";

import "../../locales/index";
import "./DatePicker.css";

type Props = {
    label: string;
    placeholder?: string;
    currentDate?: string;
    date: RefObject<HTMLInputElement>;
};

function DatePicker({ label, placeholder, currentDate, date }: Props) {
    const { t } = useTranslation();

    function reloadDebates() {
        // TODO: check if it is necesary to reload (maybe it can be done in the store)
    }

    const datePickerCancel: string = t("discovery.orderBy.datePicker.cancel");
    const datePickerClear: string = t("discovery.orderBy.datePicker.clear");
    const datePickerDone: string = t("discovery.orderBy.datePicker.done");

    useEffect(() => {
        const queryParams = new URLSearchParams();
        M.Datepicker.init(document.querySelectorAll(".datepicker"), {
            format: "dd-mm-yyyy",
            showClearBtn: true,
            onClose: () => {
                // TODO check why url is not updated instantly but after other action
                // console.log(date.current?.value);
                // queryParams.delete("date");
                // if (date.current && date.current.value.toString() !== "") {
                //     queryParams.append("date", date.current.value);
                // }
                // navigate({ search: queryParams.toString() });
            },
            i18n: {
                cancel: datePickerCancel,
                clear: datePickerClear,
                done: datePickerDone,
                months: [
                    t("discovery.orderBy.datePicker.months.january"),
                    t("discovery.orderBy.datePicker.months.february"),
                    t("discovery.orderBy.datePicker.months.march"),
                    t("discovery.orderBy.datePicker.months.april"),
                    t("discovery.orderBy.datePicker.months.may"),
                    t("discovery.orderBy.datePicker.months.june"),
                    t("discovery.orderBy.datePicker.months.july"),
                    t("discovery.orderBy.datePicker.months.august"),
                    t("discovery.orderBy.datePicker.months.september"),
                    t("discovery.orderBy.datePicker.months.october"),
                    t("discovery.orderBy.datePicker.months.november"),
                    t("discovery.orderBy.datePicker.months.december"),
                ],
                monthsShort: [
                    t("discovery.orderBy.datePicker.monthsShort.january"),
                    t("discovery.orderBy.datePicker.monthsShort.february"),
                    t("discovery.orderBy.datePicker.monthsShort.march"),
                    t("discovery.orderBy.datePicker.monthsShort.april"),
                    t("discovery.orderBy.datePicker.monthsShort.may"),
                    t("discovery.orderBy.datePicker.monthsShort.june"),
                    t("discovery.orderBy.datePicker.monthsShort.july"),
                    t("discovery.orderBy.datePicker.monthsShort.august"),
                    t("discovery.orderBy.datePicker.monthsShort.september"),
                    t("discovery.orderBy.datePicker.monthsShort.october"),
                    t("discovery.orderBy.datePicker.monthsShort.november"),
                    t("discovery.orderBy.datePicker.monthsShort.december"),
                ],
                weekdays: [
                    t("discovery.orderBy.datePicker.weekdays.sunday"),
                    t("discovery.orderBy.datePicker.weekdays.monday"),
                    t("discovery.orderBy.datePicker.weekdays.tuesday"),
                    t("discovery.orderBy.datePicker.weekdays.wednesday"),
                    t("discovery.orderBy.datePicker.weekdays.thursday"),
                    t("discovery.orderBy.datePicker.weekdays.friday"),
                    t("discovery.orderBy.datePicker.weekdays.saturday"),
                ],
                weekdaysShort: [
                    t("discovery.orderBy.datePicker.weekdaysShort.sunday"),
                    t("discovery.orderBy.datePicker.weekdaysShort.monday"),
                    t("discovery.orderBy.datePicker.weekdaysShort.tuesday"),
                    t("discovery.orderBy.datePicker.weekdaysShort.wednesday"),
                    t("discovery.orderBy.datePicker.weekdaysShort.thursday"),
                    t("discovery.orderBy.datePicker.weekdaysShort.friday"),
                    t("discovery.orderBy.datePicker.weekdaysShort.saturday"),
                ],
                weekdaysAbbrev: [
                    t("discovery.orderBy.datePicker.weekdaysAbbrev.sunday"),
                    t("discovery.orderBy.datePicker.weekdaysAbbrev.monday"),
                    t("discovery.orderBy.datePicker.weekdaysAbbrev.tuesday"),
                    t("discovery.orderBy.datePicker.weekdaysAbbrev.wednesday"),
                    t("discovery.orderBy.datePicker.weekdaysAbbrev.thursday"),
                    t("discovery.orderBy.datePicker.weekdaysAbbrev.friday"),
                    t("discovery.orderBy.datePicker.weekdaysAbbrev.saturday"),
                ],
            },
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
                    className="datepicker white-text no-autoinit"
                    ref={date}
                    value={date.current?.value || currentDate || ""}
                />
            </div>
        </>
    );
}

export default DatePicker;
