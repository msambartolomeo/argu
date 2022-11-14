import { FormEvent, useState } from "react";
import "./SearchBar.css";

interface Props {
    placeholder?: string;
}

function SearchBar({ placeholder }: Props) {
    const [query, setQuery] = useState("");

    const search = (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        // TODO: Handle search
    };

    return (
        <form onSubmit={search}>
            <div className="input-field search">
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
                <i className="material-icons">close</i>
            </div>
        </form>
    );
}

export default SearchBar;
