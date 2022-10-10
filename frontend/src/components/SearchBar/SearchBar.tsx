import "./SearchBar.css";

interface Props {
    placeholder?: string;
}

function SearchBar({ placeholder }: Props) {
    return (
        <div className="input-field search">
            <input
                className="search-input"
                placeholder={placeholder}
                type="search"
                required
            />
            <label className="label-icon active" htmlFor="search">
                <i className="material-icons">search</i>
            </label>
            <i className="material-icons">close</i>
        </div>
    );
}

export default SearchBar;
