import { useTranslation } from "react-i18next";

import { useParams } from "react-router-dom";

import DebatesList from "../../components/DebatesList/DebatesList";
import ProfileImage from "../../components/ProfileImage/ProfileImage";
import "../UserProfile/UserProfile.css";

export const DebaterProfile = () => {
    const { t } = useTranslation();
    const params = useParams();

    return (
        <div className="profile-container">
            <div className="card profile-data">
                <ProfileImage />
                <h4>username</h4>
                <h5>
                    <i className="material-icons left">stars</i>
                    100
                </h5>
                <h6>{t("profile.createdIn")}: 01/12/22</h6>
            </div>
            <div className="debates-column">
                <div className="card user-debates">
                    <h3 className="center">
                        {t("profile.userDebates", { username: "username" })}
                    </h3>
                    <DebatesList debates={[]} />
                </div>
            </div>
        </div>
    );
};
