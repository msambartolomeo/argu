import { RefObject, useEffect, useRef, useState } from "react";

import { HttpStatusCode } from "axios";
import { FieldValues, useForm } from "react-hook-form";
import { useTranslation } from "react-i18next";
import { z } from "zod";

import { zodResolver } from "@hookform/resolvers/zod";

import InputField from "../../components/InputField/InputField";
import Pagination from "../../components/Pagination/Pagination";
import SubmitButton from "../../components/SubmitButton/SubmitButton";
import { useCreateChat } from "../../hooks/chats/useCreateChat";
import { useGetChats } from "../../hooks/chats/useGetChats";
import { useSharedAuth } from "../../hooks/useAuth";
import { PaginatedList } from "../../types/PaginatedList";
import ChatDto from "../../types/dto/ChatDto";
import DebateDto from "../../types/dto/DebateDto";

interface ChatSectionProps {
    debate: DebateDto;
}
const ChatSection = ({ debate }: ChatSectionProps) => {
    const { t } = useTranslation();

    const { userInfo } = useSharedAuth();

    const form = useRef() as RefObject<HTMLFormElement>;

    const [chat, setChat] = useState<PaginatedList<ChatDto>>(
        PaginatedList.emptyList()
    );
    const [refresh, setRefresh] = useState<boolean>(true);

    const schema = z.object({
        message: z
            .string()
            .min(1, t("debate.chat.errors.messageEmpty") as string)
            .max(2000, t("debate.chat.errors.messageTooLong") as string),
    });
    const {
        register,
        handleSubmit,
        formState: { errors },
    } = useForm({
        resolver: zodResolver(schema),
    });

    const { getChats } = useGetChats();
    const { loading: postLoading, createChat } = useCreateChat();

    const handlePostChat = (data: FieldValues) => {
        createChat({
            chatUrl: debate.chats,
            message: data.message,
        }).then((output) => {
            switch (output.status) {
                case HttpStatusCode.Created:
                    setRefresh(true);
                    form.current?.reset();
                    break;
                default:
                // TODO: Error
            }
        });
    };

    useEffect(() => {
        if (refresh) {
            getChats({
                chatUrl: debate.chats,
                // TODO: page
            }).then((output) => {
                switch (output.status) {
                    case HttpStatusCode.Ok:
                        if (output.data) setChat(output.data);
                        break;
                    case HttpStatusCode.NoContent:
                        setChat(PaginatedList.emptyList());
                        break;
                }
            });
            setRefresh(false);
        }

        // FIXME: the scrolling is not working after a post
        const chatElem = document.getElementById("chat");
        chatElem?.scrollTo(0, chatElem.scrollHeight);
    }, [refresh]);

    return (
        <>
            {((debate.status !== t("debate.statuses.statusOpen") &&
                debate.status !== t("debate.statuses.statusClosing")) ||
                !userInfo ||
                (userInfo.username !== debate.creatorName &&
                    userInfo.username !== debate.opponentName)) && (
                <div className="card">
                    <div className="chat-box card-content">
                        <h5>{t("debate.chat.title")}</h5>
                        <div className="message-container" id="chat">
                            {chat.data.length > 0 ? (
                                chat.data.map((c) => (
                                    <div key={c.self} className="message">
                                        <p className="datetime">
                                            {c.createdDate}
                                        </p>
                                        <p>
                                            {c.creatorName ??
                                                t("debate.userDeleted")}
                                        </p>
                                        <p>{c.message}</p>
                                    </div>
                                ))
                            ) : (
                                <div className="message">
                                    <h6>{t("debate.chat.noMessages")}</h6>
                                </div>
                            )}
                        </div>
                        {userInfo &&
                            debate?.status !==
                                t("debate.statuses.statusClosed") &&
                            debate?.status !==
                                t("debate.statuses.statusDeleted") && (
                                <>
                                    <form
                                        acceptCharset="utf-8"
                                        onSubmit={handleSubmit((data) => {
                                            handlePostChat(data);
                                        })}
                                    >
                                        <div className="send-chat">
                                            <InputField
                                                className="flex-grow"
                                                text={t("debate.chat.message")}
                                                register={register("message")}
                                                error={
                                                    errors.message
                                                        ?.message as string
                                                }
                                            />
                                            <SubmitButton
                                                text={t("debate.chat.send")}
                                                disabled={postLoading}
                                            />
                                        </div>
                                    </form>
                                </>
                            )}
                        {userInfo && <Pagination param="todo" totalPages={1} />}
                    </div>
                </div>
            )}
        </>
    );
};

export default ChatSection;
