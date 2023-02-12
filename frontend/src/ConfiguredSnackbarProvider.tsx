import { SnackbarProviderProps } from "notistack";
import { SnackbarProvider } from "notistack";

const ConfiguredSnackbarProvider = ({ ...props }: SnackbarProviderProps) => (
    <SnackbarProvider
        maxSnack={4}
        anchorOrigin={{
            vertical: "bottom",
            horizontal: "left",
        }}
        {...props}
    />
);

export default ConfiguredSnackbarProvider;
