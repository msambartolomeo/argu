import { useEffect, useState } from "react";

import { HttpStatusCode } from "axios";
import cn from "classnames";
import { useTranslation } from "react-i18next";
import {
    Link,
    createSearchParams,
    useNavigate,
    useSearchParams,
} from "react-router-dom";

import { CircularProgress, Pagination, PaginationItem } from "@mui/material";

import "./UserProfile.css";

import DebatesList from "../../components/DebatesList/DebatesList";
import DeleteAccountModal from "../../components/DeleteAccountModal/DeleteAccountModal";
import EditProfileImageDialog from "../../components/EditProfileImageDialog/EditProfileImageDialog";
import ProfileImage from "../../components/ProfileImage/ProfileImage";
import { useGetDebatesByUrl } from "../../hooks/debates/useGetDebatesByUrl";
import { useSharedAuth } from "../../hooks/useAuth";
import { GetUserByUrlOutput } from "../../hooks/users/useGetUserByUrl";
import { useGetUserByUsername } from "../../hooks/users/useGetUserByUsername";
import "../../locales/index";
import "../../root.css";
import { PaginatedList } from "../../types/PaginatedList";
import DebateDto from "../../types/dto/DebateDto";
import UserDto from "../../types/dto/UserDto";
import { Error } from "../Error/Error";

const UserProfile = () => {
    const [userData, setUserData] = useState<UserDto | undefined>(undefined);
    // TODO: Make pagination use links provided by API
    const [debates, setDebates] = useState<
        PaginatedList<DebateDto> | undefined
    >();
    const [showMyDebates, setShowMyDebates] = useState<boolean>(false);
    const [reloadImage, setReloadImage] = useState<number>(1);
    const [error, setError] = useState<GetUserByUrlOutput | undefined>(
        undefined
    );

    const { t } = useTranslation();
    const navigate = useNavigate();

    const { userInfo } = useSharedAuth();

    const [queryParams, setQueryParams] = useSearchParams();
    let page = parseInt(queryParams.get("page") || "1", 10);

    const { loading: isUserLoading, getUserByUsername: getUser } =
        useGetUserByUsername();

    const { loading: isUserDebatesLoading, getDebatesByUrl: getDebates } =
        useGetDebatesByUrl();

    const { callLogout } = useSharedAuth();
    const logout = () => {
        callLogout();
        navigate("/login");
    };

    const handleUpdateImage = () => {
        setReloadImage(Date.now());
    };

    const handleUpdateDebates = (value: boolean) => {
        page = 1;
        queryParams.delete("page");
        setQueryParams(queryParams);
        setShowMyDebates(value);
    };

    useEffect(() => {
        if (userInfo) {
            getUser({ username: userInfo.username }).then((res) => {
                switch (res.status) {
                    case HttpStatusCode.Ok:
                        setUserData(res.data);
                        break;
                    default:
                        setError(res);
                }
            });
        } else {
            navigate("/login");
        }
    }, [userInfo]);

    useEffect(() => {
        if (userData) {
            const url =
                (showMyDebates
                    ? userData?.debates
                    : userData?.subscribedDebates) || "";
            getDebates({ url: url, page: page - 1, size: 5 }).then((res) => {
                switch (res.status) {
                    case HttpStatusCode.Ok:
                        setDebates(res.data);
                        break;
                    case HttpStatusCode.NoContent:
                        setDebates(undefined);
                        break;
                }
            });
        }
    }, [userData, showMyDebates, page]);

    if (error) return <Error status={error.status} message={error.message} />;

    if (isUserLoading) return <CircularProgress size={100} />;

    return (
        <div className="profile-container">
            <div className="card profile-data">
                <ProfileImage
                    image={userData?.image || ""}
                    reloadImage={reloadImage}
                />
                <EditProfileImageDialog
                    imageUrl={userData?.image || ""}
                    imageChange={handleUpdateImage}
                />
                <h4>{userData?.username}</h4>
                <h5>
                    <i className="material-icons left">stars</i>
                    {userData?.points}
                </h5>
                <div className="email-format">
                    <i className="material-icons">email</i>
                    <h6>{userInfo?.email}</h6>
                </div>
                <h6>
                    {t("profile.createdIn")}: {userData?.createdDate}
                </h6>
                <div
                    className="waves-effect waves-light btn logout-btn"
                    onClick={logout}
                >
                    <i className="material-icons left">logout</i>
                    {t("profile.logout")}
                </div>
                <DeleteAccountModal />
            </div>
            <div className="debates-column">
                <div className="section">
                    <a
                        onClick={() => handleUpdateDebates(false)}
                        className={cn("waves-effect btn-large", {
                            "btn-active": !showMyDebates,
                        })}
                    >
                        {t("profile.debatesSubscribed")}
                    </a>
                    <a
                        onClick={() => handleUpdateDebates(true)}
                        className={cn("waves-effect btn-large", {
                            "btn-active": showMyDebates,
                        })}
                    >
                        {t("profile.myDebates")}
                    </a>
                </div>
                <div className="card user-debates">
                    <h3 className="center">
                        {t("profile.userDebates", { username: "username" })}
                    </h3>
                    {isUserDebatesLoading ? (
                        <CircularProgress size={100} />
                    ) : (
                        <DebatesList debates={debates?.data || []} />
                    )}

                    {debates && (
                        <div className="pagination-format">
                            <Pagination
                                count={debates?.totalPages || 0}
                                color="primary"
                                className="white"
                                page={page}
                                renderItem={(item) => (
                                    <PaginationItem
                                        component={Link}
                                        to={{
                                            pathname: "/profile",
                                            search: createSearchParams({
                                                page:
                                                    item.page?.toString() ||
                                                    "1",
                                            }).toString(),
                                        }}
                                        {...item}
                                    />
                                )}
                            />
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default UserProfile;
