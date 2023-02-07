import { useEffect } from "react";

import { useTranslation } from "react-i18next";

import Pagination from "../../components/Pagination/Pagination";
import Chat from "../../types/Chat";
import User from "../../types/User";
import DebateDto from "../../types/dto/DebateDto";

const chat1: Chat = {
    message: "Message 1",
    createdDate: "2021-01-01",
    selfURL: "",
    creatorURL: "",
    debateURL: "",
};

const chats1: Chat[] = [chat1, chat1, chat1, chat1, chat1, chat1, chat1, chat1];

// CHAT SECTION
interface ChatSectionProps {
    debate?: DebateDto | null;
    userData: User | undefined;
}
const ChatSection = ({ debate, userData }: ChatSectionProps) => {
    const chat: Chat[] = chats1;
    const { t } = useTranslation();
    useEffect(() => {
        const chatElem = document.getElementById("chat");
        chatElem?.scrollTo(0, chatElem.scrollHeight);
    }, []);

    return (
        <>
            {((debate?.status !== t("debate.statuses.statusOpen") &&
                debate?.status !== t("debate.statuses.statusClosing")) ||
                !userData ||
                (userData.username !== debate.creatorName &&
                    userData.username !== debate.opponentName)) && (
                <div className="card">
                    <div className="chat-box card-content">
                        <h5>{t("debate.chat.title")}</h5>
                        <div className="message-container" id="chat">
                            {chat.length > 0 ? (
                                chat.map((c: Chat) => (
                                    <div key={c.selfURL} className="message">
                                        <p className="datetime">
                                            {c.createdDate}
                                        </p>
                                        <p>user</p>
                                        <p>{c.message}</p>
                                    </div>
                                ))
                            ) : (
                                <div className="message">
                                    <h6>{t("debate.chat.noMessages")}</h6>
                                </div>
                            )}
                        </div>
                        {userData &&
                            debate?.status !==
                                t("debate.statuses.statusClosed") &&
                            debate?.status !==
                                t("debate.statuses.statusDeleted") && (
                                <>
                                    <form
                                        method="post"
                                        acceptCharset="utf-8"
                                        id="chatForm"
                                    >
                                        <div className="send-chat">
                                            <div className="input-field flex-grow">
                                                <label>
                                                    {t("debate.chat.message")}
                                                </label>
                                                <input className="materialize-textarea" />
                                            </div>
                                            <button
                                                className="btn waves-effect center-block submitBtn"
                                                type="submit"
                                                name="chat"
                                                form="chatForm"
                                            >
                                                {t("debate.chat.send")}
                                            </button>
                                        </div>
                                    </form>
                                </>
                            )}
                        {userData && <Pagination param="todo" totalPages={1} />}
                    </div>
                </div>
            )}
        </>
    );
};

export default ChatSection;
