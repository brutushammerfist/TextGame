# TextGame

Pending a fancier name, TextGame is meant to be a small project that allows for the building of text based Choose Your Own Adventure style games, with some turn-based combat and rpg elements, if the creator so chooses.

## Concepts

### Games

Games are the overall experience that players will be enjoying. These are saved as "cartridges". It is the overarching story and should define all interactions that can be had in the experience.

### Cartridges

Cartridges are simply JSON files that follow a specific, self-defined, schema. This file is parsed into Stats, Equipment, Scenes, etc. when loaded into TextGame.

### Saves

To keep saving simple, I've decided it best to keep track of current stats, etc. as well the selections made previously by the player. Then, once the save is loaded, stats are updated to what the save file has them set to and the decision tree is followed using the decisions the player has previously made. Yes, this means that save editing is possible. No, I'm not going to worry about it. If you'd like to rip the fun out of the games that you're playing, who am I to stop you?

## To-Do

- Create builder so that those unfamiliar with JSON can generate their own games.
- Port to a web page instead so that this can be hosted online and used???
    - This would likely mean that development of this standalone java app would be discontinued.