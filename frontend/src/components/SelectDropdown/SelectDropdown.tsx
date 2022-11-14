import { useState } from "react";
import Select, { GroupBase, OptionsOrGroups } from "react-select";

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
            <Select options={suppliers} onChange={handleSelectChange} />
        </div>
    );
};

export default SelectDropdown;
