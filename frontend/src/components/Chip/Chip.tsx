interface ChipProps {
    children?: React.ReactNode;
    onClick?: () => void;
    disabled?: boolean;
}

const Chip = ({ children, onClick, disabled }: ChipProps) => {
    return (
        <>
            <button
                className="chip btn waves-effect"
                disabled={disabled}
                style={{ pointerEvents: onClick ? "auto" : "none" }}
                onClick={onClick}
            >
                {children}
            </button>
        </>
    );
};

export default Chip;
