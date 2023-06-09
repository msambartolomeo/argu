import { useEffect, useState } from "react";

import { HttpStatusCode } from "axios";
import cn from "classnames";
import { useTranslation } from "react-i18next";
import { useNavigate, useSearchParams } from "react-router-dom";

import { CircularProgress, Pagination } from "@mui/material";

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
import { PAGE_DEFAULT } from "../../types/globalConstants";
import { Error } from "../Error/Error";

const UserProfile = () => {
    const [userData, setUserData] = useState<UserDto | undefined>(undefined);
    const [debates, setDebates] = useState<
        PaginatedList<DebateDto> | undefined
    >();
    const [queryParams, setQueryParams] = useSearchParams();
    const [showMyDebates, setShowMyDebates] = useState<boolean>(() => {
        return queryParams.get("showMyDebates") === "true";
    });
    const [reloadImage, setReloadImage] = useState<number>(1);
    const [error, setError] = useState<GetUserByUrlOutput | undefined>(
        undefined
    );

    const { t } = useTranslation();
    const navigate = useNavigate();

    const { userInfo } = useSharedAuth();

    let page = parseInt(queryParams.get("page") || PAGE_DEFAULT, 10);

    const { loading: isUserLoading, getUserByUsername: getUser } =
        useGetUserByUsername();

    const { loading: isUserDebatesLoading, getDebatesByUrl: getDebatesByUrl } =
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
        if (value) queryParams.set("showMyDebates", "true");
        else queryParams.set("showMyDebates", "false");
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
                    case HttpStatusCode.Unauthorized:
                        logout();
                        break;
                    default:
                        setError(res);
                }
            });
        } else {
            navigate("/login");
        }
        if (queryParams.get("showMyDebates") === "true") setShowMyDebates(true);
        else setShowMyDebates(false);
    }, [userInfo, reloadImage]);

    useEffect(() => {
        if (userData) {
            let url =
                (showMyDebates
                    ? userData?.debates
                    : userData?.subscribedDebates) || "";
            url = url + `&page=${page - 1}`;
            getDebatesByUrl({ url: url }).then((res) => {
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
    }, [userData, showMyDebates]);

    const handleChangePage = async (value: number) => {
        if (value === page) return;

        let url = "";
        switch (value) {
            case 1:
                url = debates?.first || "";
                break;
            case debates?.totalPages:
                url = debates?.last || "";
                break;
            case page - 1:
                url = debates?.prev || "";
                break;
            case page + 1:
                url = debates?.next || "";
                break;
        }
        if (url) {
            const res = await getDebatesByUrl({ url: url });
            switch (res.status) {
                case HttpStatusCode.Ok:
                    setDebates(res.data);
                    break;
                case HttpStatusCode.NoContent:
                    setDebates(undefined);
                    break;
            }
            page = value;
            queryParams.set("page", page.toString());
            setQueryParams(queryParams);
        }
    };

    if (error) return <Error status={error.status} message={error.message} />;

    if (isUserLoading && !userData) return <CircularProgress size={100} />;

    document.title =
        "Argu" + (userData?.username ? ` | ${userData?.username}` : "");

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
                        {t("profile.userDebates", {
                            username: userData?.username,
                        })}
                    </h3>
                    {isUserDebatesLoading ? (
                        <CircularProgress size={100} />
                    ) : (
                        <DebatesList debates={debates?.data || []} />
                    )}

                    {debates && (
                        <div className="pagination-format no-background-pag">
                            <Pagination
                                count={debates?.totalPages || 0}
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

export default UserProfile;
