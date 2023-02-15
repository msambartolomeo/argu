import "./NonClickableChip.css";

interface NonClickableChipProps {
    name: string;
}

const NonClickableChip = ({ name }: NonClickableChipProps) => {
    return (
        <>
            <div className="chip wrappable-chip">{name}</div>
        </>
    );
};

export default NonClickableChip;
