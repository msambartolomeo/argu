import { ChangeEvent, useEffect } from "react";

import M from "materialize-css";
import { useTranslation } from "react-i18next";
import { useSearchParams } from "react-router-dom";

import "./SortSelectComponent.css";

import "../../../locales/index";

interface SortSelectComponentProps {
    type: "status" | "order";
    options: string[];
    handleSelectChange: (event: ChangeEvent<HTMLSelectElement>) => void;
}

const SortSelectComponent = ({
    type,
    options,
    handleSelectChange,
}: SortSelectComponentProps) => {
    const { t } = useTranslation();

    const [queryParams] = useSearchParams();

    useEffect(() => {
        M.AutoInit();
    }, []);

    const optionsElements = options.map((option) => {
        return (
            <option key={option} value={option}>
                {t(`discovery.orderBy.${type}.${option}`)}
            </option>
        );
    });

    return (
        <div className="input-field margin-left">
            <select
                id={`select-${type}`}
                onChange={handleSelectChange}
                value={queryParams.get(type) ?? ""}
            >
                {optionsElements}
            </select>
            <label htmlFor="select-status">
                {t(`discovery.orderBy.${type}.title`)}
            </label>
        </div>
    );
};

export default SortSelectComponent;
