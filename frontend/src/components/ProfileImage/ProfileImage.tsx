import { useTranslation } from "react-i18next";

import defaultProfilePhoto from "../../assets/default-profile-photo.png";
import "../../locales/index";
import "./ProfileImage.css";

interface ProfileImageProps {
    image?: string;
}

const ProfileImage = ({ image = defaultProfilePhoto }: ProfileImageProps) => {
    const { t } = useTranslation();

    const profilePhotoAlt: string = t("profile.profilePhotoAlt");

    return (
        <>
            <div className="profile-image">
                <img
                    src={image}
                    alt={profilePhotoAlt}
                    className="responsive-img"
                />
            </div>
        </>
    );
};

export default ProfileImage;
