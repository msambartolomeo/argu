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
    // TODO: Fix selected button (find reason why it renderizes when it shouldn't)
    const [showMyDebates, setShowMyDebates] = useState<boolean>(false);

    const handleClick = (value: boolean) => {
        setShowMyDebates(value);
    };

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
                <h6>Created in: 01/12/22</h6>
                <a
                    className="waves-effect waves-light btn logout-btn"
                    href="/logout"
                >
                    <i className="material-icons left">logout</i>
                    Logout
                </a>
                <DeleteAccountModal />
            </div>
            <div className="debates-column">
                <div className="section">
                    <a
                        href="/profile?list=subscribed"
                        onClick={() => handleClick(false)}
                        className={cn("waves-effect btn-large", {
                            active: !showMyDebates,
                        })}
                    >
                        Debates Subscribed
                    </a>
                    <a
                        href="/profile?list=mydebates"
                        onClick={() => handleClick(true)}
                        className={cn("waves-effect btn-large", {
                            active: showMyDebates,
                        })}
                        // onSelect={() => handleClick(true)}
                    >
                        My Debates
                    </a>
                </div>
                <div className="card user-debates">
                    <h3 className="center">Username&apos;s debates</h3>
                    <DebatesList debates={debates} />
                </div>
            </div>
        </div>
    );
};

export default UserProfile;
