package com.brutushammerfist.text;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jdk.nashorn.internal.parser.JSONParser;

import java.io.*;
import java.nio.Buffer;
import java.util.Scanner;

public class TextGame extends Application {
    private File gameCart;
    private GameCartridge game;
    private String fileContents;

    private void loadCartridge(String filename) {
        this.game = new GameCartridge(filename);
    }

    @Override
    public void start(Stage primaryStage) {
        GridPane root = new GridPane();

        ScrollPane scroll = new ScrollPane();
        GridPane grid = new GridPane();
        GridPane nextPrev = new GridPane();
        scroll.minWidthProperty().bind(primaryStage.widthProperty().multiply(0.99));
        scroll.maxWidthProperty().bind(primaryStage.widthProperty().multiply(0.99));
        scroll.minHeightProperty().bind(primaryStage.heightProperty().multiply(0.70));
        scroll.maxHeightProperty().bind(primaryStage.heightProperty().multiply(0.70));
        grid.minWidthProperty().bind(primaryStage.widthProperty().multiply(0.75));
        grid.maxWidthProperty().bind(primaryStage.widthProperty().multiply(0.75));
        grid.minHeightProperty().bind(primaryStage.heightProperty().multiply(0.2425));
        grid.maxHeightProperty().bind(primaryStage.heightProperty().multiply(0.2425));
        nextPrev.minWidthProperty().bind(primaryStage.widthProperty().multiply(0.24));
        nextPrev.maxWidthProperty().bind(primaryStage.widthProperty().multiply(0.24));
        nextPrev.minHeightProperty().bind(primaryStage.heightProperty().multiply(0.2425));
        nextPrev.maxHeightProperty().bind(primaryStage.heightProperty().multiply(0.2425));

        TextFlow flow = new TextFlow();
        flow.minWidthProperty().bind(scroll.widthProperty().multiply(0.99));
        flow.maxWidthProperty().bind(scroll.widthProperty().multiply(0.99));

        scroll.setContent(flow);

        Text text = new Text("");
        grid.setHgap(10.0);
        grid.setVgap(10.0);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Game File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All File", "*.*"));
        Button selectButton = new Button("Choose Game");
        selectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gameCart = fileChooser.showOpenDialog(primaryStage);
                text.setText(gameCart.getAbsolutePath());
            }
        });
        Button loadButton = new Button("Load Game");
        loadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    BufferedReader fileReader = new BufferedReader(new FileReader(gameCart));
                    fileContents = "";
                    String next;
                    while ((next = fileReader.readLine()) != null) {
                        fileContents = fileContents + next + "\n";
                    }
                    fileReader.close();
                    flow.getChildren().clear();
                    flow.getChildren().add(new Text(fileContents));
                } catch (IOException e) {
                    System.out.println("Game Cartridge could not be found.");
                    e.printStackTrace();
                }
            }
        });

        grid.add(selectButton, 0, 0);
        grid.add(loadButton, 0, 4);
        grid.add(text, 1, 0);

        nextPrev.setHgap(10.0);
        nextPrev.setVgap(10.0);
        nextPrev.add(new Button("Previous"), 0, 0);
        nextPrev.add(new Button("Next"), 0, 1);

        root.add(scroll, 0, 0, 6, 6);
        root.add(grid, 0, 7, 1, 1);
        root.add(nextPrev, 1, 7, 1, 1);

        Scene scene = new Scene(root, 600, 600);

        primaryStage.setTitle("Text Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
