type PlayerStats = {
  name: string;
  uuid: string;
  online: boolean;
  statistics: {
    [key: string]: number | Record<string, number> | undefined;
    DROP?: Record<string, number>;
    PICKUP?: Record<string, number>;
    MINE_BLOCK?: Record<string, number>;
    USE_ITEM?: Record<string, number>;
    BREAK_ITEM?: Record<string, number>;
    CRAFT_ITEM?: Record<string, number>;
    KILL_ENTITY?: Record<string, number>;
    ENTITY_KILLED_BY?: Record<string, number>;
  };
};
