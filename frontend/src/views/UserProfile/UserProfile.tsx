import React, { useState } from "react";
import "./UserProfile.css";
import ProfileImage from "../../components/ProfileImage/ProfileImage";
import DebatesList from "../../components/DebatesList/DebatesList";
import EditProfileImageDialog from "../../components/EditProfileImageDialog/EditProfileImageDialog";
import DeleteAccountModal from "../../components/DeleteAccountModal/DeleteAccountModal";
import Debate from "../../types/Debate";
import User from "../../types/User";
import "../../root.css";
import cn from "classnames";
import { useTranslation } from "react-i18next";
import "../../locales/index";

const UserProfile = () => {
    const user1: User = {
        username: "User 1",
        email: "user1@mail.com",
        createdDate: "2021-01-01",
    };
    const debate1: Debate = {
        id: 1,
        name: "Debate 1",
        description: "Description 1",
        isCreatorFor: true,
        category: "Category 1",
        createdDate: "2021-01-01",
        status: "Status 1",
        subscriptions: 1,
        votesFor: 0,
        votesAgainst: 0,
        creator: user1,
    };
    const debates: Debate[] = [debate1];

    const [showMyDebates, setShowMyDebates] = useState<boolean>(false);

    const { t } = useTranslation();

    // TODO: Implement i18n

    return (
        <div className="profile-container">
            <div className="card profile-data">
                <ProfileImage />
                <EditProfileImageDialog />
                <h4>username</h4>
                <h5>
                    <i className="material-icons left">stars</i>
                    100
                </h5>
                <div className="email-format">
                    <i className="material-icons">email</i>
                    <h6>example@email.com</h6>
                </div>
                <h6>{t("profile.createdIn")}: 01/12/22</h6>
                <a
                    className="waves-effect waves-light btn logout-btn"
                    href="/logout"
                >
                    <i className="material-icons left">logout</i>
                    {t("profile.logout")}
                </a>
                <DeleteAccountModal />
            </div>
            <div className="debates-column">
                <div className="section">
                    <a
                        onClick={() => setShowMyDebates(false)}
                        className={cn("waves-effect btn-large", {
                            active: !showMyDebates,
                        })}
                    >
                        {t("profile.debatesSubscribed")}
                    </a>
                    <a
                        onClick={() => setShowMyDebates(true)}
                        className={cn("waves-effect btn-large", {
                            active: showMyDebates,
                        })}
                    >
                        {t("profile.myDebates")}
                    </a>
                </div>
                <div className="card user-debates">
                    <h3 className="center">
                        {t("profile.userDebates", { username: "username" })}
                    </h3>
                    {/* <DebatesList debates={debates} /> */}
                </div>
            </div>
        </div>
    );
};

export default UserProfile;
