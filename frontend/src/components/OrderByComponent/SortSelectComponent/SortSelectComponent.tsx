import {
    FormControl,
    InputLabel,
    MenuItem,
    Select,
    SelectChangeEvent,
} from "@mui/material";
import React from "react";
import "./SortSelectComponent.css";

interface SortSelectComponentProps {
    header: string;
    defaultValue?: any;
    id: string;
    labelId: string;
    suppliers: { value: any; label: string }[];
}

const SortSelectComponent = ({
    header,
    defaultValue,
    id,
    labelId,
    suppliers,
}: SortSelectComponentProps) => {
    const [value, setValue] = React.useState("");

    const handleChange = (event: SelectChangeEvent) => {
        setValue(event.target.value);
    };

    return (
        <div className="select-container">
            <FormControl
                variant="standard"
                sx={{ m: 1, minWidth: 120 }}
                className="white-text"
            >
                <InputLabel id={labelId} className="white-text">
                    {header}
                </InputLabel>
                <Select
                    className="white-text"
                    labelId={labelId}
                    id={id}
                    // defaultValue={defaultValue}
                    value={value}
                    onChange={handleChange}
                    label={header}
                >
                    {suppliers.map((supplier) => (
                        <MenuItem key={supplier.label} value={supplier.value}>
                            {supplier.label}
                        </MenuItem>
                    ))}
                </Select>
            </FormControl>
        </div>
    );
};

export default SortSelectComponent;
