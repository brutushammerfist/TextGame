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
    private String fileContents;
    private JsonObject fileJson;
    private Gson jsonConverter = new Gson();
    private JsonArray scenes;
    private AbstractMap<String, Map<String, Integer>> graph = new HashMap<String, Map<String, Integer>>();
    private AbstractMap<String, Map<Boolean, Integer>> combatGraph = new HashMap<String, Map<Boolean, Integer>>();
    private JsonArray monsterJson;
    private ArrayList<Monster> monsters = new ArrayList<Monster>();
    private AbstractMap<String, Integer> monsterTable = new HashMap<String, Integer>();

    private PlayerCharacter player;

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
        this.scenes = this.fileJson.getAsJsonArray("scenes");
        this.monsterJson = this.fileJson.getAsJsonArray("monsters");

        JsonObject playerJson = this.fileJson.getAsJsonObject("player");
        this.player = new PlayerCharacter(playerJson.get("name").getAsString(), playerJson.get("class").getAsString(), playerJson.get("currency").getAsInt(), playerJson.get("health").getAsInt(), playerJson.get("power").getAsInt());

        // Fill out lookup tables for scenes and monsters
        AbstractMap<String, Integer> tempMap = new HashMap<String, Integer>();
        for (int i = 0; i < this.scenes.size(); i++) {
            tempMap.put(this.scenes.get(i).getAsJsonObject().get("id").getAsString(), i);
        }

        for (int i = 0; i < this.monsterJson.size(); i++) {
            JsonObject currMonster = this.monsterJson.get(i).getAsJsonObject();

            this.monsterTable.put(currMonster.get("name").getAsString(), i);
            this.monsters.add(new Monster(currMonster.get("name").getAsString(), currMonster.get("type").getAsString(), currMonster.get("health").getAsInt(), currMonster.getAsJsonArray("attacks")));
        }

        for (int i = 0; i < this.scenes.size(); i++) {
            JsonObject currScene = this.scenes.get(i).getAsJsonObject();

            if (currScene.has("win")) {
                AbstractMap<Boolean, Integer> combatResults = new HashMap<Boolean, Integer>();

                combatResults.put(true, tempMap.get(currScene.get("win").getAsString()));
                combatResults.put(false, tempMap.get(currScene.get("lose").getAsString()));

                this.combatGraph.put(currScene.get("id").getAsString(), combatResults);
            } else {
                AbstractMap<String, Integer> results = new HashMap<String, Integer>();

                for (int j = 0; j < currScene.getAsJsonArray("options").size(); j++) {
                    results.put(currScene.getAsJsonArray("options").get(j).getAsJsonObject().get("result").getAsString(), tempMap.get(currScene.getAsJsonArray("options").get(j).getAsJsonObject().get("result").getAsString()));
                }

                this.graph.put(currScene.get("id").getAsString(), results);
            }
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
            System.out.println(currScene + " - " + nextScene);
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

    public void damagePlayer(int dmg) {
        this.player.takeDamage(dmg);
    }

    int playerAttack() {
        return this.player.attack();
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
}