import React, { useEffect, useState } from "react";
import "./TrendingDebatesComponent.css";
import { useTranslation } from "react-i18next";
import "../../locales/index";
import DebateCategory from "../../types/enums/DebateCategory";
import Debate from "../../types/Debate";
import DebatesList from "../DebatesList/DebatesList";
import DebateDto from "../../types/dto/DebateDto";
import { useGetDebates } from "../../hooks/debates/useGetDebates";
import DebateOrder from "../../types/enums/DebateOrder";
import DebateStatus from "../../types/enums/DebateStatus";

// TODO: Add icons

const TrendingDebatesComponent = () => {
    const { t } = useTranslation();

    const {
        data: debatesList,
        loading: isLoading,
        getDebates: getDebates,
    } = useGetDebates();

    useEffect(() => {
        getDebates({
            order: DebateOrder.SUBS_DESC,
            status: DebateStatus.OPEN,
            page: 0,
            size: 3,
        }).catch((error) => {
            console.log("Error loading debates list: ", error);
        });
    }, []);

    return (
        <div className="trending-debates-component-container">
            <h2 className="title-container">
                <i className="medium material-icons left">
                    local_fire_department
                </i>
                {t("landingPage.trending.title")}
            </h2>
            <div className="debate-list-container">
                <DebatesList debates={debatesList} />
            </div>
        </div>
    );
};

export default TrendingDebatesComponent;
