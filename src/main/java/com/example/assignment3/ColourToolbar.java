package com.example.assignment3;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class ColourToolbar extends VBox implements InteractionModelSubscriber{
    private final ArrayList<Button> buttonList = new ArrayList<>();
    private DrawingController controller;
    private InteractionModel iModel;

    /**
     * The Css layout for the chosen button
     */
    String cssLayout = """
            -fx-border-color: red;
            -fx-border-width: 2;
            """;

    public ColourToolbar(){
        this.setWidth(100);
        this.setSpacing(5);
        setUpColourButton();
    }

    /**
     * Set up the buttons and initially select the AQUA button
     */
    private void setUpColourButton(){
        String[] list = {"AQUA", "VIOLET", "GREEN", "GOLD",
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

    /**
     * Sets controller.
     *
     * @param newController the new controller
     */
    public void setController(DrawingController newController) {
        controller = newController;
        // whenever a button is clicked, change the selected colour in interaction model
        buttonList.forEach(cr -> cr.setOnMouseClicked(e -> controller.setColour(Color.valueOf(cr.getText()))));
    }

    /**
     * Sets interaction model.
     *
     * @param newIModel the new i model
     */
    public void setInteractionModel(InteractionModel newIModel) {
        iModel = newIModel;
        // initialize the chosen colour
        iModel.setSelectedColour(Color.AQUA);
    }

    @Override
    public void iModelChanged() {
        // highlight the border of the selected colour
        buttonList.forEach(cr -> {
            if (Color.valueOf(cr.getText()).equals(iModel.getSelectedColor())) {
                cr.setStyle(cssLayout);
            } else {
                cr.setStyle("");
            }
        });
    }
}
