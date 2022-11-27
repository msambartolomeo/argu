import { useState } from "react";
import "./UserProfile.css";
import ProfileImage from "../../components/ProfileImage/ProfileImage";
import DebatesList from "../../components/DebatesList/DebatesList";
import EditProfileImageDialog from "../../components/EditProfileImageDialog/EditProfileImageDialog";
import DeleteAccountModal from "../../components/DeleteAccountModal/DeleteAccountModal";
import Debate from "../../types/Debate";

const UserProfile = () => {
    const aux: Debate = {
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
        creator: {
            username: "User 1",
            email: "user1@mail.com",
            createdDate: "2021-01-01",
        },
    };
    const [debates, setDebates] = useState<Debate[]>([aux]);
    // setDebates([aux]);

    // TODO: useEffect to change debates shown

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
                        className="waves-effect btn-large"
                    >
                        Subscribed
                    </a>
                    <a
                        href="/profile?list=mydebates"
                        className="waves-effect btn-large"
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
