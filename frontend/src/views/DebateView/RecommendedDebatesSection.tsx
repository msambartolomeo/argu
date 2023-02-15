import { useEffect, useState } from "react";

import { HttpStatusCode } from "axios";
import { useTranslation } from "react-i18next";

import { CircularProgress } from "@mui/material";

import DebateListItem from "../../components/DebateListItem/DebateListItem";
import {
    GetDebatesByUrlOutput,
    useGetDebatesByUrl,
} from "../../hooks/debates/useGetDebatesByUrl";
import DebateDto from "../../types/dto/DebateDto";

interface RecommendedDebatesSectionProps {
    debate: DebateDto;
}

const RecommendedDebatesSection = ({
    debate,
}: RecommendedDebatesSectionProps) => {
    const { t } = useTranslation();
    const [slideIndex, setSlideIndex] = useState(0);

    const [recommendedDebates, setRecommendedDebates] = useState<DebateDto[]>(
        []
    );

    const {
        loading: isRecommendedDebatesLoading,
        getDebatesByUrl: getRecommendedDebates,
    } = useGetDebatesByUrl();

    useEffect(() => {
        if (debate?.recommendations) {
            getRecommendedDebates({
                url: debate?.recommendations,
            }).then((output: GetDebatesByUrlOutput) => {
                if (output.status === HttpStatusCode.Ok) {
                    setRecommendedDebates(output.data?.data ?? []);
                }
            });
        }
    }, [debate]);

    if (isRecommendedDebatesLoading) {
        return <CircularProgress size={100} />;
    }

    return (
        <>
            {recommendedDebates.length > 0 && (
                <div className="card vote-section">
                    <h5>{t("debate.recommendedDebates")}</h5>
                    <div className="row">
                        <div className="slideshow-container">
                            {recommendedDebates.map((d: DebateDto, index) => (
                                <div
                                    key={d.id}
                                    className="fade"
                                    style={{
                                        display:
                                            slideIndex === index
                                                ? "block"
                                                : "none",
                                    }}
                                >
                                    <DebateListItem debate={d} />
                                </div>
                            ))}
                        </div>
                    </div>
                    <div className="image-selector">
                        {recommendedDebates.length > 1 && (
                            <a
                                className="prev btn image-selector"
                                onClick={() =>
                                    setSlideIndex(
                                        slideIndex === 0
                                            ? recommendedDebates.length - 1
                                            : slideIndex - 1
                                    )
                                }
                            >
                                ❮
                            </a>
                        )}
                        {recommendedDebates.length > 1 && (
                            <a
                                className="next btn image-selector"
                                onClick={() =>
                                    setSlideIndex(
                                        slideIndex ===
                                            recommendedDebates.length - 1
                                            ? 0
                                            : slideIndex + 1
                                    )
                                }
                            >
                                ❯
                            </a>
                        )}
                    </div>
                </div>
            )}
        </>
    );
};

export default RecommendedDebatesSection;
