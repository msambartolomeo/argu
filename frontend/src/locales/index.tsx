import i18n from "i18next";
import LanguageDetector from "i18next-browser-languagedetector";
import { initReactI18next } from "react-i18next";

import { TRANSLATIONS_EN } from "./en/translations";
import { TRANSLATIONS_ES } from "./es/translations";

i18n.use(LanguageDetector)
    .use(initReactI18next)
    .init({
        resources: {
            en: {
                translation: TRANSLATIONS_EN,
            },
            es: {
                translation: TRANSLATIONS_ES,
            },
        },
        fallbackLng: "en",
    });

export default i18n;
