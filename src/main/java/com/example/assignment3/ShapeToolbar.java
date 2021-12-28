package com.example.assignment3;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.util.ArrayList;
import java.util.Arrays;

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
        rectButton.setId(XRectangle.class.getName());

        squareButton = new ToggleButton();
        square = new Rectangle(30, 30);
        squareButton.setGraphic(new VBox(square, new Label("Square")));
        squareButton.setId(XSquare.class.getName());

        circleButton = new ToggleButton();
        circle = new Circle(15);
        circleButton.setGraphic(new VBox(circle, new Label("Circle")));
        circleButton.setId(XCircle.class.getName());

        ovalButton = new ToggleButton();
        oval = new Ellipse(20, 10);
        ovalButton.setGraphic(new VBox(oval, new Label("Oval")));
        ovalButton.setId(XOval.class.getName());

        lineButton = new ToggleButton();
        line = new Line(0, 0, 20, 20);
        line.setStrokeWidth(5);
        lineButton.setGraphic(new VBox(line, new Label("Line")));
        lineButton.setId(XLine.class.getName());

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
        buttonList.forEach(cr -> cr.setOnMouseClicked(e -> {
            try {
                controller.setShape((Class<? extends XShape>) Class.forName(cr.getId()));
            } catch (InstantiationException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }));
    }

    public void setInteractionModel(InteractionModel newIModel) throws InstantiationException, IllegalAccessException {
        iModel = newIModel;
        // set initial shape
        iModel.setSelectedShape(XRectangle.class);
    }

    @Override
    public void iModelChanged() {
        // set border highlight for selected shape
        buttonList.forEach(cr -> {
            Shape buttonShape = ((Shape)((VBox)cr.getGraphic()).getChildren().get(0));
            if (cr.getId().equals(iModel.getSelectedShape().getName())) {
                if ( cr.getId().equals(XLine.class.getName())) {
                    line.setStroke(iModel.getSelectedColor());
                }
                else {
                    buttonShape.setFill(iModel.getSelectedColor());
                }
            } else {
                if ( cr.getId().equals(XLine.class.getName())) {
                    line.setStroke(Color.BLACK);
                }
                else {
                    buttonShape.setFill(Color.BLACK);
                }
            }
        });
    }
}
