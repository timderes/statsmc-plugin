type StatisticType =
  | "BLOCK_MINED"
  | "CUSTOM"
  | "ITEM_BROKEN"
  | "ITEM_CRAFTED"
  | "ITEM_USED"
  | "ITEM_PICKED_UP"
  | "ITEM_DROPPED"
  | "ENTITY_KILLED"
  | "ENTITY_KILLED_BY";

/**
 * Custom Statistics available in Minecraft. Caution: Some statistics has different names in older versions.
 *
 * Based on Minecraft 1.21.10
 *
 * Scraped from: @see https://minecraft.fandom.com/wiki/Statistics#Custom_statistics
 */
type CustomStatistic =
  /** The number of times the player bred two mobs */
  | "animals_bred"
  /** Distance Flown — Distance traveled upward and forward at the same time, while more than one block above the ground. */
  | "fly_one_cm"
  /** Distance by Happy Ghast — The total distance traveled by happy ghasts. */
  | "happy_ghast_one_cm"
  /** Distance by Horse — The total distance traveled by horses. */
  | "horse_one_cm"
  /** Dispensers Searched — The number of times interacted with dispensers. */
  | "inspect_dispenser"
  /** Droppers Searched — The number of times interacted with droppers. */
  | "inspect_dropper"
  /** Hoppers Searched — The number of times interacted with hoppers. */
  | "inspect_hopper"
  /** Interactions with Anvil — The number of times interacted with anvils. */
  | "interact_with_anvil"
  /** Interactions with Beacon — The number of times interacted with beacons. */
  | "interact_with_beacon"
  /** Interactions with Blast Furnace — The number of times interacted with blast furnaces. */
  | "interact_with_blast_furnace"
  /** Interactions with Brewing Stand — The number of times interacted with brewing stands. */
  | "interact_with_brewingstand"
  /** Interactions with Campfire — The number of times interacted with campfires. */
  | "interact_with_campfire"
  /** Interactions with Cartography Table — The number of times interacted with cartography tables. */
  | "interact_with_cartography_table"
  /** Interactions with Crafting Table — The number of times interacted with crafting tables. */
  | "interact_with_crafting_table"
  /** Interactions with Furnace — The number of times interacted with furnaces. */
  | "interact_with_furnace"
  /** Interactions with Grindstone — The number of times interacted with grindstones. */
  | "interact_with_grindstone"
  /** Interactions with Lectern — The number of times interacted with lecterns. */
  | "interact_with_lectern"
  /** Interactions with Loom — The number of times interacted with looms. */
  | "interact_with_loom"
  /** Interactions with Smithing Table — The number of times interacted with smithing tables. */
  | "interact_with_smithing_table"
  /** Interactions with Smoker — The number of times interacted with smokers. */
  | "interact_with_smoker"
  /** Interactions with Stonecutter — The number of times interacted with stonecutters. */
  | "interact_with_stonecutter"
  /** Jumps — The total number of jumps performed. */
  | "jump"
  /** Games Quit — The number of times "Save and quit to title" has been clicked. */
  | "leave_game"
  /** Distance by Minecart — The total distance traveled by minecarts. */
  | "minecart_one_cm"
  /** Mob Kills — The number of mobs the player killed. */
  | "mob_kills"
  /** Barrels Opened — The number of times the player has opened a barrel. */
  | "open_barrel"
  /** Chests Opened — The number of times the player opened chests. */
  | "open_chest"
  /** Ender Chests Opened — The number of times the player opened ender chests. */
  | "open_enderchest"
  /** Shulker Boxes Opened — The number of times the player has opened a shulker box. */
  | "open_shulker_box"
  /** Distance by Pig — The total distance traveled by pigs via saddles. */
  | "pig_one_cm"
  /** Note Blocks Played — The number of note blocks hit. */
  | "play_noteblock"
  /** Music Discs Played — The number of music discs played on a jukebox. */
  | "play_record"
  /** Time Played — The total amount of time played (tracked in ticks). */
  | "play_one_minute"
  /** Player Kills — The number of players the player killed. */
  | "player_kills"
  /** Plants Potted — The number of plants potted onto flower pots. */
  | "pot_flower"
  /** Raids Triggered — The number of times the player has triggered a raid. */
  | "raid_trigger"
  /** Raids Won — The number of times the player has won a raid. */
  | "raid_win"
  /** Times Slept in a Bed — The number of times the player has slept in a bed. */
  | "sleep_in_bed"
  /** Sneak Time — The time the player has held down the sneak button (ticks). */
  | "sneak_time"
  /** Distance Sprinted — The total distance sprinted. */
  | "sprint_one_cm"
  /** Distance by Strider — The total distance traveled by striders via saddles. */
  | "strider_one_cm"
  /** Distance Swum — The total distance covered with sprint-swimming. */
  | "swim_one_cm"
  /** Talked to Villagers — The number of times interacted with villagers (opened trading GUI). */
  | "talked_to_villager"
  /** Targets Hit — The number of times the player has shot a target block. */
  | "target_hit"
  /** Time Since Last Death — The time since the player's last death (ticks). */
  | "time_since_death"
  /** Time Since Last Rest — The time since the player's last rest (ticks). */
  | "time_since_rest"
  /** Time with World Open — The total amount of time the world was opened (ticks). */
  | "total_world_time"
  /** Traded with Villagers — The number of times traded with villagers. */
  | "traded_with_villager"
  /** Trapped Chests Triggered — The number of times the player opened trapped chests. */
  | "trigger_trapped_chest"
  /** Note Blocks Tuned — The number of times interacted with note blocks. */
  | "tune_noteblock"
  /** Water Taken from Cauldron — The number of times the player took water from cauldrons with glass bottles. */
  | "use_cauldron"
  /** Distance Walked on Water — The total distance covered while the player's head is not underwater. */
  | "walk_on_water_one_cm"
  /** Distance Walked — The total distance walked. */
  | "walk_one_cm"
  /** The total distance covered in any direction while the player's head is underwater. */
  | "walk_under_water_one_cm";
