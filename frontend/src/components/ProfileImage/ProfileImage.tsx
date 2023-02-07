import { useTranslation } from "react-i18next";

import "./ProfileImage.css";

import defaultProfilePhoto from "../../assets/default-profile-photo.png";
import "../../locales/index";

interface ProfileImageProps {
    image?: string;
    reloadImage?: number;
}

const ProfileImage = ({
    image = defaultProfilePhoto,
    reloadImage,
}: ProfileImageProps) => {
    const { t } = useTranslation();

    const profilePhotoAlt: string = t("profile.profilePhotoAlt");

    return (
        <>
            <div className="profile-image">
                <img
                    key={reloadImage}
                    src={image ? image : defaultProfilePhoto}
                    alt={profilePhotoAlt}
                    defaultValue={defaultProfilePhoto}
                    onError={({ target }) => {
                        (target as HTMLImageElement).src = defaultProfilePhoto;
                    }}
                    className="responsive-img"
                />
            </div>
        </>
    );
};

export default ProfileImage;
