type World = {
  seed: number;
  allow_monsters: boolean;
  max_height: number;
  pvp: boolean;
  difficulty: "EASY" | "NORMAL" | "HARD" | "PEACEFUL";
  environment: "NORMAL" | "NETHER" | "THE_END";
  min_height: number;
  game_time: number;
  full_time: number;
  allow_animals: boolean;
  current_players: string[];
  name: string;
  weather: string;
  time: number;
};
