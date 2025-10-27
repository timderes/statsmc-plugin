type ServerInfo = {
  is_hardcore: boolean;
  server_name: string;
  motd: string;
  worlds: World[];
  ip: string;
  game_mode: string;
  max_players: number;
  world_type: string;
  server_version: string;
};
