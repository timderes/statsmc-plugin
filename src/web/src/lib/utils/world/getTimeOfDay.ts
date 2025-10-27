/**
 * Gets the time of day based on the provided time in ticks.
 * One Minecraft day is `24000` ticks (20 minutes) with `0` being sunrise.
 *
 * @see https://minecraft.fandom.com/wiki/Daylight_cycle
 * @see https://hub.spigotmc.org/javadocs/spigot/org/bukkit/World.html#getTime()
 *
 * @param time - The time of day in ticks.
 * @returns The corresponding time of day.
 */
const getTimeOfDay = (time: World["time"]) => {
  switch (true) {
    case time > 23000: // -> /time set sunrise
      return "Sunrise";
    case time > 18000:
      return "Midnight";
    case time > 13000: // -> /time set night
      return "Night";
    case time > 12000: // -> /time set sunset
      return "Sunset";
    case time > 6000: // -> /time set noon
      return "Noon";
    case time > 1000: // -> /time set day
      return "Day";
    default:
      return "Morning";
  }
};

export default getTimeOfDay;
