/**
 * Formats the playtime from ticks to a human-readable string.
 *
 * @param ticks The playtime specified in the Minecraft statistics (in ticks).
 * @returns The formatted playtime string.
 */
export const getPlaytime = (ticks: number): string => {
  const SECONDS = ticks / 20;

  if (SECONDS < 60) {
    return `${Math.floor(SECONDS)} s`;
  }

  if (SECONDS < 3600) {
    return `${Math.floor(SECONDS / 60)} min`;
  }

  return `${(SECONDS / 3600).toLocaleString(undefined, {
    maximumFractionDigits: 1,
  })} h`;
};
