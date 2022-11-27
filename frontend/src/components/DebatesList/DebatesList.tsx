import Debate from "../../types/Debate";
import DebateListItem from "../DebateListItem/DebateListItem";

interface DebatesListProps {
    debates: Debate[];
}

const DebatesList = ({ debates }: DebatesListProps) => {
    if (debates.length === 0) {
        return <h5>No debates found.</h5>;
    }
    return (
        <>
            {/* TODO: Crear clase debate */}
            {debates.map((debate: Debate) => (
                <div className="list-item" key={debate.id}>
                    <DebateListItem debate={debate} />
                </div>
            ))}
        </>
    );
};

export default DebatesList;
