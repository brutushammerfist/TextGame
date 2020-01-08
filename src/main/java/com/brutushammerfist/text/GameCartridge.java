package com.brutushammerfist.text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class GameCartridge {
	// File Parsing Variables
    private String fileContents;
    private JsonObject fileJson;
    private Gson jsonConverter = new Gson();
	
	// Scene Variables
    private JsonArray scenes;
    private AbstractMap<String, Map<String, Integer>> graph = new HashMap<>();
	
	// Combat Scene Variables
    private AbstractMap<String, Map<Boolean, Integer>> combatGraph = new HashMap<>();
	
	// Monster Variables
    private ArrayList<Monster> monsters = new ArrayList<>();
    private AbstractMap<String, Integer> monsterTable = new HashMap<>();
	
	// Item Variables
    private ArrayList<Item> items = new ArrayList<>();

	// Player Variables
    private PlayerCharacter player;
	
	// Stats Variables
	//private ArrayList<Stat> stats = new ArrayList<>();
	private AbstractMap<String, Stat> stats = new HashMap<>();
	

    public GameCartridge() {}

    GameCartridge(File filename) {
        this.fileJson = this.loadGame(filename);
        this.parseGameData();
    }

    private JsonObject loadGame(File file) {
        //Read in game cartridge file
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(file));
            fileContents = "";
            String next;
            while ((next = fileReader.readLine()) != null) {
                fileContents = fileContents + next;
            }
            fileReader.close();

            return this.jsonConverter.fromJson(this.fileContents, JsonObject.class);
        } catch (IOException e) {
            System.out.println("Game Cartridge could not be found.");
            e.printStackTrace();
        }

        return null;
    }

    // Parse JSON to create tree/graph of where options lead.
    private void parseGameData() {
        // Parse player information
        JsonObject playerJson = this.fileJson.getAsJsonObject("player");
        this.player = new PlayerCharacter(playerJson.get("name").getAsString(), playerJson.get("class").getAsString(), playerJson.get("currency").getAsInt(), playerJson.get("health").getAsInt(), playerJson.get("power").getAsInt(), playerJson.getAsJsonArray("attacks"));
		
		// Parse everything else
		this.parseStats();
        this.parseMonsters();
        this.parseScenes();
        this.parseItems();
        this.parseLootTables();		
		
		// Parse player stats, now that stats are loaded
		JsonArray playerStats = playerJson.getAsJsonArray("stats");
        for (int i = 0; i < playerStats.size(); i++) {
			this.player.addStat(new Stat(playerStats.get(i).getAsJsonObject().get("stat").getAsString(), playerStats.get(i).getAsJsonObject().get("lvl").getAsInt(), this.stats.get(playerStats.get(i).getAsJsonObject().get("stat").getAsString()).getMaxLvl()));
		}

        for (Item item : this.items) {
            this.player.addItem(item);
        }
    }

    private void parseScenes() {
        // Get scene list from game file
        this.scenes = this.fileJson.getAsJsonArray("scenes");

        // Create indexing for each scene in list
        AbstractMap<String, Integer> tempMap = new HashMap<>();
        for (int i = 0; i < this.scenes.size(); i++) {
            tempMap.put(this.scenes.get(i).getAsJsonObject().get("id").getAsString(), i);
        }

        // Generate a map for which scenes lead to which
        for (int i = 0; i < this.scenes.size(); i++) {
            JsonObject currScene = this.scenes.get(i).getAsJsonObject();

            if (currScene.has("win")) {
                // Generate map portion for combat
                AbstractMap<Boolean, Integer> combatResults = new HashMap<Boolean, Integer>();

                combatResults.put(true, tempMap.get(currScene.get("win").getAsString()));
                combatResults.put(false, tempMap.get(currScene.get("lose").getAsString()));

                this.combatGraph.put(currScene.get("id").getAsString(), combatResults);
            } else {
                // Generate for non-combat
                AbstractMap<String, Integer> results = new HashMap<String, Integer>();

                for (int j = 0; j < currScene.getAsJsonArray("options").size(); j++) {
                    results.put(currScene.getAsJsonArray("options").get(j).getAsJsonObject().get("result").getAsString(), tempMap.get(currScene.getAsJsonArray("options").get(j).getAsJsonObject().get("result").getAsString()));
                }

                this.graph.put(currScene.get("id").getAsString(), results);
            }
        }
    }

    private void parseMonsters() {
        JsonArray monsterJson = this.fileJson.getAsJsonArray("monsters");

        for (int i = 0; i < monsterJson.size(); i++) {
            JsonObject currMonster = monsterJson.get(i).getAsJsonObject();

            this.monsterTable.put(currMonster.get("name").getAsString(), i);
            this.monsters.add(new Monster(currMonster.get("name").getAsString(), currMonster.get("type").getAsString(), currMonster.get("health").getAsInt(), currMonster.getAsJsonArray("attacks")));
			
			JsonArray currMonsterStats = currMonster.getAsJsonArray("stats");
			for (int j = 0; j < currMonsterStats.size(); j++) {
				this.monsters.get(i).addStat(new Stat(currMonsterStats.get(j).getAsJsonObject().get("stat").getAsString(), currMonsterStats.get(j).getAsJsonObject().get("lvl").getAsInt(), this.stats.get(currMonsterStats.get(j).getAsJsonObject().get("stat").getAsString()).getMaxLvl()));
			}
		}
    }

    private void parseItems() {
        JsonArray itemJson = this.fileJson.getAsJsonArray("items");

        for (int i = 0; i < itemJson.size(); i++) {
            JsonObject currItem = itemJson.get(i).getAsJsonObject();

            switch (currItem.get("type").getAsString()) {
                case "weapon":
                    this.items.add(new Weapon(currItem.get("name").getAsString(), currItem.get("description").getAsString(), currItem.getAsJsonArray("stats")));
                    break;
                case "helm":
                    this.items.add(new Armor(currItem.get("name").getAsString(), Armor.ArmorType.HELM, currItem.get("description").getAsString(), currItem.getAsJsonArray("stats")));
                    break;
                case "chest":
                    this.items.add(new Armor(currItem.get("name").getAsString(), Armor.ArmorType.CHEST, currItem.get("description").getAsString(), currItem.getAsJsonArray("stats")));
                    break;
                case "legs":
                    this.items.add(new Armor(currItem.get("name").getAsString(), Armor.ArmorType.LEGS, currItem.get("description").getAsString(), currItem.getAsJsonArray("stats")));
                    break;
                case "boots":
                    this.items.add(new Armor(currItem.get("name").getAsString(), Armor.ArmorType.BOOTS, currItem.get("description").getAsString(), currItem.getAsJsonArray("stats")));
                    break;
                case "hands":
                    this.items.add(new Armor(currItem.get("name").getAsString(), Armor.ArmorType.HANDS, currItem.get("description").getAsString(), currItem.getAsJsonArray("stats")));
                    break;
                case "consumable":
					this.items.add(new Item(currItem.get("name").getAsString(), Item.ItemType.CONSUMABLE, 0, currItem.get("maxStack").getAsInt(), currItem.get("description").getAsString()));
                    break;
                //case "bag":
                    //break;
                default:
                    this.items.add(new Item(currItem.get("name").getAsString(), Item.ItemType.MISC, 0, 0, currItem.get("description").getAsString()));
            }
        }
    }

    private void parseLootTables() {

    }
	
	private void parseStats() {
		JsonArray statsJson = this.fileJson.getAsJsonArray("stats");
		
		for (int i = 0; i < statsJson.size(); i++) {
			JsonObject currStat = statsJson.get(i).getAsJsonObject();
			
			//this.stats.add(new Stat(currStat.get("name").getAsString(), 0, currStat.get("maxLvl").getAsInt()));
			
			this.stats.put(currStat.get("name").getAsString(), new Stat(currStat.get("name").getAsString(), 0, currStat.get("maxLvl").getAsInt()));
		}
	}

    // Send next screen to the application based upon choice.
    JsonObject proceed(String currScene, String nextScene) {
        if (currScene == null) {
            for (int i = 0; i < this.scenes.size(); i++) {
                if (this.scenes.get(i).getAsJsonObject().has("begin")) {
                    return this.scenes.get(i).getAsJsonObject();
                }
            }
        } else {
            int next = this.graph.get(currScene).get(nextScene);
            return this.scenes.get(next).getAsJsonObject();
        }
        return null;
    }

    JsonObject proceed(String currScene, Boolean winner) {
        int next = this.combatGraph.get(currScene).get(winner);
        return this.scenes.get(next).getAsJsonObject();
    }

    Monster getMonster(JsonArray monsterSpawn) {
        int spawn = new Random().nextInt(100);
        int lowerBound = 0;

        for (int i = 0; i < monsterSpawn.size(); i++) {
            if (spawn >= lowerBound && spawn < monsterSpawn.get(i).getAsJsonObject().get("spawn").getAsInt()) {
                Monster spawned = this.monsters.get(this.monsterTable.get(monsterSpawn.get(i).getAsJsonObject().get("name").getAsString()));
                spawned.setText(monsterSpawn.get(i).getAsJsonObject().get("text").getAsString());
                return spawned;
            } else {
                lowerBound = monsterSpawn.get(i).getAsJsonObject().get("spawn").getAsInt();
            }
        }

        return null;
    }

    int getPlayerHealth() {
        return this.player.getHealth();
    }
	
	int getPlayerMaxHealth() {
		return this.player.getMaxHealth();
	}

    public void damagePlayer(int dmg) {
        this.player.takeDamage(dmg);
    }

    public String getContents() {
        return this.fileContents;
    }

    public JsonObject getFileJson() {
        return this.fileJson;
    }

    String getGameTitle() {
        return this.fileJson.get("title").getAsString();
    }

    String getGameDescription() {
        return this.fileJson.get("description").getAsString();
    }

    String getGameAuthor() {
        return this.fileJson.get("author").getAsString();
    }

    String getVersion() {
        return this.fileJson.get("version").getAsString();
    }

    String getActs() {
        return this.fileJson.getAsJsonArray("acts").get(0).toString();
    }

    public ArrayList<Stat> getPlayerStats() { return this.player.getStats(); }

	public JsonArray getPlayerAttacks() { return this.player.getAttacks(); }
	
	public int playerAttackDamage(String name) { return this.player.attack(name); }
	
	public void playerEquip(String name) { this.player.equipItem(name); }

	public void playerUnequip(String toRemove) { this.player.unequipItem(toRemove); }

	public Inventory getPlayerInv() { return this.player.getInv(); }
	
	public void removePlayerItem(Item toRemove) { this.player.getInv().removeItem(toRemove); }

	public ArrayList<Item> getPlayerEquipment() { return this.player.getEquipment(); }
	
	public void playerConsume(Item toConsume) { this.player.consume(toConsume); }
}