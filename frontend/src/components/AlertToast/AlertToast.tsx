import { SyntheticEvent } from "react";

import { Alert, Snackbar } from "@mui/material";

interface AlertToastProps {
    open: boolean;
    message: string;
    severity: "error" | "warning" | "info" | "success";
    onClose?: () => void;
    autoHideDuration?: number;
}

const AlertToast = ({
    open,
    message,
    severity,
    onClose,
    autoHideDuration,
}: AlertToastProps) => {
    const handleClose = (event: SyntheticEvent | Event, reason?: string) => {
        if (reason === "clickaway") {
            return;
        }

        onClose && onClose();
    };

    return (
        <Snackbar
            open={open}
            onClose={handleClose}
            autoHideDuration={autoHideDuration ? autoHideDuration : 8000}
            anchorOrigin={{
                vertical: "bottom",
                horizontal: "center",
            }}
        >
            <Alert
                severity={severity}
                sx={{ width: "100%" }}
                onClose={handleClose}
            >
                {message}
            </Alert>
        </Snackbar>
    );
};

export default AlertToast;
