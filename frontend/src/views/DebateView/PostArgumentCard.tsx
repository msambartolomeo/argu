import { useTranslation } from "react-i18next";

import TextArea from "../../components/TextArea/TextArea";
import Argument from "../../types/Argument";

interface PostArgumentCardProps {
    handleSubmit: () => void;
    lastArgument: Argument;
    debateCreator: string | undefined;
}

// POST ARGUMENT CARD
const PostArgumentCard = ({
    handleSubmit,
    lastArgument,
    debateCreator,
}: PostArgumentCardProps) => {
    // TODO: Error handling
    const { t } = useTranslation();

    let message = t("debate.argument.argumentMessage");
    let submitMessage = t("debate.argument.postArgument");
    if (
        lastArgument === null ||
        (lastArgument.status === "introduction" &&
            lastArgument.creator.username === debateCreator)
    ) {
        message = t("debate.argument.introMessage");
        submitMessage = t("debate.argument.postIntro");
    } else if (lastArgument.status === "closing") {
        message = t("debate.argument.conclusionMessage");
        submitMessage = t("debate.argument.postConclusion");
    }
    return (
        <form
            encType="multipart/form-data"
            onSubmit={handleSubmit}
            method="post"
            acceptCharset="utf-8"
            id="postArgumentForm"
        >
            <div className="card-content">
                <span className="card-title">{message}</span>
                <TextArea text={t("debate.argument.content")} />
                <div className="image-selector">
                    <div className="file-field input-field">
                        <div className="btn">
                            <label className="white-text">
                                {t("debate.argument.image")}
                            </label>
                            <input type="file" />
                        </div>
                        <div className="file-path-wrapper">
                            <input className="file-path validate" type="text" />
                        </div>
                    </div>
                    <a id="x" className="material-icons">
                        close
                    </a>
                </div>
                <button
                    className="btn waves-effect center-block submitBtn"
                    type="submit"
                    name="argument"
                    form="postArgumentForm"
                >
                    {submitMessage}
                    <i className="material-icons right">send</i>
                </button>
            </div>
        </form>
    );
};

export default PostArgumentCard;
