type PlayerStats = {
  player: {
    current_world: string | null;
    is_online: boolean;
    name: string;
    uuid: string;
    last_seen: number | null;
    first_joined: number | null;
    is_banned: boolean;
    is_op: boolean;
  };
  statistics: Partial<Record<CustomStatistic, number>>;
  mined_blocks?: {
    [material: string]: number;
  };
};
