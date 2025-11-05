import type { MINECRAFT_ADVANCEMENTS_ALL } from "../lib/utils/constants/advancements";

type AdvancementDifficulty = "Advancement" | "Goal" | "Challenge";

type Advancement = {
  title: string;
  description: string;
  difficulty: AdvancementDifficulty; // Each difficulty has a different badge
  icon: string; // Ingame item ID for the icon
  identifier: (typeof MINECRAFT_ADVANCEMENTS_ALL)[number];
  requirements?: string[]; // Only when multiple requirements exists (eg. Travel to all nether biomes --> nether/explore_nether)
  experience_reward?: number; // Some advancements give XP rewards
};
