import { useTranslation } from "react-i18next";

import {
    FormControl,
    InputLabel,
    MenuItem,
    Select,
    SelectChangeEvent,
} from "@mui/material";

import "./SortSelectComponent.css";

import "../../../locales/index";

interface SortSelectComponentProps {
    header: string;
    name: string;
    defaultValue?: any;
    id: string;
    labelId: string;
    suppliers: { value: any; label: string }[];
    query: string;
    handleSelectChange: (event: SelectChangeEvent) => void;
}

const SortSelectComponent = ({
    header,
    name,
    defaultValue,
    id,
    labelId,
    suppliers,
    query,
    handleSelectChange,
}: SortSelectComponentProps) => {
    const { t } = useTranslation();

    const placeholder = t(`discovery.orderBy.placeholders.${defaultValue}`);

    return (
        <div className="select-container">
            <FormControl
                variant="standard"
                sx={{ m: 1, minWidth: 120 }}
                className="white-text"
            >
                <InputLabel id={labelId} className="white-text">
                    {t(`discovery.orderBy.${name}.title`)}
                </InputLabel>
                <Select
                    classes={{
                        icon: "white-text",
                    }}
                    className="white-text"
                    labelId={labelId}
                    id={id}
                    placeholder={placeholder}
                    displayEmpty={true}
                    defaultValue={defaultValue}
                    value={query}
                    onChange={handleSelectChange}
                    label={header}
                >
                    {suppliers.map((supplier) => (
                        <MenuItem key={supplier.label} value={supplier.value}>
                            {t(`discovery.orderBy.${name}.${supplier.label}`)}
                        </MenuItem>
                    ))}
                </Select>
            </FormControl>
        </div>
    );
};

export default SortSelectComponent;
