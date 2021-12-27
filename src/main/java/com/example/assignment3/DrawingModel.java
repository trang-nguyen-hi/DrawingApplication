package com.example.assignment3;

import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DrawingModel {
    private ArrayList<XShape> shapes;
    private ArrayList<DrawingModelSubscriber> subs;
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
        subs.forEach(sub -> sub.modelChanged());
    }

    public void addShape(String shape, double x1, double y1, double x2, double y2, Paint newColor) {
        switch (shape){
            case "Rectangle" -> shapes.add(new XRectangle(x1,y1,x2,y2,newColor));
            case "Square" -> shapes.add(new XSquare(x1,y1,x2,y2,newColor));
            case "Circle" -> shapes.add(new XCircle(x1,y1,x2,y2,newColor));
            case "Line" -> shapes.add(new XLine(x1,y1,x2,y2,newColor));
            case "Oval" -> shapes.add(new XOval(x1,y1,x2,y2,newColor));
            default -> System.out.println("shape name given to addShape is wrong");
        }
        shapes.get(shapes.size() - 1).setZ(nextZ);
        nextZ += 1;
        notifySubscribers();
    }

    public void removeShape(XShape shape){
        getShapes().remove(shape);
        notifySubscribers();
    }

    public void setZandSort(XShape shape){
        shape.setZ(nextZ += 1);
        nextZ += 1;
        shapes.sort(Comparator.comparingInt(XShape::getzNumber));
        notifySubscribers();
    }

    public void moveShape(XShape shape, double dX, double dY) {
        shape.move(dX,dY);
        notifySubscribers();
    }

    public List<XShape> getShapes() {
        return shapes;
    }

    public void updateLast(double x2, double y2){
        XShape last = getShapes().get(getShapes().size() - 1);
        if (getShapes().size() > 0 ) {
            last.update(x2, y2);
        }
        notifySubscribers();
    }

    public XShape whichShape(double x, double y){
        for (int j = shapes.size() - 1; j >= 0; j--) {
            if ( shapes.get(j).contains(x, y)){
                return shapes.get(j);
            }
        }
        return null;
    }

    // ADD
    public boolean contains(double x, double y) {
        return shapes.stream().anyMatch(shape -> shape.contains(x,y));
        // alternate implementation:

//        boolean found = false;
//        for (SketchLine line : lines) {
//            if (line.contains(x,y)) found = true;
//        }
//        return found;
    }

}
