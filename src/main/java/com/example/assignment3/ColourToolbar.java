package com.example.assignment3;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class ColourToolbar extends VBox implements InteractionModelSubscriber{
    ArrayList<Button> buttonList = new ArrayList<>();
    DrawingController controller;
    DrawingModel model;
    InteractionModel iModel;

    String cssLayout = "-fx-border-color: red;\n" +
            "-fx-border-width: 2;\n";

    public ColourToolbar(){
        this.setWidth(100);
        this.setSpacing(5);
        setUpColourButton();

    }

    private void setUpColourButton(){
        String list[] = {"AQUA", "VIOLET", "GREEN", "GOLD",
                        "ORANGE", "CORAL", "FUCHSIA", "PERU"};

        // for each colour, create a button for it
        for (String name : list) {
            Button button = new Button(name);
            button.setBackground(new Background(new BackgroundFill(Color.valueOf(name), CornerRadii.EMPTY, Insets.EMPTY)));
            button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            this.getChildren().add(button);
            this.setVgrow(button, Priority.ALWAYS);
            buttonList.add(button);

            // initialize the aqua button
            if (name.equals("AQUA")){
                button.setStyle(cssLayout);
            }
        }
    }

    public void setModel(DrawingModel newModel) {
        model = newModel;
    }

    public void setController(DrawingController newController) {
        controller = newController;
        // whenever a button is clicked, change the selected colour in interaction model
        buttonList.forEach(cr -> cr.setOnMouseClicked(e -> controller.setColour(Color.valueOf(cr.getText()))));
    }

    public void setInteractionModel(InteractionModel newIModel) {
        iModel = newIModel;
        iModel.setColour(Color.AQUA);
    }

    @Override
    public void iModelChanged() {
        // set border highlight for selected colour
        buttonList.forEach(cr -> {
            if (Color.valueOf(cr.getText()).equals(iModel.selectedColor)) {
                cr.setStyle(cssLayout);
            } else {
                cr.setStyle("");
            }
        });
    }
}
