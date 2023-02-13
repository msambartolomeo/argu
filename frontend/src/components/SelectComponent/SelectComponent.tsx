import { Controller } from "react-hook-form";

import { Autocomplete, TextField } from "@mui/material";
import FormControl from "@mui/material/FormControl/FormControl";

interface SelectComponentProps {
    name: string;
    label: string;
    /* eslint-disable  @typescript-eslint/no-explicit-any */
    control: any;
    options: { value: string; label: string }[];
    error?: string;
}

const SelectComponent = ({
    name,
    label,
    control,
    options,
    error,
    ...props
}: SelectComponentProps) => {
    return (
        <FormControl {...props} fullWidth={true}>
            <Controller
                name={name}
                control={control}
                render={({ field }) => (
                    <Autocomplete
                        options={options}
                        autoComplete={true}
                        isOptionEqualToValue={(option, value) =>
                            option.value === value.value
                        }
                        renderInput={(params) => (
                            <TextField
                                {...field}
                                {...params}
                                label={label}
                                error={Boolean(error)}
                                helperText={error}
                            />
                        )}
                        onChange={(_, data) => field.onChange(data)}
                    />
                )}
            />
        </FormControl>
    );
};
export default SelectComponent;
