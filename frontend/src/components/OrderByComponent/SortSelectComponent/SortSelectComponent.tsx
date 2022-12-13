import {
    FormControl,
    InputLabel,
    MenuItem,
    Select,
    SelectChangeEvent,
} from "@mui/material";
import "./SortSelectComponent.css";

interface SortSelectComponentProps {
    header: string;
    defaultValue?: any;
    id: string;
    labelId: string;
    suppliers: { value: any; label: string }[];
    query: string;
    handleSelectChange: (event: SelectChangeEvent) => void;
}

const SortSelectComponent = ({
    header,
    defaultValue,
    id,
    labelId,
    suppliers,
    query,
    handleSelectChange,
}: SortSelectComponentProps) => {
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
                    defaultValue={defaultValue}
                    value={query}
                    onChange={handleSelectChange}
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
