import { useEffect, useState } from "react";

import { HttpStatusCode } from "axios";
import { useSnackbar } from "notistack";
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
    const { enqueueSnackbar } = useSnackbar();

    useEffect(() => {
        getDebates({
            order: DebateOrder.SUBS_DESC,
            status: DebateStatus.OPEN,
            page: 0,
            size: 3,
        }).then((response) => {
            switch (response.status) {
                case HttpStatusCode.Ok:
                    if (response.data) setDebates(response.data.data);
                    break;
                default:
                    enqueueSnackbar(t("errors.unexpected"), {
                        variant: "error",
                    });
                    break;
            }
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
                    <CircularProgress size={100} />
                ) : (
                    <DebatesList debates={debates} />
                )}
            </div>
        </div>
    );
};

export default TrendingDebatesComponent;
