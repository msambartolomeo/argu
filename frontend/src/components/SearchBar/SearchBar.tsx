import { FormEvent, useEffect, useState } from "react";

import { useNavigate, useSearchParams } from "react-router-dom";

import "./SearchBar.css";

import { useNonInitialEffect } from "../../hooks/useNonInitialEffect";

interface Props {
    placeholder?: string;
}

function SearchBar({ placeholder }: Props) {
    const [queryParams, setQueryParams] = useSearchParams();
    const [query, setQuery] = useState(queryParams.get("search") ?? "");
    const navigate = useNavigate();

    const search = (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setQueryParams(queryParams);
        navigate({
            pathname: "/discover",
            search: queryParams.toString(),
        });
    };

    useNonInitialEffect(() => {
        const oldSearch = queryParams.get("search");
        queryParams.delete("page");
        queryParams.delete("search");
        if (query) {
            queryParams.append("search", query);
        } else if (oldSearch) {
            // NOTE: if there was a search param, and query was cleared, now we want to delete it
            setQueryParams(queryParams);
        }
    }, [query]);

    useEffect(() => {
        setQuery(queryParams.get("search") ?? "");
    }, [queryParams]);

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
