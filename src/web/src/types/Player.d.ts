type PlayerStats = {
  player: {
    current_world: string | null;
    is_online: boolean;
    name: string;
    uuid: string;
  };
  statistics: {
    [key: string]: number | Record<string, number> | undefined;
  };
};
