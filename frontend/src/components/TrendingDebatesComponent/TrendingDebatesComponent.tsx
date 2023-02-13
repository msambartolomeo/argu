import { useEffect, useState } from "react";

import { useTranslation } from "react-i18next";

import { CircularProgress } from "@mui/material";

import "./TrendingDebatesComponent.css";

import { useGetDebates } from "../../hooks/debates/useGetDebates";
import "../../locales/index";
import DebateDto from "../../types/dto/DebateDto";
import DebateOrder from "../../types/enums/DebateOrder";
import DebateStatus from "../../types/enums/DebateStatus";
import DebatesList from "../DebatesList/DebatesList";

const TrendingDebatesComponent = () => {
    const { t } = useTranslation();

    const [debates, setDebates] = useState<DebateDto[]>([]);

    const { loading, getDebates } = useGetDebates();

    useEffect(() => {
        getDebates({
            order: DebateOrder.SUBS_DESC,
            status: DebateStatus.OPEN,
            page: 0,
            size: 3,
        })
            .then((out) => {
                if (out.data) {
                    setDebates(out.data.data);
                }
            })
            .catch((error) => {
                throw new Error("Error loading debates list: ", error);
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
                {loading ? (
                    <CircularProgress data-testid="loading" size={100} />
                ) : (
                    <DebatesList data-testid="debates-list" debates={debates} />
                )}
            </div>
        </div>
    );
};

export default TrendingDebatesComponent;
