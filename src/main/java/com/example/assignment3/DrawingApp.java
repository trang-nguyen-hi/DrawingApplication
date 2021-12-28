package com.example.assignment3;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DrawingApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws InstantiationException, IllegalAccessException {
        MainUI mainUI = new MainUI();
        InteractionModel interactionModel = new InteractionModel();
        DrawingModel drawingModel = new DrawingModel();
        DrawingController drawingController = new DrawingController();

        // Put the M-C-V system together
        mainUI.setController(drawingController);
        mainUI.setInteractionModel(interactionModel);
        mainUI.setModel(drawingModel);
        drawingModel.addSubscriber(mainUI);

        drawingController.setModel(drawingModel);
        drawingController.setInteractionModel(interactionModel);

        Scene scene = new Scene(mainUI);
        // detect key presses for the deletion function
        scene.setOnKeyPressed(drawingController::handleKeyPressed);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
