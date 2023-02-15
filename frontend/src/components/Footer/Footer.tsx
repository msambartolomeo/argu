import { useTranslation } from "react-i18next";

import {
    FormControl,
    InputLabel,
    MenuItem,
    Select,
    SelectChangeEvent,
} from "@mui/material";

import "./Footer.css";

import i18n from "../../locales";
import SupportedLanguages from "../../types/enums/SupportedLanguages";

const Footer = () => {
    const { t } = useTranslation();
    const languagesList = Object.entries(SupportedLanguages).map(
        ([key, value]) => ({
            value: value,
            label: key,
        })
    );

    const handleLanguageChange = (event: SelectChangeEvent) => {
        i18n.changeLanguage(event.target.value as string);
    };

    return (
        <div className="footer-component-container">
            <p>&copy; Copyright 2023, Argu</p>
            <FormControl>
                <InputLabel
                    id="language-select-label"
                    sx={{
                        color: "white",
                    }}
                >
                    {t("language")}
                </InputLabel>
                <Select
                    labelId="language-select-label"
                    onChange={handleLanguageChange}
                    value={i18n.language}
                    sx={{
                        color: "white",
                        ".MuiOutlinedInput-notchedOutline": {
                            borderColor: "rgba(228, 219, 233, 0.25)",
                        },
                        "&.Mui-focused .MuiOutlinedInput-notchedOutline": {
                            borderColor: "rgba(228, 219, 233, 0.25)",
                        },
                        "&:hover .MuiOutlinedInput-notchedOutline": {
                            borderColor: "rgba(228, 219, 233, 0.25)",
                        },
                        ".MuiSvgIcon-root ": {
                            fill: "white !important",
                        },
                    }}
                >
                    {languagesList.map((language) => (
                        <MenuItem key={language.value} value={language.value}>
                            {language.label}
                        </MenuItem>
                    ))}
                </Select>
            </FormControl>
        </div>
    );
};

export default Footer;
