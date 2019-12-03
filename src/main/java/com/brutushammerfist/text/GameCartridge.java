package com.brutushammerfist.text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class GameCartridge {
    private String fileContents;
    private JsonObject fileJson;
    private Gson jsonConverter = new Gson();
    private JsonArray scenes;
    private AbstractMap<String, Map<String, Integer>> graph = new HashMap<String, Map<String, Integer>>();

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
        // Fix this to know it broke

        return new JsonObject();
    }

    // Parse JSON to create tree/graph of where options lead.
    private void parseGameData() {
        this.scenes = this.fileJson.getAsJsonArray("scenes");

        AbstractMap<String, Integer> tempMap = new HashMap<String, Integer>();
        for (int i = 0; i < this.scenes.size(); i++) {
            System.out.println(this.scenes.get(i).getAsJsonObject().get("id").getAsString());
            System.out.println(i);
            tempMap.put(this.scenes.get(i).getAsJsonObject().get("id").getAsString(), i);
        }

        for (int i = 0; i < this.scenes.size(); i++) {
            JsonObject currScene = this.scenes.get(i).getAsJsonObject();

            AbstractMap<String, Integer> results = new HashMap<String, Integer>();
            for (int j = 0; j < currScene.getAsJsonArray("options").size(); j++) {
                results.put(currScene.getAsJsonArray("options").get(j).getAsJsonObject().get("result").getAsString(), tempMap.get(currScene.getAsJsonArray("options").get(j).getAsJsonObject().get("result").getAsString()));
            }

            this.graph.put(currScene.get("id").getAsString(), results);
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
            Integer next = this.graph.get(currScene).get(nextScene);
            return this.scenes.get(next).getAsJsonObject();
        }
        return null;
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