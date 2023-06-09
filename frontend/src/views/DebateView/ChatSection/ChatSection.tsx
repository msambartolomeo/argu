import { RefObject, useEffect, useRef, useState } from "react";

import { HttpStatusCode } from "axios";
import { useSnackbar } from "notistack";
import { FieldValues, useForm } from "react-hook-form";
import { useTranslation } from "react-i18next";
import { z } from "zod";

import { zodResolver } from "@hookform/resolvers/zod";
import { Pagination } from "@mui/material";

import "./ChatSection.css";

import InputField from "../../../components/InputField/InputField";
import SubmitButton from "../../../components/SubmitButton/SubmitButton";
import { useCreateChat } from "../../../hooks/chats/useCreateChat";
import { useGetChats } from "../../../hooks/chats/useGetChats";
import { useSharedAuth } from "../../../hooks/useAuth";
import { PaginatedList } from "../../../types/PaginatedList";
import ChatDto from "../../../types/dto/ChatDto";
import DebateDto from "../../../types/dto/DebateDto";
import DebateStatus from "../../../types/enums/DebateStatus";
import { PAGE_DEFAULT } from "../../../types/globalConstants";

interface ChatSectionProps {
    debate: DebateDto;
}
const ChatSection = ({ debate }: ChatSectionProps) => {
    const { t } = useTranslation();
    const { enqueueSnackbar } = useSnackbar();

    const { userInfo } = useSharedAuth();

    const form = useRef() as RefObject<HTMLFormElement>;

    const [chat, setChat] = useState<PaginatedList<ChatDto>>(
        PaginatedList.emptyList()
    );
    const [chatUrl, setChatUrl] = useState<string>(debate.chats);
    const [page, setPage] = useState<number>(parseInt(PAGE_DEFAULT));

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

    function callGet() {
        getChats({
            chatUrl,
        }).then((output) => {
            switch (output.status) {
                case HttpStatusCode.Ok:
                    if (output.data) setChat(output.data);
                    break;
                case HttpStatusCode.NoContent:
                    setChat(PaginatedList.emptyList());
                    break;
                default:
                    enqueueSnackbar(t("errors.unexpected"), {
                        variant: "error",
                    });
            }
        });
    }

    const handlePostChat = (data: FieldValues) => {
        createChat({
            chatUrl: debate.chats,
            message: data.message,
        }).then((output) => {
            switch (output.status) {
                case HttpStatusCode.Created:
                    callGet();
                    form.current?.reset();
                    break;
                default:
                    enqueueSnackbar(t("errors.unexpected"), {
                        variant: "error",
                    });
            }
        });
    };

    useEffect(() => {
        callGet();
    }, [debate, chatUrl]);

    useEffect(() => {
        if (page === 1) {
            const chatElem = document.getElementById("chat");
            chatElem?.scrollTo(0, chatElem.scrollHeight);
        }
    }, [chat]);

    const handleChangePage = async (value: number) => {
        if (value === page) return;

        let url = "";
        switch (value) {
            case 1:
                url = chat?.first || "";
                break;
            case chat?.totalPages:
                url = chat?.last || "";
                break;
            case page - 1:
                url = chat?.prev || "";
                break;
            case page + 1:
                url = chat?.next || "";
                break;
        }

        if (url) {
            setChatUrl(url);
            setPage(value);
        }
    };

    return (
        <>
            {((debate.status !== DebateStatus.OPEN &&
                debate.status !== DebateStatus.CLOSING) ||
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
                            debate.status !== DebateStatus.CLOSED &&
                            debate.status !== DebateStatus.DELETED && (
                                <>
                                    <form
                                        acceptCharset="utf-8"
                                        onSubmit={handleSubmit((data) => {
                                            handlePostChat(data);
                                        })}
                                        ref={form}
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
                        {chat.data.length > 0 && (
                            <div className="pagination-format no-background-pag">
                                <Pagination
                                    count={chat?.totalPages || 0}
                                    color="primary"
                                    className="white"
                                    siblingCount={0}
                                    boundaryCount={0}
                                    page={page}
                                    showFirstButton
                                    showLastButton
                                    onChange={(event, page) =>
                                        handleChangePage(page)
                                    }
                                />
                            </div>
                        )}
                    </div>
                </div>
            )}
        </>
    );
};

export default ChatSection;
