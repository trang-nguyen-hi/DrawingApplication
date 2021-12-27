package com.example.assignment3;

import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class MainUI extends BorderPane implements DrawingModelSubscriber{
    ShapeToolbar shapeToolbar;
    DrawingView drawingView;
    MiniDrawingView miniDrawingView;
    MiniDrawingController miniDrawingController;
    ColourToolbar colourToolbar;

    DrawingModel model;
    InteractionModel iModel;
    DrawingController controller;


    public MainUI(){
        shapeToolbar = new ShapeToolbar();
        drawingView = new DrawingView(2000, 2000, 500, 500);
        miniDrawingView = new MiniDrawingView(2000, 2000, 100, 100);
        colourToolbar = new ColourToolbar();

        miniDrawingController = new MiniDrawingController();
        miniDrawingView.setController(miniDrawingController);
        this.setLeft(shapeToolbar);
        StackPane view = new StackPane(drawingView, miniDrawingView);
        view.setStyle("-fx-border-color: red;\n" +
                "-fx-border-insets: 2;\n" +
                "-fx-border-width: 2;\n" +
                "-fx-border-style: dashed;\n");
        view.setAlignment(miniDrawingView, Pos.TOP_LEFT);
        this.setCenter(view);
        this.setRight(colourToolbar);
    }

    public void setModel(DrawingModel newModel) {
        model = newModel;
        shapeToolbar.setModel(newModel);
        colourToolbar.setModel(newModel);

        drawingView.setModel(newModel);
        miniDrawingView.setModel(newModel);
        model.addSubscriber(drawingView);
        model.addSubscriber(miniDrawingView);

        miniDrawingController.setModel(model);
    }

    public void setController(DrawingController newController) {
        controller = newController;
        shapeToolbar.setController(newController);
        drawingView.setController(newController);
        colourToolbar.setController(newController);
    }

    public void setInteractionModel(InteractionModel newIModel) {
        iModel = newIModel;

        shapeToolbar.setInteractionModel(newIModel);
        iModel.addSubscriber(shapeToolbar);

        colourToolbar.setInteractionModel(newIModel);
        iModel.addSubscriber(colourToolbar);

        drawingView.setInteractionModel(newIModel);
        iModel.addSubscriber(drawingView);

        miniDrawingView.setInteractionModel(newIModel);
        iModel.addSubscriber(miniDrawingView);

        miniDrawingController.setInteractionModel(newIModel);
    }

    @Override
    public void modelChanged() {
        //draw()
    }

}
