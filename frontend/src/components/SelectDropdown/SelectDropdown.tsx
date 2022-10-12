import { useState } from "react";
import Select, { GroupBase, OptionsOrGroups } from "react-select";

import "./SelectDropdown.css";

// const suppliers = [
//     { value: "supplier1", label: "Supplier 1" },
//     { value: "supplier2", label: "Supplier 2" },
//     { value: "supplier3", label: "Supplier 3" },
// ];

const SelectDropdown = (suppliers: { value: string, label: string }[]) => {

    const [seledSupplier, setSelectedSupplier] = useState();

    const handleSelectChange = ({ value }: { string }) => {
        console.log(value);
    };

    return (
        <div className="select-container">
            <Select options={suppliers} onChange={handleSelectChange} />
        </div>
    );
};

export default SelectDropdown;
