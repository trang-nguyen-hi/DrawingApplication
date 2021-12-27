package com.example.assignment3;

import javafx.scene.paint.Paint;

import java.lang.invoke.MethodType;

public abstract class XShape {
    double left, top, right, bottom;
    protected double rootX, rootY;
    Paint colour;
    int zNumber;

    public XShape(double newLeft, double newTop, double newRight, double newBottom, Paint newPaint) {
        left = newLeft;
        top = newTop;
        right = newRight;
        bottom = newBottom;

        rootX = newLeft;
        rootY = newTop;

        colour = newPaint;
    }

    public abstract boolean contains(double x, double y);

    public abstract String getSubType();

    public void update(double x2, double y2){
        this.left = Math.min(rootX, x2);
        this.right = Math.max(rootX, x2);
        this.top = Math.min(rootY, y2);
        this.bottom = Math.max(rootY, y2);
    };

    public Paint getColour(){
        return colour;
    }

    public String toString(){
        return "Type: " + getSubType()
                +" Left: " + left
                +" Top: " + top
                +" Right: " + right
                +" Bottom: " + bottom;
    }

    public void setZ(int number){
        this.zNumber = number;
    }

    public void move(double dX, double dY) {
        left += dX;
        top += dY;
        right += dX;
        bottom += dY;
        updateRoot();
    }

    public void updateRoot(){
        rootX = left;
        rootY = top;
    }

    public int getzNumber(){
        return zNumber;
    }
}
