import React from "react";
import "./LandingPageAboutComponent.css";
import debateIcon from "../../assets/debate-icon.png";
import createParticipateDebate from "../../assets/create-participate-debate.png";
import subscribeLikeVote from "../../assets/subscribe-like-vote.png";

// TODO: Internationalize
// TODO: Align text and images

interface IconsProps {
    debateIconImg?: string;
    createParticipateDebateImg?: string;
    subscribeLikeVoteImg?: string;
}

const LandingPageAboutComponent = ({
    debateIconImg = debateIcon,
    createParticipateDebateImg = createParticipateDebate,
    subscribeLikeVoteImg = subscribeLikeVote,
}: IconsProps) => {
    return (
        <>
            <div className="about-container">
                <div className="left-text-container">
                    <h2>About Argu</h2>
                    <h6>
                        Argu cuts through the noise typically associated with
                        social media, making it easy to engage in focused
                        discussion.
                    </h6>
                    <h6>
                        Argu allows to visualize debates in a structural manner:
                        starting from introductions, followed by the
                        debaters&apos; arguments and finished by each
                        debater&apos; conclusion.
                    </h6>
                    <h6>
                        Only one debater can argue at the time. The other one
                        has to wait their turn!
                    </h6>
                </div>
                <div className="right-img-container">
                    <img src={debateIconImg} alt="debate icon" />
                </div>
            </div>
            <div className="about-container">
                <div className="left-img-container">
                    <img src={createParticipateDebateImg} alt="debate icon" />
                </div>
                <div className="right-text-container">
                    <h2>Participate in debates!</h2>
                    <h6>
                        Create your own debate or participate in another one.
                    </h6>
                    <h6>Note that only moderators can use this feature.</h6>
                    <h6>
                        If you aren&apos;t a moderator yet, become one by
                        clicking the button above!
                    </h6>
                </div>
            </div>
            <div className="about-container">
                <div className="left-text-container">
                    <h2>Engage in debates</h2>
                    <h6>
                        Subscribe to receive notifications of the debate
                        you&apos;re interested in.
                    </h6>
                    <h6>Like arguments when you agree with the debater.</h6>
                    <h6>
                        Vote for the one you consider is winning the debate!
                    </h6>
                </div>
                <div className="right-img-container">
                    <img src={subscribeLikeVoteImg} alt="debate icon" />
                </div>
            </div>
        </>
    );
};

export default LandingPageAboutComponent;
