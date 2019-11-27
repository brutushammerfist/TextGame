package com.brutushammerfist.text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class GameCartridge {
    //private ArrayList<GameState> states;
    private String fileContents;
    private JsonObject fileJson;
    private Gson jsonConverter = new Gson();

    public GameCartridge() {

    }

    public GameCartridge(File filename) {
        this.fileJson = this.loadGame(filename);
    }

    public JsonObject loadGame(File file) {
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

    public String getContents() {
        return this.fileContents;
    }

    public JsonObject getFileJson() {
        return this.fileJson;
    }

    public String getGameTitle() {
        return this.fileJson.get("title").getAsString();
    }

    public String getGameDescription() {
        return this.fileJson.get("description").getAsString();
    }

    public String getGameAuthor() {
        return this.fileJson.get("author").getAsString();
    }

    public String getVersion() {
        return this.fileJson.get("version").getAsString();
    }

    public String getActs() {
        return this.fileJson.getAsJsonArray("acts").get(0).toString();
    }
}