package com.brutushammerfist.text;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
 
public class CartridgeBuilder extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
		TabPane root = new TabPane();
		root.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
		
		VBox sceneInfo = new VBox();
		sceneInfo.getChildren().add(new Label("Scenes"));
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
		
		primaryStage.setTitle("Cartridge Builder");
		primaryStage.setScene(scene);
		primaryStage.show();
    }
}