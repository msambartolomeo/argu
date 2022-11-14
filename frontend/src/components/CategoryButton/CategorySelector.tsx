import "./CategorySelector.css";
import cn from "classnames";

interface Props {
    categories: Array<string>;
    url: string;
    // NOTE: Maybe we can remove header and all when we have i18n idk
    header: string;
    all: string;
}

// TODO: change a to Link when we have router
function CategorySelector({ categories, url, header, all }: Props) {
    // TODO: Get from router
    const params = new URLSearchParams(document.location.search);
    const currentCategory = params.get("category");

    const categoryButtons = [];
    for (let i = 0; i < categories.length; i++) {
        categoryButtons.push(
            <li key={categories[i]}>
                <a
                    href={`${url}?category=${categories[i]}`}
                    className={cn(
                        "collection-item",
                        "waves-effect",
                        "badge-margin",
                        "category-button",
                        { active: categories[i] === currentCategory }
                    )}
                >
                    {categories[i]}
                </a>
            </li>
        );
    }

    return (
        <ul className="collection with-header">
            <li className="collection-header">
                <h5>{header}</h5>
            </li>
            <li>
                <a
                    href={url}
                    className={cn(
                        "collection-item",
                        "waves-effect",
                        "badge-margin",
                        "category-button",
                        { active: currentCategory === null }
                    )}
                >
                    {all}
                </a>
            </li>
            {categoryButtons}
        </ul>
    );
}

export default CategorySelector;
