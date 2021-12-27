package com.example.assignment3;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class DrawingController {
    InteractionModel iModel;
    DrawingModel model;
    double prevX, prevY = Double.NaN;


    // ADD
    private enum State {
        READY,
        NOT_ON_SHAPE,
        DRAWING,
        ON_SHAPE,
        MOVE,
        PANNING
    }

    private State currentState;

    // END ADD
    public DrawingController() {
        // ADD
        currentState = State.READY;
        // END ADD
    }

    public void setInteractionModel(InteractionModel newModel) {
        iModel = newModel;
    }

    public void setModel(DrawingModel newModel) {
        model = newModel;
    }

    public void handlePressed(Double normX, Double normY, MouseEvent event) {
        prevX = normX;
        prevY = normY;
        // ADD

        if ( event.isSecondaryButtonDown()){
            currentState = State.PANNING;
        }
        else if (iModel.onHandle(normX, normY)){
            currentState = State.DRAWING;
        }
        else if ( !model.contains(normX, normY)){
            currentState = State.NOT_ON_SHAPE;
        }
        else {
            currentState = State.ON_SHAPE;
        }
        // END ADD
    }

    public void handleDragged(Double normX, Double normY, MouseEvent event) {
        // ADD
        switch (currentState) {
            case NOT_ON_SHAPE -> {
                // END ADD
                model.addShape(iModel.getSelectedShape(), prevX, prevY, normX, normY, iModel.getSelectedColor());
                iModel.setSelectedObject(model.getShapes().get(model.getShapes().size() - 1));
                currentState = State.DRAWING;
            }
            case DRAWING -> {
                model.updateLast(normX, normY);
                iModel.updateHandle();
            }
            case ON_SHAPE -> {
                currentState = State.MOVE;
                double dX = normX - prevX;
                double dY = normY - prevY;
                iModel.setSelectedObject(model.whichShape(prevX, prevY));
                model.setZandSort(model.whichShape(prevX, prevY));
                model.moveShape(model.whichShape(prevX, prevY), dX, dY);
                iModel.updateHandle();
                prevX = normX;
                prevY = normY;
            }
            case MOVE ->  {
                double dX = normX - prevX;
                double dY = normY - prevY;
                model.moveShape(model.getShapes().get(model.getShapes().size() - 1), dX, dY);
                iModel.updateHandle();
                prevX = normX;
                prevY = normY;
            }
            case PANNING -> {
                double dX = prevX - normX;
                double dY = prevY - normY;
                iModel.moveView(dX, dY);
            }
        }
    }

    // ADD

    public void handleReleased(MouseEvent event) {
        switch (currentState){
            // if click on the canvas and release, unselect everything
            case NOT_ON_SHAPE-> {
                iModel.unselectObject();
                currentState = State.READY;
            }
            case MOVE, PANNING -> {
                currentState = State.READY;
            }
            case DRAWING -> {
                iModel.getSelectedObject().updateRoot();
                currentState = State.READY;
            }
            case ON_SHAPE -> {
                iModel.setSelectedObject(model.whichShape(prevX, prevY));
                model.setZandSort(model.whichShape(prevX, prevY));
                currentState = State.READY;
            }
        }
    }

    public void handleKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.BACK_SPACE || keyEvent.getCode() == KeyCode.DELETE){
            if (iModel.isSelectedObject()){
                model.removeShape(iModel.getSelectedObject());
            }
        }

    }

    public void handleResizeView(double normWidth, double normHeight){
        iModel.updateView(iModel.viewLeft, iModel.viewTop, normWidth, normHeight);
    }

    public void setColour(Paint newColour) {
        iModel.setColour(newColour);
    }

    public void setShape(String newShape){
        iModel.setShape(newShape);
    }
}
