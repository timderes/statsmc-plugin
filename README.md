# StatsMC - Minecraft Statistics Plugin

**This plugin creates a API and web interface that returns player statistics for your Minecraft server.**

> Plugin is currently work-in-progress. Some features may not work as expected. Please report any issues you encounter. **Web interface is planned as a future update!**

This repository is a fork of [StatsMC-plugin](https://github.com/Affenmilchmann/StatsMC-plugin). The original plugin was created by [Affenmilchmann](https://github.com/Affenmilchmann/).

## How to use

1. Download the `StatsMC1-x.jar` file from the releases page.
2. Place the file in your Minecraft server's `plugins` directory.
3. Start your server. The plugin will automatically create a configuration file.
4. You can view the statistics by accessing the API endpoint. The default URL is `http://your-server-ip:11111/mcstats/`.

## Configuration

The plugin creates a configuration file named `config.yml` in the `plugins/StatsMC` directory. You can modify the following settings:

| Setting | Description                         | Default Value |
| ------- | ----------------------------------- | ------------- |
| `port`  | The port on which the API will run. | `11111`       |

## Supported Minecraft Versions

Currently the plugin only officially supports Minecraft 1.21.x. However, it may work with other versions as well. If you encounter any issues, please open an issue on the GitHub repository.

| StatsMC Version | Supported Minecraft Versions |
| --------------- | ---------------------------- |
| 1.0             | 1.21.x                       |

## API Endpoints

### _/mcstats/all_players_

### **args**:

`statistic `- the name of statistic

`block_type` - the name of material (if statistic requires one. example: DROP statistic requires type of item (material) that is dropped). **The arg name may be confusing!** It's literally any item in the game. Stone, stick, pickaxe

`entity_type` - the name of entity (if statistic requires one. example: KILL_ENTITY statistic requires type of entity that is killed)

### **return**:

Dict of player names as keys and their stat values as values.

### **examples**:

query string: `/mcstats/all_players?statistic=DEATHS`

json response _(dict[str, int])_: `{"Notch":4,"_jeb":5}`

&nbsp;

query string: `/mcstats/all_players?statistic=TIMES_DIED`

json response _(dict[str, str])_: `{"error": "Invalid 'statistic' value"}`

commentary: _There is no "TIMES_DIED" statistic. You must use statistics keywords from `/mcstats/list`_

&nbsp;

query string: `/mcstats/all_players?statistic=MINE_BLOCK&block_type=STONE`

json response _(dict[str, int])_: `{"Notch":123,"_jeb":321}`

&nbsp;

query string: `/mcstats/all_players?statistic=BREAK_BLOCK&block_type=INVALID_BLOCK_123`

json response _(dict[str, str])_: `{"error": "Invalid 'statistic' value"}`

commentary: _Both "BREAK_BLOCK" and "INVALID_BLOCK_123" are invalid keywords. But statistic arg has higher priority_

&nbsp;

query string: `/mcstats/all_players?statistic=PICKUP&block_type=LEAD_ORE`

json response _(dict[str, str])_: `{"error": "Invalid 'block_type' value"}`

commentary: _"PICKUP" is a valid statistic. But there is no "LEAD_ORE" in the game_

&nbsp;

query string: `/mcstats/all_players?statistic=PICKUP`

json response _(dict[str, str])_: `{"error": "PICKUP requres 'block_type' argument"}`

commentary: _API will tell you, if statistic needs second argument_

&nbsp;

query string: `/mcstats/all_players?statistic=KILL_ENTITY`

json response _(dict[str, str])_: `{"error": "KILL_ENTITY requres 'entity_type' argument"}`

&nbsp;

query string: `/mcstats/all_players?statistic=KILL_ENTITY&entity_type=CREEPER`

json response _(dict[str, int])_: `{"Notch":49,"_jeb":31}`

&nbsp;

query string: `/mcstats/all_players?statistic=KILL_ENTITY&entity_type=DROPPED_ITEM`

json response _(dict[str, int])_: `{"Notch":0,"_jeb":0}`

commentary: _This is an impossible statistic. You can not directly kill a dropped item. Yes. Minecraft handles a lot of these. Like amount of player deaths from XP orb. (/mcstats/all_players?statistic=ENTITY_KILLED_BY&entity_type=EXPERIENCE_ORB)_

&nbsp;

---

### _/mcstats/list_

### **args**:

No args needed

### **return**:

list of all tracked statistics. All items from this list can be used in `statistic` argument of */mcstats/all_players/*

### **example**:

query string: `/mcstats/list`

json response _(list[str])_: `["DAMAGE_DEALT","DAMAGE_TAKEN", ... ~ 80 more items ... ,"STRIDER_ONE_CM"]`

---

### _/mcstats/list/materials_

### **args**:

No args needed

### **return**:

list of all materials. All items from this list can be used in `block_type` argument of */mcstats/all_players/*

### **example**:

query string: `/mcstats/list/materials`

json response _(list[str])_: `["AIR", "STONE", ... ~ 1220 more items ... ,"POTTED_FLOWERING_AZALEA_BUSH"]`

---

### _/mcstats/list/entities_

### **args**:

No args needed

### **return**:

list of all entities. All items from this list can be used in `entity_type` of */mcstats/all_players/*

### **example**:

query string: `/mcstats/list/entities`

json response _(list[str])_: `["DROPPED_ITEM", ... ~ 110 more items ... ,"PLAYER","UNKNOWN"]`

## Contributing

**This is the first time I am creating a plugin for Minecraft. So, if you see any mistakes in the code or have any suggestions for improvements, please feel free to contribute! :)**

## Disclaimer

This plugin is not affiliated with Mojang Studios or Microsoft. It is an independent project created by the community. The plugin is provided "as-is" without any warranties or guarantees. Use it at your own risk.

**Created following the [Minecraft Usage Guidelines](https://www.minecraft.net/en-us/terms#usage-guidelines).**

### License

This project is licensed under the MIT License.
