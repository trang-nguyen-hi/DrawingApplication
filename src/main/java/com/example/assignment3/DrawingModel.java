package com.example.assignment3;

import javafx.scene.paint.Paint;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DrawingModel {
    private ArrayList<XShape> shapes;
    private ArrayList<DrawingModelSubscriber> subs;
    /**
     * The next z number.
     */
    int nextZ;

    public DrawingModel() {
        shapes = new ArrayList<>();
        subs = new ArrayList<>();
        nextZ = 0;
    }

    public void addSubscriber(DrawingModelSubscriber aSub) {
        subs.add(aSub);
    }

    private void notifySubscribers() {
        subs.forEach(DrawingModelSubscriber::modelChanged);
    }

    /**
     * Add a shape.
     *
     * @param shape    the shape
     * @param x1       left
     * @param y1       top
     * @param x2       right
     * @param y2       bottom
     * @param newColor the shape's color
     */
    public void addShape(Class<? extends XShape> shape, double x1, double y1, double x2, double y2, Paint newColor) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<?> ctr = shape.getConstructor(double.class, double.class, double.class, double.class, Paint.class);
        getShapes().add((XShape) ctr.newInstance(new Object[] {x1, y1, x2, y2, newColor }));
        // set the Z number of the new shape
        shapes.get(shapes.size() - 1).setZ(nextZ);
        nextZ += 1;
        notifySubscribers();
    }

    /**
     * Remove shape.
     *
     * @param shape the shape
     */
    public void removeShape(XShape shape){
        getShapes().remove(shape);
        notifySubscribers();
    }

    /**
     * Set z and sort the shape list.
     *
     * @param shape the shape
     */
    public void setZandSort(XShape shape){
        shape.setZ(nextZ);
        nextZ += 1;
        shapes.sort(Comparator.comparingInt(XShape::getzNumber));
        notifySubscribers();
    }

    /**
     * Move shape.
     *
     * @param shape the shape
     * @param dX    the distance x
     * @param dY    the distance y
     */
    public void moveShape(XShape shape, double dX, double dY) {
        shape.move(dX,dY);
        notifySubscribers();
    }

    /**
     * Gets the list of shapes.
     *
     * @return the shapes
     */
    public List<XShape> getShapes() {
        return shapes;
    }

    /**
     * Resize a shape.
     *
     * @param shape the shape
     * @param x2    new right
     * @param y2    new bottom
     */
    public void resize (XShape shape, double x2, double y2){
        shape.update(x2, y2);
        notifySubscribers();
    }

    /**
     * Which shape x contains the given pair of coordinates.
     * @return the x shape
     */
    public XShape whichShape(double x, double y){
        for (int j = shapes.size() - 1; j >= 0; j--) {
            if ( shapes.get(j).contains(x, y)){
                return shapes.get(j);
            }
        }
        return null;
    }

    /**
     * If any shape contains the given x and y
     * @return the boolean
     */
    public boolean contains(double x, double y) {
        return shapes.stream().anyMatch(shape -> shape.contains(x,y));
    }

}
