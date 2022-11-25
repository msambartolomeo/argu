import "./ProfileImage.css";
import defaultProfilePhoto from "../../assets/default-profile-photo.png";

interface ProfileImageProps {
    image?: string;
}

const ProfileImage = ({ image = defaultProfilePhoto }: ProfileImageProps) => {
    return (
        <>
            <div className="profile-image">
                <img
                    src={image}
                    alt="profile photo"
                    className="responsive-img"
                />
            </div>
        </>
    );
};

export default ProfileImage;
