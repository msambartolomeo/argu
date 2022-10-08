import "./Pagination.css";
import cn from "classnames";

interface Props {
    param: string;
    totalPages: number;
}

function Pagination({ param, totalPages }: Props) {
    // TODO: Get page number from router
    const params = new URLSearchParams(document.location.search);
    let paramValue = params.get(param);
    if (paramValue === null) {
        paramValue = "0";
    }
    const currentPage = parseInt(paramValue);

    // TODO: Get url from router and keep current params
    const getUrl = (page: number) => `http://localhost:3000?${param}=${page}`;

    const buttons = [];
    for (let i = 0; i < totalPages; i++) {
        buttons.push(
            <li
                key={i}
                className={cn({
                    active: true,
                    "selected-button": i === currentPage,
                })}
            >
                <a href={getUrl(i)}>{i}</a>
            </li>
        );
    }

    return (
        <>
            {totalPages > 0 && (
                <div className="center pagination-margin">
                    <ul className="pagination">
                        {currentPage > 0 && (
                            <li className="active">
                                <a href={getUrl(currentPage - 1)}>{"<"}</a>
                            </li>
                        )}

                        {buttons}

                        {totalPages > currentPage + 1 && (
                            <li className="active">
                                <a href={getUrl(currentPage + 1)}>{">"}</a>
                            </li>
                        )}
                    </ul>
                </div>
            )}
        </>
    );
}

export default Pagination;
