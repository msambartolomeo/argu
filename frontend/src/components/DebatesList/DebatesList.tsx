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
            {debates.map((debate: Debate) => (
                <DebateListItem debate={debate} key={debate.id} />
            ))}
        </>
    );
};

export default DebatesList;
