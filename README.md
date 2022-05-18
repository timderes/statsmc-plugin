# Description 
This plugin creates API that returns vanilla player statistics on *"&lt;ip&gt;:11236/mcstats/all_players/"*. It requires port **11236** to be opened in your hosting settings.

 This plugin is created to work with [StatsMC Discord bot](https://discordbotlist.com/bots/statsmc) that allows you to access player statistic tops. But if you just need an API, you can use it too

# Api entry points:

---

## */mcstats/all_players*

### **args**:

`statistic `- the name of statistic

`block_type` - the name of material (if statistic requires one. example: DROP statistic requires type of item (material) that is dropped). **The arg name may be confusing!** It's literally any item in the game. Stone, stick, pickaxe

`entity_type` - the name of entity (if statistic requires one. example: KILL_ENTITY statistic requires type of entity that is killed)

### **return**:

Dict of player names as keys and their stat values as values.

### **examples**:

query string: `/mcstats/all_players?statistic=DEATHS`

json response *(dict[str, int])*: `{"Notch":4,"_jeb":5}`

&nbsp;

query string: `/mcstats/all_players?statistic=TIMES_DIED`

json response *(dict[str, str])*: `{"error": "Invalid 'statistic' value"}`

commentary: *There is no "TIMES_DIED" statistic. You must use statistics keywords from `/mcstats/list`*

&nbsp;

query string: `/mcstats/all_players?statistic=MINE_BLOCK&block_type=STONE`

json response *(dict[str, int])*: `{"Notch":123,"_jeb":321}`

&nbsp;

query string: `/mcstats/all_players?statistic=BREAK_BLOCK&block_type=INVALID_BLOCK_123`

json response *(dict[str, str])*: `{"error": "Invalid 'statistic' value"}`

commentary: *Both "BREAK_BLOCK" and "INVALID_BLOCK_123" are invalid keywords. But statistic arg has higher priority*

&nbsp;

query string: `/mcstats/all_players?statistic=PICKUP&block_type=LEAD_ORE`

json response *(dict[str, str])*: `{"error": "Invalid 'block_type' value"}`

commentary: *"PICKUP" is a valid statistic. But there is no "LEAD_ORE" in the game*

&nbsp;

query string: `/mcstats/all_players?statistic=PICKUP`

json response *(dict[str, str])*: `{"error": "PICKUP requres 'block_type' argument"}`

commentary: *API will tell you, if statistic needs second argument*

&nbsp;

query string: `/mcstats/all_players?statistic=KILL_ENTITY`

json response *(dict[str, str])*: `{"error": "KILL_ENTITY requres 'entity_type' argument"}`

&nbsp;

query string: `/mcstats/all_players?statistic=KILL_ENTITY&entity_type=CREEPER`

json response *(dict[str, int])*: `{"Notch":49,"_jeb":31}`

&nbsp;

query string: `/mcstats/all_players?statistic=KILL_ENTITY&entity_type=DROPPED_ITEM`

json response *(dict[str, int])*: `{"Notch":0,"_jeb":0}`

commentary: *This is an impossible statistic. You can not directly kill a dropped item. Yes. Minecraft handles a lot of these. Like amount of player deaths from XP orb. (/mcstats/all_players?statistic=ENTITY_KILLED_BY&entity_type=EXPERIENCE_ORB)*

&nbsp;

---

## */mcstats/list*

### **args**:

No args needed

### **return**:

list of all tracked statistics. All items from this list can be used in `statistic` argument of */mcstats/all_players/*

### **example**:

query string: `/mcstats/list`

json response *(list[str])*: `["DAMAGE_DEALT","DAMAGE_TAKEN", ... ~ 80 more items ... ,"STRIDER_ONE_CM"]`

---

## */mcstats/list/materials*

### **args**:

No args needed

### **return**:

list of all materials. All items from this list can be used in `block_type` argument of */mcstats/all_players/*

### **example**:

query string: `/mcstats/list/materials`

json response *(list[str])*: `["AIR", "STONE", ... ~ 1220 more items ... ,"POTTED_FLOWERING_AZALEA_BUSH"]`

---

## */mcstats/list/entities*

### **args**:
No args needed

### **return**:
list of all entities. All items from this list can be used in `entity_type` of */mcstats/all_players/*

### **example**:

query string: `/mcstats/list/entities`

json response *(list[str])*: `["DROPPED_ITEM", ... ~ 110 more items ... ,"PLAYER","UNKNOWN"]`
