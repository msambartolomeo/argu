interface DebaterDisplayProps {
    debater?: string;
    position: string;
}

const DebaterDisplay = ({
    debater = "[deleted]",
    position,
}: DebaterDisplayProps) => {
    const handleGoToProfile = () => {
        // TODO: Implement go to profile call
    };

    return (
        <div className="username-container">
            <h6>
                <b>{position}</b>
            </h6>
            {debater !== "[deleted]" ? (
                <a className="link" onClick={handleGoToProfile}>
                    {debater}
                </a>
            ) : (
                <i>{debater}</i>
            )}
        </div>
    );
};

export default DebaterDisplay;
