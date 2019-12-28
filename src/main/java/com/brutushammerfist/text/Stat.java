package com.brutushammerfist.text;

public class Stat {
    private String name;
    private int lvl;
    private int maxLvl;

    public Stat() {}

    public Stat(String name, int lvl, int maxLvl) {
        this.name = name;
        this.lvl = lvl;
        this.maxLvl = maxLvl;
    }
	
	public Stat(Stat orig) {
		this.name = orig.getName();
		this.lvl = orig.getLvl();
		this.maxLvl = orig.getMaxLvl();
	}

    public String getName() { return this.name; }

    public int getLvl() { return this.lvl; }

    public int getMaxLvl() { return this.maxLvl; }
}
