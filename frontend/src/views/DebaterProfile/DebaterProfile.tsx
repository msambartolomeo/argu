import { CircularProgress, Pagination, PaginationItem } from "@mui/material";
import { useTranslation } from "react-i18next";

import { useCallback, useEffect, useState } from "react";

import { Link, useLocation, useParams } from "react-router-dom";

import { HttpStatusCode } from "axios";

import DebatesList from "../../components/DebatesList/DebatesList";
import ProfileImage from "../../components/ProfileImage/ProfileImage";
import {
    GetDebatesByUrlOutput,
    useGetDebatesByUrl,
} from "../../hooks/debates/useGetDebatesByUrl";
import { GetUserByUrlOutput } from "../../hooks/users/useGetUserByUrl";
import { useGetUserByUsername } from "../../hooks/users/useGetUserByUsername";
import { useGetUserImage } from "../../hooks/users/useGetUserImage";
import { PaginatedList } from "../../types/PaginatedList";
import DebateDto from "../../types/dto/DebateDto";
import UserDto from "../../types/dto/UserDto";
import { Error } from "../Error/Error";
import "../UserProfile/UserProfile.css";

export const DebaterProfile = () => {
    const { t } = useTranslation();
    const params = useParams();
    const [userData, setUserData] = useState<UserDto | undefined>();
    const [userImage, setUserImage] = useState<string | undefined>();
    const [userDebates, setUserDebates] = useState<
        PaginatedList<DebateDto> | undefined
    >();
    const [error, setError] = useState<string | undefined>();

    const location = useLocation();
    const query = new URLSearchParams(location.search);
    const page = parseInt(query.get("page") || "1", 10);

    const { loading: userLoading, getUserByUsername: getUserByUsername } =
        useGetUserByUsername();

    const { loading: imageLoading, getUserImage: getUserImage } =
        useGetUserImage();

    const { loading: debatesLoading, getDebatesByUrl: getDebatesByUrl } =
        useGetDebatesByUrl();

    useEffect(() => {
        const username = params.url || "";
        getUserByUsername({ username: username }).then(
            (res: GetUserByUrlOutput) => {
                switch (res.status) {
                    case HttpStatusCode.Ok:
                        setUserData(res.data);
                        break;
                    default:
                        setError(res.message);
                }
            }
        );
    }, []);

    const fetchUserDebates = useCallback(() => {
        if (userData?.debates) {
            getDebatesByUrl({
                url: userData.debates,
                page: page - 1,
                size: 5,
            }).then((res: GetDebatesByUrlOutput) => {
                switch (res.status) {
                    case HttpStatusCode.Ok:
                        setUserDebates(res.data);
                        break;
                }
            });
        }
    }, [userData, page]);

    useEffect(() => {
        fetchUserDebates();
    }, [fetchUserDebates]);

    const fetchProfileImage = useCallback(() => {
        if (userData?.image) {
            getUserImage(userData.image).then((status) => {
                switch (status) {
                    case HttpStatusCode.NotFound:
                        break;
                    default:
                        setUserImage(userData.image);
                }
            });
        }
    }, [userData]);

    useEffect(() => {
        fetchProfileImage();
    }, [fetchProfileImage]);

    if (error)
        return <Error status={HttpStatusCode.NotFound} message={error} />;
    if (userLoading || debatesLoading || imageLoading || !userData)
        return <CircularProgress size={100} />;

    return (
        <div className="profile-container">
            <div className="card profile-data">
                <ProfileImage image={userImage} />
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

                    <div className="pagination-format">
                        <Pagination
                            count={userDebates?.totalPages || 0}
                            color="primary"
                            className="white"
                            page={page}
                            renderItem={(item) => (
                                <PaginationItem
                                    component={Link}
                                    to={
                                        "/user/" +
                                        userData?.username +
                                        "?page=" +
                                        item.page
                                    }
                                    {...item}
                                />
                            )}
                        />
                    </div>
                </div>
            </div>
        </div>
    );
};
