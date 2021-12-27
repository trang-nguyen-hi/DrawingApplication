package com.example.assignment3;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.lang.invoke.VolatileCallSite;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.SynchronousQueue;

public class ShapeToolbar extends VBox implements InteractionModelSubscriber{
    ToggleButton rectButton;
    Rectangle rectangle;

    ToggleButton squareButton;
    Rectangle square;

    ToggleButton circleButton;
    Circle circle;

    ToggleButton ovalButton;
    Ellipse oval;

    ToggleButton lineButton;
    Line line;

    ArrayList<ToggleButton> buttonList;

    DrawingController controller;
    DrawingModel model;
    InteractionModel iModel;

    public ShapeToolbar(){
        rectButton = new ToggleButton();
        rectangle = new Rectangle(40, 20);
        rectButton.setGraphic(new VBox(rectangle, new Label("Rectangle")));

        squareButton = new ToggleButton();
        square = new Rectangle(30, 30);
        squareButton.setGraphic(new VBox(square, new Label("Square")));


        circleButton = new ToggleButton();
        circle = new Circle(15);
        circleButton.setGraphic(new VBox(circle, new Label("Circle")));


        ovalButton = new ToggleButton();
        oval = new Ellipse(20, 10);
        ovalButton.setGraphic(new VBox(oval, new Label("Oval")));


        lineButton = new ToggleButton();
        line = new Line(0, 0, 20, 20);
        line.setStrokeWidth(5);
        lineButton.setGraphic(new VBox(line, new Label("Line")));

        buttonList = new ArrayList<>(Arrays.asList(rectButton, squareButton, circleButton, ovalButton, lineButton));

        ToggleGroup toggleGroup = new ToggleGroup();
        buttonList.forEach(toggleButton -> {
            ((VBox) toggleButton.getGraphic()).setAlignment(Pos.CENTER);
            toggleButton.setToggleGroup(toggleGroup);
            toggleButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            this.setVgrow(toggleButton, Priority.ALWAYS);
            this.getChildren().add(toggleButton);
        });
    }

    public void setModel(DrawingModel newModel) {
        model = newModel;
    }

    public void setController(DrawingController newController) {
        controller = newController;
        buttonList.forEach(cr -> cr.setOnMouseClicked(e -> controller.setShape(((Label)((VBox)cr.getGraphic()).getChildren().get(1)).getText())));
    }

    public void setInteractionModel(InteractionModel newIModel) {
        iModel = newIModel;
        // set initial shape
        iModel.setShape("Rectangle");
    }

    @Override
    public void iModelChanged() {
        // set border highlight for selected shape
        buttonList.forEach(cr -> {
            String buttonlabel = ((Label)((VBox)cr.getGraphic()).getChildren().get(1)).getText();
            Shape buttonShape = ((Shape)((VBox)cr.getGraphic()).getChildren().get(0));
            if (buttonlabel.equals(iModel.getSelectedShape())) {
                if ( buttonlabel.equals("Line")) {
                    line.setStroke(iModel.getSelectedColor());
                }
                else {
                    buttonShape.setFill(iModel.getSelectedColor());
                }
            } else {
                if ( buttonlabel.equals("Line")) {
                    line.setStroke(Color.BLACK);
                }
                else {
                    buttonShape.setFill(Color.BLACK);
                }
            }
        });
    }
}
