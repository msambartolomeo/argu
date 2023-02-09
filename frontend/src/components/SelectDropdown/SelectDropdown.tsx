import Select from "react-select";

import "./SelectDropdown.css";

interface SelectDropdownProps {
    suppliers: { value: string; label: string }[];
    error?: string;
    placeholder?: string;
}

const SelectDropdown = ({
    suppliers,
    placeholder,
    // TODO: Add error management
    error,
}: SelectDropdownProps) => (
    <div className="select-container">
        <Select
            menuPortalTarget={document.body}
            styles={{ menuPortal: (base) => ({ ...base, zIndex: 9999 }) }}
            options={suppliers}
            isSearchable={true}
            isClearable={true}
            required={true}
            placeholder={placeholder}
        />
    </div>
);

export default SelectDropdown;
