const CENTIMETER_IN_METERS = 0.01;
const CENTIMETER_IN_KILOMETERS = 0.00001;

const CENTIMETER_IN_INCHES = 0.393701;
const CENTIMETER_IN_FEET = 0.0328084;
const CENTIMETER_IN_MILES = 0.0000062137;
const CENTIMETER_IN_YARDS = 0.0109361;

/**
 * Formats a number to a localized string with specified options.
 */
const formatNumber = (
  value: number,
  options: Intl.NumberFormatOptions = {}
) => {
  return value.toLocaleString(undefined, {
    maximumFractionDigits: 2,
    ...options,
  });
};

/**
 * Calculates the total distance traveled based on the provided statistics and unit.
 *
 * @param statistics - The statistics containing distance data.
 * @param unit - The unit to convert the distance to.
 * @returns The formatted total distance string.
 */
export const getTotalDistance = (
  statistics: Partial<Record<string, number>>, // CustomStatistic Key
  unit: Distance
): string => {
  // In Minecraft the distance is measured in centimeters. Each custom statistic key
  // that ends with '_one_cm' represents a distance type traveled in centimeters.
  const distanceStatistics = Object.keys(statistics)
    .filter((key) => key.endsWith("_one_cm"))
    // Get the values of the filtered statistics
    .map((key) => statistics[key] ?? 0);

  const totalDistanceInCm = distanceStatistics.reduce((a, b) => a + b, 0);

  // Return a formatted string based on the desired unit
  if (unit === "IMPERIAL") {
    if (totalDistanceInCm < 30) {
      return formatNumber(totalDistanceInCm * CENTIMETER_IN_INCHES) + " in";
    }

    if (totalDistanceInCm < 30_000) {
      return formatNumber(totalDistanceInCm * CENTIMETER_IN_FEET) + " ft";
    }

    if (totalDistanceInCm < 900_000) {
      return formatNumber(totalDistanceInCm * CENTIMETER_IN_YARDS) + " yd";
    }

    return formatNumber(totalDistanceInCm * CENTIMETER_IN_MILES) + " mi";
  }

  // Metric system
  if (totalDistanceInCm < 50) {
    return formatNumber(totalDistanceInCm) + " cm";
  }

  if (totalDistanceInCm < 5_000) {
    return formatNumber(totalDistanceInCm * CENTIMETER_IN_METERS) + " m";
  }

  return formatNumber(totalDistanceInCm * CENTIMETER_IN_KILOMETERS) + " km";
};
