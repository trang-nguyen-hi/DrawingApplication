package com.example.assignment3;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;

import java.lang.reflect.InvocationTargetException;


public class DrawingController {
    InteractionModel iModel;
    DrawingModel model;

    // for
    double prevX, prevY;


    // ADD
    private enum State {
        /**
         * Ready state.
         */
        READY,
        /**
         * Not on shape state.
         */
        NOT_ON_SHAPE,
        /**
         * Drawing/Resizing state.
         */
        DRAWING,
        /**
         * On shape state.
         */
        ON_SHAPE,
        /**
         * Move state.
         */
        MOVE,
        /**
         * Panning state.
         */
        PANNING
    }

    private State currentState;

    public DrawingController() {
        currentState = State.READY;
    }

    public void setInteractionModel(InteractionModel newModel) {
        iModel = newModel;
    }

    public void setModel(DrawingModel newModel) {
        model = newModel;
    }

    /**
     * Handle pressed.
     *
     * @param normX the normalized x
     * @param normY the normalized y
     * @param event the mouse event
     */
    public void handlePressed(Double normX, Double normY, MouseEvent event) {
        prevX = normX;
        prevY = normY;

        // If the right-mouse button is pressed
        if ( event.isSecondaryButtonDown()){
            currentState = State.PANNING;
        }
        // If it is on the handle of the currently-selected shape
        else if (iModel.onHandle(normX, normY)){
            currentState = State.DRAWING;
        }
        // If it is on the background
        else if ( !model.contains(normX, normY)){
            currentState = State.NOT_ON_SHAPE;
        }
        // If it is on a shape
        else {
            currentState = State.ON_SHAPE;
        }
    }

    /**
     * Handle dragged.
     * @param normX the normalized x
     * @param normY the normalized y
     * @param event the mouse event
     */
    public void handleDragged(Double normX, Double normY, MouseEvent event) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        switch (currentState) {
            // Press on the background and drag -> Create new shape and resize it
            case NOT_ON_SHAPE -> {
                model.addShape(iModel.getSelectedShape(), prevX, prevY, normX, normY, iModel.getSelectedColor());
                iModel.setSelectedObject(model.getShapes().get(model.getShapes().size() - 1));
                currentState = State.DRAWING;
            }
            case DRAWING -> {
                model.resize(iModel.getSelectedObject(), normX, normY);
                // update the handle position as we resize the shape
                iModel.updateHandle();
            }
            // Press on a shape and drag -> select and move the shape
            case ON_SHAPE -> {
                currentState = State.MOVE;
                // the moving distance
                double dX = normX - prevX;
                double dY = normY - prevY;
                // select object
                iModel.setSelectedObject(model.whichShape(prevX, prevY));
                // set a new Z number for the selected shape and sort the list
                // so that the newly-selected will be on top of other shapes
                model.setZandSort(model.whichShape(prevX, prevY));
                // move shape
                model.moveShape(model.whichShape(prevX, prevY), dX, dY);
                iModel.updateHandle();
                // get ready for the next movement
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

    /**
     * Handle released.
     *
     * @param event the event
     */
    public void handleReleased(MouseEvent event) {
        switch (currentState){
            // if click on the canvas and release, unselect everything
            case NOT_ON_SHAPE-> iModel.unselectObject();
            case DRAWING -> // finish drawing and set the anchor of the shape at the current top-left coordinates
                    iModel.getSelectedObject().updateAnchor();
            // if click on a shape and release, select the shape
            case ON_SHAPE -> {
                iModel.setSelectedObject(model.whichShape(prevX, prevY));
                model.setZandSort(model.whichShape(prevX, prevY));
            }
        }
        currentState = State.READY;
    }

    /**
     * Handle key pressed.
     *
     * @param keyEvent the key event
     */
    public void handleKeyPressed(KeyEvent keyEvent) {
        // if Delete is pressed, delete the current selected object
        // On Mac, Delete key has keyCode BACK_SPACE
        if (keyEvent.getCode() == KeyCode.BACK_SPACE || keyEvent.getCode() == KeyCode.DELETE){
            if (iModel.isSelectedObject()){
                model.removeShape(iModel.getSelectedObject());
            }
        }
    }

    /**
     * Handle resizing the drawing view
     *
     * @param normWidth  the new normalized width
     * @param normHeight the new normalized height
     */
    public void handleResizeView(double normWidth, double normHeight){
        iModel.updateView(iModel.viewLeft, iModel.viewTop, normWidth, normHeight);
    }

    /**
     * Sets colour.
     *
     * @param newColour the new colour
     */
    public void setColour(Paint newColour) {
        iModel.setSelectedColour(newColour);
    }

    /**
     * Set shape.
     *
     * @param newShape the new shape
     */
    public void setShape(Class<? extends XShape> newShape) throws InstantiationException, IllegalAccessException {
        iModel.setSelectedShape(newShape);
    }
}
