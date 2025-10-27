import {
  IconCloudRain,
  IconCloudStorm,
  IconProgressHelp,
  IconSun,
} from "@tabler/icons-react";

/**
 * Get the appropriate weather icon based on the weather condition.
 *
 * Will default to a question mark icon if the weather condition is
 * unrecognized.
 *
 * @param weather - The weather condition.
 * @returns The corresponding weather icon.
 */
const WeatherIcon = (weather: World["weather"]) => {
  switch (weather) {
    case "clear":
      return <IconSun />;
    case "rain":
      return <IconCloudRain />;
    case "thunder":
      return <IconCloudStorm />;
    default:
      // Circle with question mark
      return <IconProgressHelp />;
  }
};

export default WeatherIcon;
