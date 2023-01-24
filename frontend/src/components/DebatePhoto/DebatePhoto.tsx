import defaultDebatePhoto from "../../assets/debate_stock.png";

interface DebatePhotoProps {
    id?: string;
    alt: string;
}

const DebatePhoto = ({ id = defaultDebatePhoto, alt }: DebatePhotoProps) => {
    return (
        <>
            <div className="image-width">
                <img src={id} alt={alt} className="limit-img responsive-img" />
            </div>
        </>
    );
};

export default DebatePhoto;
