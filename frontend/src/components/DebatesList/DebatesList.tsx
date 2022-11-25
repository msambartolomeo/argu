interface DebatesListProps {
    debates: string[];
}

const DebatesList = ({ debates }: DebatesListProps) => {
    if (debates.length === 0) {
        return <h5>No debates found.</h5>;
    }
    return (
        <>
            {/* TODO: Crear clase debate */}
            {debates.map((debate: string) => (
                <div className="list-item" key={debate}>
                    <div>Item</div>
                </div>
            ))}
        </>
    );
};

export default DebatesList;
