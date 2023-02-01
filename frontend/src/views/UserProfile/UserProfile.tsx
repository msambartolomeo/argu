import { useState } from "react";
import "./UserProfile.css";
import ProfileImage from "../../components/ProfileImage/ProfileImage";
import DebatesList from "../../components/DebatesList/DebatesList";
import EditProfileImageDialog from "../../components/EditProfileImageDialog/EditProfileImageDialog";
import DeleteAccountModal from "../../components/DeleteAccountModal/DeleteAccountModal";
import "../../root.css";
import cn from "classnames";
import { useTranslation } from "react-i18next";
import "../../locales/index";
import DebateDto from "../../types/dto/DebateDto";
import DebateCategory from "../../types/enums/DebateCategory";
import DebateStatus from "../../types/enums/DebateStatus";

const UserProfile = () => {
    const debates: DebateDto[] = [
        {
            id: 1,
            name: "Is the earth flat?",
            description:
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec auctor, nisl eget ultricies tincidunt, nunc nisl aliquam nisl, eget aliquam nunc nisl euismod nunc. Donec auctor, nisl eget ultricies tincidunt, nunc nisl aliquam nisl, eget aliquam nunc nisl euismod nunc.",
            isCreatorFor: true,
            category: DebateCategory.SCIENCE,
            status: DebateStatus.OPEN,
            createdDate: new Date("2021-05-01"),
            subscriptionsCount: 3,
            votesFor: 2,
            votesAgainst: 1,
            creatorName: "user1",
            self: "",
            image: "",
            opponent: "",
            arguments: "",
            chats: "",
        },
    ];

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
                            "btn-active": !showMyDebates,
                        })}
                    >
                        {t("profile.debatesSubscribed")}
                    </a>
                    <a
                        onClick={() => setShowMyDebates(true)}
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
                    <DebatesList debates={debates} />
                </div>
            </div>
        </div>
    );
};

export default UserProfile;
