import TextArea from "../../components/TextArea/TextArea";
import "./RequestModerator.css";

const RequestModerator = () => {
    // TODO: Error handling and connect to api
    return (
        <div className="card moderator-container">
            <form method="post" acceptCharset="utf-8">
                <div className="card-content">
                    <span className="card-title">
                        Want to become a moderator?
                    </span>
                    <p>
                        Once you become a moderator, you will be able to create
                        your own debates. To prevent our users from
                        misinformation, we ask you to tell us a bit about why
                        you want to become one. We will review it and contact
                        you as soon as possible.
                    </p>
                    <TextArea text="Explain why you should become a moderator" />
                    <button
                        className="btn waves-effect center-block"
                        type="submit"
                        name="action"
                    >
                        Send
                        <i className="material-icons right">send</i>
                    </button>
                </div>
            </form>
        </div>
    );
};

export default RequestModerator;
