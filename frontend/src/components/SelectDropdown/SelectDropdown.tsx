import { useState } from "react";

import Select from "react-select";

import "./SelectDropdown.css";

interface SelectDropdownProps {
    suppliers: { value: string; label: string }[];
}

const SelectDropdown = ({ suppliers }: SelectDropdownProps) => {
    const [seledSupplier, setSelectedSupplier] = useState();

    const handleSelectChange = () => {
        console.log("value");
        // const value = event.target.value;
        // setSelectedSupplier(value);
    };

    return (
        <div className="select-container">
            <Select
                menuPortalTarget={document.body}
                styles={{ menuPortal: (base) => ({ ...base, zIndex: 9999 }) }}
                options={suppliers}
                onChange={handleSelectChange}
            />
        </div>
    );
};

export default SelectDropdown;
