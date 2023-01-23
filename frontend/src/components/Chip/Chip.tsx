interface ChipProps {
    name: string;
    path?: string;
}

const Chip = ({ name, path }: ChipProps) => {
    return (
        <>
            <a
                className="chip btn waves-effect"
                style={{ pointerEvents: path ? "auto" : "none" }}
                href={path}
            >
                {name}
            </a>
        </>
    );
};

export default Chip;
