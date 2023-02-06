import { FormEvent, useEffect, useState } from "react";

import {
    createSearchParams,
    useNavigate,
    useSearchParams,
} from "react-router-dom";

import "./SearchBar.css";

interface Props {
    placeholder?: string;
}

function SearchBar({ placeholder }: Props) {
    const [query, setQuery] = useState("");
    const [queryParams, setQueryParams] = useSearchParams();
    const navigate = useNavigate();

    const search = (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        navigate({
            pathname: "/discover",
            search: createSearchParams(queryParams).toString(),
        });
    };

    useEffect(() => {
        const listener = (e: KeyboardEvent) => {
            if (e.key === "Enter" || e.key === "NumPadEnter") {
                search(e as any);
            }
        };
        // TODO: check if this is needed
        // document.addEventListener("keydown", listener);
        // return () => {
        //     document.removeEventListener("keydown", listener);
        // };
    }, [query]);

    useEffect(() => {
        queryParams.delete("page");
        queryParams.delete("order");
        queryParams.delete("status");
        queryParams.delete("search");
        if (query) {
            queryParams.append("search", query);
        }
        setQueryParams(queryParams);
    }, [query]);

    return (
        <form onSubmit={search}>
            <div className="input-field search hide-on-med-and-down">
                <input
                    className="search-input"
                    placeholder={placeholder}
                    type="search"
                    required
                    value={query}
                    onChange={(e) => setQuery(e.target.value)}
                />
                <label className="label-icon active" htmlFor="search">
                    <i className="material-icons">search</i>
                </label>
                <i className="material-icons" onClick={() => setQuery("")}>
                    close
                </i>
            </div>
        </form>
    );
}

export default SearchBar;
