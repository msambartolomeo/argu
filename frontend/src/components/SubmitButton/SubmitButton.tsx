interface ButtonProps {
    text: string;
}

const SubmitButton = ({ text }: ButtonProps) => {
    return (
        <>
            <button type="submit" className="btn waves-effect center-block">
                {text}
                <i className="material-icons right">send</i>
            </button>
        </>
    );
};

export default SubmitButton;
