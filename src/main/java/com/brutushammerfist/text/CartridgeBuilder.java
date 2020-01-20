package com.brutushammerfist.text;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
 
public class CartridgeBuilder extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
		TabPane root = new TabPane();
		root.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

		ListView listView = new ListView();

		Label sceneIDLabel = new Label("ID:");
		TextField sceneID = new TextField();

		Label sceneTypeLabel = new Label("Type:");
		Spinner sceneType = new Spinner();

		Label sceneTextLabel = new Label("Text:");
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Game File");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All File", "*.*"));
		Button sceneText = new Button("Select text file.");
		sceneText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				sceneText.setText(fileChooser.showOpenDialog(primaryStage).getAbsolutePath());
			}
		});

		ListView option = new ListView();

		Label optionValueLabel = new Label("Option Value:");
		TextField optionValue = new TextField();

		Label resultLabel = new Label("Result");
		Spinner result = new Spinner();

		TilePane optionInfo = new TilePane(optionValueLabel, optionValue, resultLabel, result);
		optionInfo.setPrefColumns(2);

		HBox options = new HBox(option, optionInfo);

		//TilePane sceneInfoToo = new TilePane(sceneIDLabel, sceneID, sceneTypeLabel, sceneType, sceneTextLabel, sceneText);
		TilePane sceneInfoThree = new TilePane(sceneIDLabel, sceneID, sceneTypeLabel, sceneType, sceneTextLabel, sceneText);
		sceneInfoThree.setPrefColumns(2);

		VBox sceneInfoToo = new VBox(sceneInfoThree, options);

		HBox sceneInfo = new HBox(listView, sceneInfoToo);

		VBox playerInfo = new VBox();
		playerInfo.getChildren().add(new Label("Player"));
		VBox itemInfo = new VBox();
		itemInfo.getChildren().add(new Label("Items"));
		VBox enemyInfo = new VBox();
		enemyInfo.getChildren().add(new Label("Enemies"));
		VBox lootInfo = new VBox();
		lootInfo.getChildren().add(new Label("Loot"));
		VBox statInfo = new VBox();
		statInfo.getChildren().add(new Label("Stats"));
		
		Tab scenes = new Tab("Scenes", sceneInfo);
		Tab player = new Tab("Player", playerInfo);
		Tab items = new Tab("Items", itemInfo);
		Tab enemies = new Tab("Enemies", enemyInfo);
		Tab loottables = new Tab("Loottables", lootInfo);
		Tab stats = new Tab("Stats", statInfo);


		
		root.getTabs().addAll(scenes, player, items, enemies, loottables, stats);
		
		Scene scene = new Scene(root, 600, 600);

		primaryStage.setTitle("ListView Experiment 1");


		
		primaryStage.setTitle("Cartridge Builder");
		primaryStage.setScene(scene);
		primaryStage.show();
    }
}