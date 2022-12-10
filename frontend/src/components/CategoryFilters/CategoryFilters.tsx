import { Button, Stack } from "@mui/material";
import DebateCategory from "../../model/enums/DebateCategory";
import "./CategoryFilters.css";
import { useLocation } from "react-router-dom";

const CategoryFilters = () => {
    const categories = Object.values(DebateCategory);

    const search = useLocation().search;
    const selectedCategory = new URLSearchParams(search).get("category");

    return (
        <div className="category-filters-container">
            <h5 className="categories-header">Categories</h5>
            <Stack direction="column" spacing={1} className="categories-stack">
                <Button
                    variant="text"
                    href="/debates"
                    className={`category-button ${
                        selectedCategory === null ? "active" : ""
                    }`}
                >
                    All
                </Button>
                {categories.map((category) => (
                    <Button
                        key={category}
                        variant="text"
                        href={`/debates?category=${category}`}
                        className={`category-button ${
                            category === selectedCategory ? "active" : ""
                        }`}
                    >
                        {category}
                    </Button>
                ))}
            </Stack>
        </div>
        // <div class="category-list z-depth-3">
        //     <ul class="collection with-header">
        //         <li class="collection-header"><h5><spring:message code="pages.debates-list.categories"/></h5></li>
        //         <li>
        //             <a href="<c:url value="/debates" />" class="collection-item waves-effect badge-margin category-button ${ empty
        //             param.category ? "active" : ""}">
        //                 <spring:message code="category.all"/>
        //             </a>
        //         </li>

        //         <c:forEach items="${categories}" var="category">
        //             <li>
        //                 <a href="<c:url value="?category=${category.name}" />"
        //                 class="collection-item waves-effect badge-margin category-button ${category.name == param.       category ?
        //                 "active" : ""}">
        //                     <spring:message code="category.${category.name}"/>
        //                 </a>
        //             </li>
        //         </c:forEach>
        //     </ul>
        // </div>
    );
};

export default CategoryFilters;
