import Debate from "../../types/Debate";
import NonClickableChip from "../NonClickableChip/NonClickableChip";
import "./DebateListItem.css";
import defaultDebatePhoto from "../../assets/debate_stock.png";

interface DebateListItemProps {
    debate: Debate;
}

// TODO: Check if creator is received like a string or an URI
const DebateListItem = ({ debate }: DebateListItemProps) => {
    return (
        <div className="list-item">
            <a
                className="card black-text hoverable"
                href={"/debates/" + debate.id}
            >
                <div className="card-content debate-info-holder">
                    <div className="debate-holder-separator">
                        <div className="debate-text-holder">
                            <h5 className="debate-title word-wrap">
                                {debate.name}
                            </h5>
                            <h6>
                                <b>Creator:</b>{" "}
                                {debate.creator.username
                                    ? debate.creator.username
                                    : "[deleted user]"}
                            </h6>
                        </div>
                        <div className="debate-footer">
                            <NonClickableChip name={debate.category} />
                            <NonClickableChip
                                name={"Created: " + debate.createdDate}
                            />
                            <NonClickableChip
                                name={"Status: " + debate.status}
                            />
                            <NonClickableChip
                                name={
                                    "Subscribed users: " + debate.subscriptions
                                }
                            />
                        </div>
                    </div>
                    <div className="image-width">
                        <img
                            src={
                                debate.image
                                    ? "/images/" + debate.image
                                    : defaultDebatePhoto
                            }
                            alt="Debate photo"
                            className="limit-img-sm responsive-img"
                        />
                    </div>
                </div>
            </a>
        </div>
    );
};

export default DebateListItem;
