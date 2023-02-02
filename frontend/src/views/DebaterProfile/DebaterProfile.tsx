import { CircularProgress } from "@mui/material";
import { useTranslation } from "react-i18next";

import { useEffect, useState } from "react";

import { useParams } from "react-router-dom";

import { HttpStatusCode } from "axios";

import DebatesList from "../../components/DebatesList/DebatesList";
import ProfileImage from "../../components/ProfileImage/ProfileImage";
import {
    GetDebatesByUrlOutput,
    useGetDebatesByUrl,
} from "../../hooks/debates/useGetDebatesByUrl";
import { GetUserByUrlOutput } from "../../hooks/users/useGetUserByUrl";
import { useGetUserByUsername } from "../../hooks/users/useGetUserByUsername";
import { PaginatedList } from "../../types/PaginatedList";
import DebateDto from "../../types/dto/DebateDto";
import UserDto from "../../types/dto/UserDto";
import { Error } from "../Error/Error";
import "../UserProfile/UserProfile.css";

export const DebaterProfile = () => {
    const { t } = useTranslation();
    const params = useParams();
    const [userData, setUserData] = useState<UserDto | undefined>();
    const [userDebates, setUserDebates] = useState<
        PaginatedList<DebateDto> | undefined
    >();
    const [error, setError] = useState<string | undefined>();

    const { loading: userLoading, getUserByUsername: getUserByUsername } =
        useGetUserByUsername();

    const { loading: debatesLoading, getDebatesByUrl: getDebatesByUrl } =
        useGetDebatesByUrl();

    useEffect(() => {
        const username = params.url || "";
        getUserByUsername({ username: username }).then(
            (res: GetUserByUrlOutput) => {
                if (res.status === HttpStatusCode.Ok) {
                    setUserData(res.data);
                } else {
                    setError(res.message);
                }
            }
        );
    }, []);

    useEffect(() => {
        if (userData?.debates) {
            getDebatesByUrl({
                url: userData.debates,
                size: 5,
            }).then((res: GetDebatesByUrlOutput) => {
                if (res.status === HttpStatusCode.Ok) {
                    setUserDebates(res.data);
                }
            });
        }
    }, [userData]);

    if (error)
        return <Error status={HttpStatusCode.NotFound} message={error} />;
    if (userLoading || debatesLoading || !userData || !userDebates)
        return <CircularProgress size={100} />;
    return (
        <div className="profile-container">
            <div className="card profile-data">
                <ProfileImage />
                <h4>{userData?.username}</h4>
                <h5>
                    <i className="material-icons left">stars</i>
                    {userData?.points}
                </h5>
                <h6>
                    {t("profile.createdIn")}: {userData?.createdDate}
                </h6>
            </div>
            <div className="debates-column">
                <div className="card user-debates">
                    <h3 className="center">
                        {t("profile.userDebates", { username: "username" })}
                    </h3>
                    <DebatesList debates={userDebates?.data || []} />
                </div>
            </div>
        </div>
    );
};
