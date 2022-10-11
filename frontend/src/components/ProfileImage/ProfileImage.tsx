import "./ProfileImage.css";

interface ProfileImageProps {
    image?: string;
}

const ProfileImage = ({ image }: ProfileImageProps) => {
    return (
        <>
            <div>
                <img
                    src={image}
                    alt="profile photo"
                    className="responsive-img"
                />
            </div>
        </>
    );
};

ProfileImage.defaultProps = {
    image: "/user-profile-default.png",
};

export default ProfileImage;
