package com.example.assignment3;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;

public class InteractionModel {
    Paint selectedColor;
    String selectedShape;

    XShape selectedObject;
    XCircle handle;

    ArrayList<InteractionModelSubscriber> subscribers;

    double viewLeft, viewTop, viewWidth, viewHeight;


    public InteractionModel() {
        subscribers = new ArrayList<>();
    }

    public void addSubscriber(InteractionModelSubscriber aSub) {
        subscribers.add(aSub);
    }

    private void notifySubscribers() {
        subscribers.forEach(sub -> sub.iModelChanged());
    }

    public void setColour(Paint newColour) {
        selectedColor = newColour;
        notifySubscribers();
    }

    public void setShape(String newShape){
        if (!Arrays.asList("Rectangle", "Square", "Circle", "Oval", "Line").contains(newShape)){
            throw new InvalidParameterException("Variable newShape is not in the 5 default shapes!");
        }
        selectedShape = newShape;
        notifySubscribers();
    }

    public void setSelectedObject(XShape object){
        selectedObject = object;
        handle = new XCircle(selectedObject.right - 0.002, selectedObject.bottom - 0.002, selectedObject.right + 0.002, selectedObject.bottom + 0.002, Color.YELLOW);
        notifySubscribers();
    }

    public void updateHandle(){
        if ( isSelectedObject()) {
            handle = new XCircle(selectedObject.right - 0.002, selectedObject.bottom - 0.002, selectedObject.right + 0.002, selectedObject.bottom + 0.002, Color.YELLOW);
        }
        notifySubscribers();
    }

    public void updateView (double viewLeft, double viewTop, double viewWidth, double viewHeight){
        this.viewLeft = viewLeft;
        this.viewTop = viewTop;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        notifySubscribers();
    }

    public boolean onViewFinder(double normX, double normY){
        return viewLeft <= normX
                && viewLeft + viewWidth >= normX
                && viewTop <= normY
                && viewTop + viewHeight >= normY;
    }

    public void moveView(double dX, double dY){
        double newLeft = viewLeft + dX;
        double newTop = viewTop + dY;
        if ( newLeft >= 0 && newLeft <= 1-viewWidth &&
            newTop >= 0 && newTop <= 1- viewHeight) {
            viewLeft += dX;
            viewTop += dY;
        }
        notifySubscribers();
    }

    public boolean onHandle(double x, double y){
        if ( handle == null){
            return false;
        }
        return handle.contains(x, y);
    }

    public void unselectObject(){
        selectedObject = null;
        handle = null;
        notifySubscribers();
    }

    public boolean isSelectedObject(){
        return selectedObject != null;
    }

    public Paint getSelectedColor() {
        return selectedColor;
    }

    public String getSelectedShape() {
        return selectedShape;
    }

    public XShape getSelectedObject() {
        return selectedObject;
    }

    protected XCircle getHandle(){
        return handle;
    }

}
