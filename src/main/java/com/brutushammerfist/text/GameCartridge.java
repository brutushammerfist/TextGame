package com.brutushammerfist.text;

import java.io.File;
import java.util.ArrayList;

public class GameCartridge {
    public ArrayList<GameState> states;

    public GameCartridge() {

    }

    public GameCartridge(String filename) {
        this.loadGame(new File(filename));
    }

    public void loadGame(File file) {
        //Read in game cartridge file
    }
}