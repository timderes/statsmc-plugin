import { IconMoon, IconSun } from "@tabler/icons-react";
import { useEffect, useState } from "react";
import Button from "react-bootstrap/Button";

/**
 * The available color schemes.
 *
 * @see https://getbootstrap.com/docs/5.3/customize/color-modes/
 */
type Colorscheme = "light" | "dark";

/**
 * A toggle button to switch between light and dark color schemes.
 *
 * Uses localStorage to remember user preference across sessions.
 */
const ColorschemeToggle = () => {
  const [activeTheme, setActiveTheme] = useState<Colorscheme>("light");

  const applyTheme = (theme: Colorscheme) => {
    const ROOT_PAGE_ELEMENT = document.documentElement;

    ROOT_PAGE_ELEMENT.setAttribute("data-bs-theme", theme);
  };

  const handleToggle = () => {
    const newTheme: Colorscheme = activeTheme === "light" ? "dark" : "light";
    setActiveTheme(newTheme);

    if (window.localStorage) {
      localStorage.setItem("colorscheme", newTheme);
    }
  };

  // Initialize once from localStorage or system preference
  useEffect(() => {
    let initial: Colorscheme = "light";

    try {
      const saved = localStorage.getItem("colorscheme") as Colorscheme | null;

      if (saved === "light" || saved === "dark") {
        initial = saved;
      } else if (
        window.matchMedia &&
        window.matchMedia("(prefers-color-scheme: dark)").matches
      ) {
        initial = "dark";
      }
    } catch {
      console.error("Failed to access localStorage for colorscheme!");
    }

    setActiveTheme(initial);
    applyTheme(initial);
  }, []);

  // Keep DOM in sync if activeTheme changes
  useEffect(() => {
    applyTheme(activeTheme);
  }, [activeTheme]);

  // When theme is dark, show sun icon to indicate switching to light mode
  return (
    <Button variant="link" onClick={handleToggle}>
      {activeTheme === "dark" ? <IconSun /> : <IconMoon />}
    </Button>
  );
};

export default ColorschemeToggle;
