import { useState } from "react";
import "./UserProfile.css";
import ProfileImage from "../../components/ProfileImage/ProfileImage";
import DebatesList from "../../components/DebatesList/DebatesList";
import EditProfileImageDialog from "../../components/EditProfileImageDialog/EditProfileImageDialog";

const UserProfile = () => {
    const [debates, setDebates] = useState<string[]>([]);

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
