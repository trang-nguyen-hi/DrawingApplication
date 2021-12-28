package com.example.assignment3;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public class InteractionModel {
    private Paint selectedColor;
    private Class<? extends XShape> selectedShape;
    private XShape selectedObject;
    // the handle of the selected object
    private XCircle handle;

    private ArrayList<InteractionModelSubscriber> subscribers;

    /**
     * The current location and size of the main drawing view in the document (normalized)
     */
    protected double viewLeft, viewTop, viewWidth, viewHeight;

    public InteractionModel() {
        subscribers = new ArrayList<>();
    }

    public void addSubscriber(InteractionModelSubscriber aSub) {
        subscribers.add(aSub);
    }

    private void notifySubscribers() {
        subscribers.forEach(InteractionModelSubscriber::iModelChanged);
    }

    /**
     * Sets selected colour.
     * @param newColour the new colour
     */
    public void setSelectedColour(Paint newColour) {
        selectedColor = newColour;
        notifySubscribers();
    }

    /**
     * Sets selected shape.
     *
     * @param newShape the shape class
     */
    public void setSelectedShape(Class<? extends XShape> newShape) {
        selectedShape = newShape;
        notifySubscribers();
    }

    /**
     * Set selected object.
     *
     * @param object the object
     */
    public void setSelectedObject(XShape object){
        selectedObject = object;
        handle = new XCircle(selectedObject.right - 0.002, selectedObject.bottom - 0.002, selectedObject.right + 0.002, selectedObject.bottom + 0.002, Color.YELLOW);
        notifySubscribers();
    }

    /**
     * Update handle.
     */
    public void updateHandle(){
        if ( isSelectedObject()) {
            handle = new XCircle(selectedObject.right - 0.002, selectedObject.bottom - 0.002, selectedObject.right + 0.002, selectedObject.bottom + 0.002, Color.YELLOW);
        }
        notifySubscribers();
    }

    /**
     * Update view.
     *
     * @param viewLeft   the view left
     * @param viewTop    the view top
     * @param viewWidth  the view width
     * @param viewHeight the view height
     */
    public void updateView (double viewLeft, double viewTop, double viewWidth, double viewHeight){
        this.viewLeft = viewLeft;
        this.viewTop = viewTop;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        notifySubscribers();
    }

    /**
     * If the given point is inside the View Finder
     * @param normX the normalized x
     * @param normY the normalized y
     * @return the boolean
     */
    public boolean onViewFinder(double normX, double normY){
        return viewLeft <= normX
                && viewLeft + viewWidth >= normX
                && viewTop <= normY
                && viewTop + viewHeight >= normY;
    }

    /**
     * Move view.
     *
     * @param dX the distance x
     * @param dY the distance y
     */
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

    /**
     * Is the given point inside the handle circle
     * @param x the x
     * @param y the y
     * @return the boolean
     */
    public boolean onHandle(double x, double y){
        if ( handle == null){
            return false;
        }
        return handle.contains(x, y);
    }

    /**
     * Unselect object.
     */
    public void unselectObject(){
        selectedObject = null;
        handle = null;
        notifySubscribers();
    }

    /**
     * Is any object being selected.
     * @return the boolean
     */
    public boolean isSelectedObject(){
        return selectedObject != null;
    }

    /**
     * Gets selected color.
     *
     * @return the selected color
     */
    public Paint getSelectedColor() {
        return selectedColor;
    }

    /**
     * Gets selected shape.
     *
     * @return the selected shape
     */
    public Class<? extends XShape> getSelectedShape() {
        return selectedShape;
    }

    /**
     * Gets selected object.
     *
     * @return the selected object
     */
    public XShape getSelectedObject() {
        return selectedObject;
    }

    /**
     * Get handle x circle.
     *
     * @return the x circle
     */
    protected XCircle getHandle(){
        return handle;
    }

}
