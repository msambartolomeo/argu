import React from "react";

import { useTranslation } from "react-i18next";

import "./LandingPageAboutComponent.css";

import createParticipateDebate from "../../assets/create-participate-debate.png";
import debateIcon from "../../assets/debate-icon.png";
import subscribeLikeVote from "../../assets/subscribe-like-vote.png";
import "../../locales/index";

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
    const { t } = useTranslation();

    return (
        <div className="about-component-container">
            <div className="about-container bottom-margin">
                <div className="left-text-container">
                    <h2>{t("landingPage.about.about")}</h2>
                    <h6>{t("landingPage.about.aboutText1")}</h6>
                    <h6>{t("landingPage.about.aboutText2")}</h6>
                    <h6>{t("landingPage.about.aboutText3")}</h6>
                </div>
                <div className="right-img-container white-img resize-img">
                    <img src={debateIconImg} alt="debate icon" />
                </div>
            </div>
            <div className="about-container bottom-margin">
                <div className="left-img-container resize-img">
                    <img src={createParticipateDebateImg} alt="debate icon" />
                </div>
                <div className="right-text-container">
                    <h2>{t("landingPage.about.participate")}</h2>
                    <h6>{t("landingPage.about.participateText1")}</h6>
                    <h6>{t("landingPage.about.participateText2")}</h6>
                    <h6>{t("landingPage.about.participateText3")}</h6>
                </div>
            </div>
            <div className="about-container">
                <div className="left-text-container">
                    <h2>{t("landingPage.about.engage")}</h2>
                    <h6>{t("landingPage.about.engageText1")}</h6>
                    <h6>{t("landingPage.about.engageText2")}</h6>
                    <h6>{t("landingPage.about.engageText3")}</h6>
                </div>
                <div className="right-img-container resize-img">
                    <img src={subscribeLikeVoteImg} alt="debate icon" />
                </div>
            </div>
        </div>
    );
};

export default LandingPageAboutComponent;
