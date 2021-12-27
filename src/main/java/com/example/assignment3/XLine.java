package com.example.assignment3;

import javafx.scene.paint.Paint;

import java.security.PublicKey;

public class XLine extends XShape{
    // ADD
    double ratioA, ratioB, ratioC;
    double tolerance;
    double length;
    // END ADD

    public XLine(double x1, double y1, double x2, double y2, Paint colour){
        super(x1, y1, x2, y2, colour);
        this.update(x2, y2);
    }

    // ADD
    private double dist(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
    }

    @Override
    public void update(double x2, double y2) {
        this.right = x2;
        this.bottom = y2;
        this.tolerance = 0.002;
        length = dist(this.left,this.top,this.right,this.bottom);
        ratioA = (this.top - this.bottom) / length;
        ratioB = (this.right - this.left) / length;
        ratioC = -1 * ((this.top-this.bottom) * this.left + (this.right-this.left) * this.top) / length;
    }

    @Override
    public boolean contains(double x, double y) {
        return Math.abs(distanceFromLine(x,y)) < tolerance &&
        dist(x,y,this.left,this.top) < length &&
                dist(x,y,this.right,this.bottom) < length;

    }

    private double distanceFromLine(double x, double y) {
        return ratioA * x + ratioB * y + ratioC;
    }
    // END ADD

    @Override
    public String getSubType() {
        return "Line";
    }
}
