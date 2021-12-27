package com.example.assignment3;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.shape.DrawMode;
import javafx.stage.Stage;

public class DrawingApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        MainUI mainUI = new MainUI();
        InteractionModel interactionModel = new InteractionModel();
        DrawingModel drawingModel = new DrawingModel();
        DrawingController drawingController = new DrawingController();

        mainUI.setController(drawingController);
        mainUI.setInteractionModel(interactionModel);
        mainUI.setModel(drawingModel);
        drawingModel.addSubscriber(mainUI);

        drawingController.setModel(drawingModel);
        drawingController.setInteractionModel(interactionModel);

        Scene scene = new Scene(mainUI);
        scene.setOnKeyPressed(drawingController::handleKeyPressed);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
