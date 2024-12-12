import React, { createContext, useState, useContext, useEffect } from "react";

const ThemeContext = createContext();

export const useTheme = () => useContext(ThemeContext);

export const ThemeProvider = ({ children }) => {
    const [isDarkMode, setIsDarkMode] = useState(() => {
        const savedTheme = localStorage.getItem("theme");
        if (savedTheme) {
            return savedTheme === "dark";
        } else {
            return window.matchMedia("(prefers-color-scheme: dark)").matches;
        }
    });

    useEffect(() => {
        const bodyClass = document.body.classList;
        if (isDarkMode) {
            bodyClass.add("dark");
            bodyClass.remove("light");
        } else {
            bodyClass.add("light");
            bodyClass.remove("dark");
        }
        localStorage.setItem("theme", isDarkMode ? "dark" : "light");
    }, [isDarkMode]);

    const toggleTheme = () => {
        setIsDarkMode((prev) => !prev);
    };

    return (
        <ThemeContext.Provider value={{ isDarkMode, toggleTheme }}>
            {children}
        </ThemeContext.Provider>
    );
};
