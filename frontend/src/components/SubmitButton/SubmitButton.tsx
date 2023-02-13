interface ButtonProps {
    text: string;
    disabled?: boolean;
}

const SubmitButton = ({ text, disabled }: ButtonProps) => {
    return (
        <>
            <button
                data-testid="submit-btn"
                type="submit"
                className="btn waves-effect center-block"
                disabled={disabled}
            >
                {text}
                <i className="material-icons right">send</i>
            </button>
        </>
    );
};

export default SubmitButton;
