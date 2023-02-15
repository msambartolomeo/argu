import { Controller } from "react-hook-form";

import {
    FormControl,
    FormControlLabel,
    FormHelperText,
    FormLabel,
    Radio,
    RadioGroup,
} from "@mui/material";

import "./RadioComponent.css";

interface RadioComponentProps {
    name: string;
    label: string;
    /* eslint-disable  @typescript-eslint/no-explicit-any */
    control: any;
    options: { value: string; label: string }[];
    error?: string;
}

const RadioComponent = ({
    name,
    label,
    control,
    options,
    error,
    ...props
}: RadioComponentProps) => {
    return (
        <FormControl {...props}>
            <FormLabel component="legend">{label}</FormLabel>
            <Controller
                name={name}
                control={control}
                render={({ field }) => (
                    <>
                        <RadioGroup
                            {...field}
                            value={field.value}
                            // row
                            defaultValue={null}
                        >
                            {options.map((option) => (
                                <FormControlLabel
                                    key={option.value}
                                    value={option.value}
                                    label={option.label}
                                    onChange={field.onChange}
                                    control={<Radio />}
                                />
                            ))}
                        </RadioGroup>
                        <FormHelperText error={Boolean(error)}>
                            {error}
                        </FormHelperText>
                    </>
                )}
            />
        </FormControl>
    );
};

export default RadioComponent;
