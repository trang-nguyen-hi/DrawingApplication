package com.example.assignment3;

import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class MainUI extends BorderPane implements DrawingModelSubscriber{
    private final ShapeToolbar shapeToolbar;
    private final ColourToolbar colourToolbar;
    private final DrawingView drawingView;

    private final MiniDrawingView miniDrawingView;
    private final MiniDrawingController miniDrawingController;

    public MainUI(){
        shapeToolbar = new ShapeToolbar();
        colourToolbar = new ColourToolbar();

        drawingView = new DrawingView(2000, 2000, 500, 500);
        miniDrawingView = new MiniDrawingView(2000, 2000, 100, 100);

        miniDrawingController = new MiniDrawingController();
        miniDrawingView.setController(miniDrawingController);

        this.setLeft(shapeToolbar);
        StackPane view = new StackPane(drawingView, miniDrawingView);
        view.setStyle("""
                -fx-border-color: red;
                -fx-border-insets: 2;
                -fx-border-width: 2;
                -fx-border-style: dashed;
                """);
        StackPane.setAlignment(miniDrawingView, Pos.TOP_LEFT);
        this.setCenter(view);
        this.setRight(colourToolbar);
    }

    public void setModel(DrawingModel newModel) {
        shapeToolbar.setModel(newModel);

        // Pass the model for the children views
        drawingView.setModel(newModel);
        miniDrawingView.setModel(newModel);
        miniDrawingController.setModel(newModel);

        // Add them to the subscriber list
        newModel.addSubscriber(drawingView);
        newModel.addSubscriber(miniDrawingView);

    }

    public void setController(DrawingController newController) {
        // Pass the controller to the smaller views
        shapeToolbar.setController(newController);
        drawingView.setController(newController);
        colourToolbar.setController(newController);
    }

    public void setInteractionModel(InteractionModel newIModel) throws InstantiationException, IllegalAccessException {
        shapeToolbar.setInteractionModel(newIModel);
        newIModel.addSubscriber(shapeToolbar);

        colourToolbar.setInteractionModel(newIModel);
        newIModel.addSubscriber(colourToolbar);

        drawingView.setInteractionModel(newIModel);
        newIModel.addSubscriber(drawingView);

        miniDrawingView.setInteractionModel(newIModel);
        newIModel.addSubscriber(miniDrawingView);

        miniDrawingController.setInteractionModel(newIModel);
    }

    @Override
    public void modelChanged() {
    }

}
