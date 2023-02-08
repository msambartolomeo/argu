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
}: SelectDropdownProps) => {
    return (
        <div className="select-container">
            <Select
                form="create-product-form"
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
};

export default SelectDropdown;
