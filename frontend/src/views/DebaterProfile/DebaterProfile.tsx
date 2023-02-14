import { useCallback, useEffect, useState } from "react";

import { HttpStatusCode } from "axios";
import { useTranslation } from "react-i18next";
import { useParams, useSearchParams } from "react-router-dom";

import { CircularProgress, Pagination } from "@mui/material";

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
import { PAGE_DEFAULT } from "../../types/globalConstants";
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

    const [queryParams, setQueryParams] = useSearchParams();
    let page = parseInt(queryParams.get("page") || PAGE_DEFAULT, 10);

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
            }).then((res: GetDebatesByUrlOutput) => {
                switch (res.status) {
                    case HttpStatusCode.Ok:
                        setUserDebates(res.data);
                        break;
                    case HttpStatusCode.NoContent:
                        setUserDebates(undefined);
                        break;
                }
            });
        }
    }, [userData]);

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

    const handleChangePage = async (value: number) => {
        let url = "";
        switch (value) {
            case 1:
                url = userDebates?.first || "";
                break;
            case userDebates?.totalPages:
                url = userDebates?.last || "";
                break;
            case page - 1:
                url = userDebates?.prev || "";
                break;
            case page + 1:
                url = userDebates?.next || "";
                break;
        }
        const res = await getDebatesByUrl({ url: url });
        switch (res.status) {
            case HttpStatusCode.Ok:
                setUserDebates(res.data);
                break;
            case HttpStatusCode.NoContent:
                setUserDebates(undefined);
                break;
        }
        page = value;
        setQueryParams({ page: value.toString() });
    };

    if (error)
        return <Error status={HttpStatusCode.NotFound} message={error} />;
    if (userLoading || imageLoading || !userData)
        return <CircularProgress size={100} />;

    document.title = `Argu | ${userData.username}`;

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
                    {debatesLoading ? (
                        <CircularProgress size={100} />
                    ) : (
                        <DebatesList debates={userDebates?.data || []} />
                    )}

                    {userDebates && (
                        <div className="pagination-format">
                            <Pagination
                                count={userDebates?.totalPages || 0}
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
        </div>
    );
};
