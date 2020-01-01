package com.brutushammerfist.text;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class TextGame extends Application {
    private File gameCart;
    private GameCartridge game;
    private JsonObject nextScene;
    private Monster monster;
    private ArrayList<String> choices = new ArrayList<>();
    private FileChooser fileChooser = new FileChooser();
    private Gson gson = new Gson();

    private void loadCartridge(File filename) {
        this.game = new GameCartridge(filename);
    }

    private void loadCombat(TextFlow flow, VBox grid, VBox overview, VBox charInventory, VBox charStats, VBox charEquip) {
		this.loadCharacterInfo(overview, charInventory, charStats, charEquip);
        if (this.monster.getHealth() > 0 && game.getPlayerHealth() > 0) {
            grid.getChildren().clear();
			
			for (int i = 0; i < this.game.getPlayerAttacks().size(); i++) {
				final String atkName = this.game.getPlayerAttacks().get(i).getAsJsonObject().get("name").getAsString();
				Button button = new Button(atkName);
				button.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						monster.takeDamage(game.playerAttackDamage(atkName));
						int damage = monster.attack();
						game.damagePlayer(damage);
						
						String unformattedText = monster.getText() + "\n\n\nYour attack does %s damage!\n\n\nThe foe currently has %s health remaining.";
						
						flow.getChildren().clear();
						Text text = new Text(String.format(unformattedText, damage, game.playerAttackDamage(atkName), monster.getHealth()));
						flow.getChildren().add(text);
						
						loadCombat(flow, grid, overview, charInventory, charStats, charEquip);
					}
				});
				button.setMaxWidth(Double.MAX_VALUE);
				grid.getChildren().add(button);
			}
        } else {
            if (this.monster.getHealth() < 1) {
                this.choices.add(this.nextScene.get("win").getAsString() + "`" + "win");
                this.nextScene = game.proceed(this.nextScene.get("id").getAsString(), true);
                loadScene(flow, grid, overview, charInventory, charStats, charEquip);
            } else {
                this.choices.add(this.nextScene.get("win").getAsString() + "`" + "lose");
                this.nextScene = game.proceed(this.nextScene.get("id").getAsString(), false);
                loadScene(flow, grid, overview, charInventory, charStats, charEquip);
            }
            this.monster.setHealth(this.monster.getMaxHealth());
        }
    }

    private void loadScene(TextFlow flow, VBox grid, VBox overview, VBox charInventory, VBox charStats, VBox charEquip) {
        this.loadCharacterInfo(overview, charInventory, charStats, charEquip);
        if (nextScene.has("win")) {
            this.monster = this.game.getMonster(nextScene.getAsJsonArray("monsters"));

            flow.getChildren().clear();
			// This comment fixes a null pointer exception for getting text in the line below? Will not work without comment here.
            flow.getChildren().add(new Text(this.monster.getText()));
            grid.getChildren().clear();

            this.loadCombat(flow, grid, overview, charInventory, charStats, charEquip);
        } else {
            flow.getChildren().clear();
            flow.getChildren().add(new Text(nextScene.get("text").getAsString()));
            grid.getChildren().clear();

            JsonArray options = nextScene.getAsJsonArray("options");
            for (int i = 0; i < options.size(); i++) {
                Button button = new Button(options.get(i).getAsJsonObject().get("value").getAsString());
                int finalI = i;
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        choices.add(options.get(finalI).getAsJsonObject().get("result").getAsString());
                        nextScene = game.proceed(nextScene.get("id").getAsString(), options.get(finalI).getAsJsonObject().get("result").getAsString());
                        loadScene(flow, grid, overview, charInventory, charStats, charEquip);
                    }
                });
                button.setMaxWidth(Double.MAX_VALUE);
                button.wrapTextProperty().setValue(true);
                grid.getChildren().add(button);
            }
        }
    }

    private void loadCharacterInfo(VBox overview, VBox charInventory, VBox charStats, VBox charEquip) {
		overview.getChildren().clear();
        Label health = new Label("Health: " + Integer.toString(this.game.getPlayerHealth()) + "/" + Integer.toString(this.game.getPlayerMaxHealth()));
		overview.getChildren().add(health);
		
		ArrayList<Stat> stats = this.game.getPlayerStats();

        charStats.getChildren().clear();
		
		for (Stat stat : stats) {
            Label label = new Label(stat.getName() + ": " + stat.getLvl() + "/" + stat.getMaxLvl());
            charStats.getChildren().add(label);
        }
		
		for (int i = 0; i < game.getPlayerInv().getMaxSize(); i++) {
			if (game.getPlayerInv().get(i) != null) {
				final String itemName = game.getPlayerInv().get(i).getItem().getName();
				final int finalI = i;
				Button item = new Button(itemName);
				if (game.getPlayerInv().get(i).getItem().getType() == Item.ItemType.WEAPON) {
					item.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							game.playerEquip(itemName);
							game.removePlayerItem(game.getPlayerInv().get(finalI).getItem());
						}
					});
				} else if (game.getPlayerInv().get(i).getItem().getType() == Item.ItemType.ARMOR) {
					item.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							game.playerEquip(itemName);
							game.removePlayerItem(game.getPlayerInv().get(finalI).getItem());
						}
					});
				}
                item.setMaxWidth(Double.MAX_VALUE);
				charInventory.getChildren().add(item);
			} else {
				Button item = new Button("Empty.");
                item.setMaxWidth(Double.MAX_VALUE);
				charInventory.getChildren().add(item);
			}
		}
    }

    private void resetToBeginningScreen(Stage primaryStage, TextFlow flow, VBox grid, VBox overview, VBox charInventory, VBox charStats, VBox charEquip) {
        flow.getChildren().clear();
        grid.getChildren().clear();
        this.gameCart = null;
        this.monster = null;
        this.game = null;
        this.nextScene = null;
        this.choices.clear();

        Text filePath = new Text("");
        grid.setSpacing(10.0);
        this.fileChooser.setTitle("TextGame");
        this.fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All File", "*.*"));
        Button selectButton = new Button("Choose Game");
        selectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gameCart = fileChooser.showOpenDialog(primaryStage);
                filePath.setText(gameCart.getAbsolutePath());
            }
        });
        selectButton.setMaxWidth(Double.MAX_VALUE);
        Button loadButton = new Button("Load Game");
        loadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadCartridge(gameCart);
                primaryStage.setTitle(game.getGameTitle());
                nextScene = game.proceed(null, (String) null);
                loadScene(flow, grid, overview, charInventory, charStats, charEquip);
            }
        });
        loadButton.setMaxWidth(Double.MAX_VALUE);

        grid.getChildren().add(selectButton);
        grid.getChildren().add(filePath);
        grid.getChildren().add(loadButton);
        grid.setFillWidth(true);
    }

    private void save(Stage primaryStage) {
        String filepath = this.gameCart.getAbsolutePath().substring(0, this.gameCart.getAbsolutePath().lastIndexOf('\\'));

        this.fileChooser.setInitialDirectory(new File(filepath));
        File saveFile = this.fileChooser.showSaveDialog(primaryStage);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile.getAbsolutePath()));

            JsonObject json = new JsonObject();

            JsonArray choices = new JsonArray();
            for (String a : this.choices) {
                choices.add(a);
            }

            json.addProperty("choices", this.gson.toJson(choices));

            writer.write(this.gson.toJson(json));
            writer.close();
        } catch (IOException e) {
            System.out.println("IO Exception during Save!");
        }
    }

    private void load(Stage primaryStage, TextFlow flow, VBox grid, VBox overview, VBox charInventory, VBox charStats, VBox charEquip) {
        File filepath = this.fileChooser.showOpenDialog(primaryStage);
        String fileContents = "";

        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(filepath));
            String next;
            while ((next = fileReader.readLine()) != null) {
                fileContents = fileContents + next;
            }
            fileReader.close();

            JsonObject json = gson.fromJson(fileContents, JsonObject.class);
            JsonArray choices = gson.fromJson(json.get("choices").getAsString(), JsonArray.class);

            for (int i = 0; i < choices.size(); i++) {
                if(choices.get(i).getAsString().contains("`")) {
                    String trimmed = choices.get(i).getAsString().substring(0, choices.get(i).getAsString().lastIndexOf("`"));
                    String result = choices.get(i).getAsString().substring(choices.get(i).getAsString().lastIndexOf("`") + 1);
                    if (result.contains("win")) {
                        this.nextScene = this.game.proceed(this.nextScene.get("id").getAsString(), true);
                    } else {
                        this.nextScene = this.game.proceed(this.nextScene.get("id").getAsString(), false);
                    }
                } else {
                    this.nextScene = this.game.proceed(this.nextScene.get("id").getAsString(), choices.get(i).getAsString());
                }

            }

            this.loadScene(flow, grid, overview, charInventory, charStats, charEquip);
        } catch (IOException e) {
            System.out.println("Game Cartridge could not be found.");
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();

        // Main Text
        ScrollPane scroll = new ScrollPane();
        TextFlow flow = new TextFlow();

        // Character Info Side Pane
        TabPane characterInfo = new TabPane();
        characterInfo.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        ScrollPane gridScroll = new ScrollPane();
        ScrollPane overScroll = new ScrollPane();
        ScrollPane invScroll = new ScrollPane();
        ScrollPane statScroll = new ScrollPane();
        ScrollPane equipScroll = new ScrollPane();
        VBox overView = new VBox();
        VBox charInventory = new VBox();
        VBox charStats = new VBox();
        VBox charEquip = new VBox();

        // Interactive Area
        VBox grid = new VBox();
		
		CartridgeBuilder builder = new CartridgeBuilder();

        final Menu fileMenu = new Menu("File");
        MenuItem restart = new MenuItem("Close current game");
        restart.setOnAction(event -> {
            this.resetToBeginningScreen(primaryStage, flow, grid, overView, charInventory, charStats, charEquip);
        });
        MenuItem load = new MenuItem("Load Save");
        load.setOnAction(event -> {
            this.load(primaryStage, flow, grid, overView, charInventory, charStats, charEquip);
        });
        MenuItem save = new MenuItem("Save Game");
        save.setOnAction(event -> {
            this.save(primaryStage);
        });
		MenuItem build = new MenuItem("Open Builder");
		build.setOnAction(event -> {
			builder.start(new Stage());
		});
        fileMenu.getItems().addAll(restart, load, save, build);

        final Menu optionsMenu = new Menu("Options");

        final Menu helpMenu = new Menu("Help");

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, optionsMenu, helpMenu);

        scroll.minWidthProperty().bind(primaryStage.widthProperty().multiply(0.74));
        scroll.maxWidthProperty().bind(primaryStage.widthProperty().multiply(0.74));
        scroll.minHeightProperty().bind(primaryStage.heightProperty().multiply(0.70));
        scroll.maxHeightProperty().bind(primaryStage.heightProperty().multiply(0.70));

        gridScroll.minWidthProperty().bind(primaryStage.widthProperty().multiply(0.74));
        gridScroll.maxWidthProperty().bind(primaryStage.widthProperty().multiply(0.74));
        gridScroll.minHeightProperty().bind(primaryStage.heightProperty().multiply(0.2425));
        gridScroll.maxHeightProperty().bind(primaryStage.heightProperty().multiply(0.2425));

        characterInfo.minWidthProperty().bind(primaryStage.widthProperty().multiply(0.2475));
        characterInfo.maxWidthProperty().bind(primaryStage.widthProperty().multiply(0.2475));

        flow.minWidthProperty().bind(scroll.widthProperty().multiply(0.99));
        flow.maxWidthProperty().bind(scroll.widthProperty().multiply(0.99));
        grid.minWidthProperty().bind(gridScroll.widthProperty().multiply(0.99));
        grid.maxWidthProperty().bind(gridScroll.widthProperty().multiply(0.99));

        scroll.setContent(flow);
        gridScroll.setContent(grid);

        Text filePath = new Text("");
        grid.setSpacing(10.0);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Game File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All File", "*.*"));
        Button selectButton = new Button("Choose Game");
        selectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gameCart = fileChooser.showOpenDialog(primaryStage);
                filePath.setText(gameCart.getAbsolutePath());
            }
        });
        selectButton.setMaxWidth(Double.MAX_VALUE);
        Button loadButton = new Button("Load Game");
        loadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadCartridge(gameCart);
                primaryStage.setTitle(game.getGameTitle());
                nextScene = game.proceed(null, (String) null);
                loadScene(flow, grid, overView, charInventory, charStats, charEquip);
            }
        });
        loadButton.setMaxWidth(Double.MAX_VALUE);

        grid.getChildren().add(selectButton);
        grid.getChildren().add(filePath);
        grid.getChildren().add(loadButton);
        grid.setFillWidth(true);

        overScroll.setContent(overView);
        invScroll.setContent(charInventory);
        statScroll.setContent(charStats);
        equipScroll.setContent(charEquip);

		Tab overview = new Tab("Overview", overScroll);
        Tab inventory = new Tab("Inventory", invScroll);
        Tab stats = new Tab("Stats", statScroll);
        Tab equipment = new Tab("Equipment", equipScroll);

        invScroll.maxWidthProperty().bind(primaryStage.widthProperty().multiply(0.2475));
        invScroll.minWidthProperty().bind(primaryStage.widthProperty().multiply(0.2475));
        invScroll.maxHeightProperty().bind(primaryStage.heightProperty().multiply(0.91));
        invScroll.minHeightProperty().bind(primaryStage.heightProperty().multiply(0.91));
        charInventory.maxWidthProperty().bind(invScroll.widthProperty().multiply(0.97));
        charInventory.minWidthProperty().bind(invScroll.widthProperty().multiply(0.97));

        characterInfo.getTabs().addAll(overview, inventory, stats, equipment);

        VBox rootLeft = new VBox();
        rootLeft.getChildren().addAll(scroll, gridScroll);

        HBox rootRight = new HBox();
        rootRight.getChildren().addAll(rootLeft, characterInfo);

        root.getChildren().addAll(menuBar, rootRight);

        Scene scene = new Scene(root, 600, 600);

        primaryStage.setTitle("Text Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
