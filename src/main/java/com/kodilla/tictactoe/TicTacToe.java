package com.kodilla.tictactoe;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.Random;

public class TicTacToe extends Application {

    private char player = 'X';
    private char comp = 'O';
    private Cell[][] cell = new Cell[3][3];
    private Label lblStatus = new Label("X's turn to play");
    private TextField textField = new TextField();
    private Integer numberOfGames;
    private Integer compPoints = 0;
    private Integer userPoints = 0;
    private boolean gameOver;

    @Override
    public void start(Stage primaryStage) {

        GridPane pane = new GridPane();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                pane.add(cell[i][j] = new Cell(), j, i);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(pane);

        Pane bottomPanel = new Pane();
        bottomPanel.getChildren().addAll(lblStatus);
        borderPane.setBottom(bottomPanel);


        Scene sceneSecond = new Scene(borderPane, 450, 250);

        Pane panel = new Pane();
        panel.setPrefWidth(450);
        panel.setPrefHeight(250);
        Button button = new Button("Start");
        button.setLayoutX(100);
        button.setLayoutY(125);
        button.setMaxWidth(50);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                numberOfGames = Integer.parseInt(textField.getText());
                primaryStage.setScene(sceneSecond);
            }
        });
        Label labelTextField = new Label("Name:");
        labelTextField.setLayoutX(0);
        labelTextField.setLayoutY(125);
        labelTextField.setMaxWidth(50);
        textField = new TextField();
        textField.setLayoutX(50);
        textField.setLayoutY(125);
        textField.setMaxWidth(50);
        panel.getChildren().addAll(labelTextField, textField, button);

        Scene sceneFirst = new Scene(panel, 450, 250);
        primaryStage.setTitle("TicTacToe");
        primaryStage.setScene(sceneFirst);
        primaryStage.show();
    }

    public static boolean isFull(Cell[][]cells) {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (cells[i][j].getToken() == ' ')
                    return false;

        return true;
    }

    public static char[][] copyCells(Cell[][] oldCells) {
        char[][] newCells = new char[3][3];
        for(int i = 0; i<3;i++){
            for(int j =0 ; j<3;j++){
                newCells[i][j] = oldCells[i][j].getToken();
            }
        }
        return newCells;
    }

    public boolean isWon(char token) {
        for (int i = 0; i < 3; i++)
            if (cell[i][0].getToken() == token
                    && cell[i][1].getToken() == token
                    && cell[i][2].getToken() == token) {
                return true;
            }

        for (int j = 0; j < 3; j++)
            if (cell[0][j].getToken() == token
                    && cell[1][j].getToken() == token
                    && cell[2][j].getToken() == token) {
                return true;
            }

        if (cell[0][0].getToken() == token
                && cell[1][1].getToken() == token
                && cell[2][2].getToken() == token) {
            return true;
        }

        if (cell[0][2].getToken() == token
                && cell[1][1].getToken() == token
                && cell[2][0].getToken() == token) {
            return true;
        }

        return false;
    }

    public class Cell extends Pane {

        private char token = ' ';


        public Cell() {
            setStyle("-fx-border-color: black");
            this.setPrefSize(2000, 2000);
            this.setOnMouseClicked(e -> playerTurn());
        }

        public char getToken() {
            return token;
        }

        public void clearToken() {
            token = ' ';
        }

        public boolean isFilled() {
            return token == player || token == comp;
        }

        public void setToken(char c) {
            token = c;

            if (token == 'X') {
                Line line1 = new Line(10, 10,
                        this.getWidth() - 10, this.getHeight() - 10);
                line1.endXProperty().bind(this.widthProperty().subtract(10));
                line1.endYProperty().bind(this.heightProperty().subtract(10));
                line1.setStroke(Color.ORANGE);
                Line line2 = new Line(10, this.getHeight() - 10,
                        this.getWidth() - 10, 10);
                line2.startYProperty().bind(
                        this.heightProperty().subtract(10));
                line2.endXProperty().bind(this.widthProperty().subtract(10));
                line2.setStroke(Color.ORANGE);

                this.getChildren().addAll(line1, line2);
            } else if (token == 'O') {
                Ellipse ellipse = new Ellipse(this.getWidth() / 2,
                        this.getHeight() / 2, this.getWidth() / 2 - 10,
                        this.getHeight() / 2 - 10);
                ellipse.centerXProperty().bind(
                        this.widthProperty().divide(2));
                ellipse.centerYProperty().bind(
                        this.heightProperty().divide(2));
                ellipse.radiusXProperty().bind(
                        this.widthProperty().divide(2).subtract(10));
                ellipse.radiusYProperty().bind(
                        this.heightProperty().divide(2).subtract(10));
                ellipse.setStroke(Color.GREEN);
                ellipse.setFill(Color.WHITE);

                getChildren().add(ellipse);
            }
        }

        private boolean compTurn() {
            Random r = new Random();
            boolean fieldFound = false;
            while (!fieldFound) {
                char[][] tempCells = copyCells(cell);

                int[] move = MiniMax.getBestMove(tempCells);
                int i = move[0];
                int j = move[1];
                //int i = r.nextInt(3);
                //int j = r.nextInt(3);
                if (cell[i][j].getToken() == ' ') {
                    cell[i][j].setToken(comp);
                    fieldFound = true;
                }
                if (isWon(comp)) {
                    compPoints++;
                    lblStatus.setText(prepareLabel(comp + " won! The game is over"));
                    if (userPoints >= numberOfGames) {
                        lblStatus.setText(prepareLabel(comp + " won! The game is over"));
                        comp = ' ';
                    }
                    resetBoard();
                } else if (isFull(cell)) {
                    lblStatus.setText(prepareLabel("Draw! The round is over"));
                    comp = ' ';
                }
            }
            return false;
        }

        private boolean playerTurn() {

            if (token == ' ' && player != ' ') {
                setToken(player);

                if (isWon(player)) {
                    userPoints++;
                    lblStatus.setText(prepareLabel(player + " won! The round is over"));
                    if (userPoints >= numberOfGames) {
                        lblStatus.setText(prepareLabel(player + " won! The game is over"));
                        player = ' ';
                    }
                    resetBoard();
                } else if (isFull(cell)) {
                    lblStatus.setText(prepareLabel("Draw! The round is over"));
                    player = ' ';
                } else {
                    compTurn();
                }
            }
            return false;
        }

        private void resetBoard() {
            for (int i = 0 ; i < 3 ; i++) {
                for (int j = 0 ; j <3 ; j++ ) {
                    cell[i][j].clearToken();
                    cell[i][j].getChildren().clear();
                }
            }
        }

        private String prepareLabel(String message) {
            return message + ". User " + userPoints + "-" + compPoints + " Comp";
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
