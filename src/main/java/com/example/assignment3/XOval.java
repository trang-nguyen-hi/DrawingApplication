package com.example.assignment3;

import javafx.scene.paint.Paint;

public class XOval extends XShape{
    double width, height;

    public XOval(double x1, double y1, double x2, double y2, Paint colour){
        super(x1, y1, x2, y2, colour);
        setWandH();
    }

    private void setWandH(){
        this.width = this.right - this.left;
        this.height = this.bottom - this.top;
    }

    @Override
    public void update(double x2, double y2) {
        super.update(x2, y2);
        setWandH();
    }

    @Override
    public boolean contains(double x, double y) {
        Double rx = width/2;
        Double ry = height/2;
        Double cx = left + rx;
        Double cy = top + ry;
        return Math.pow(x - cx, 2)/Math.pow(rx, 2) + Math.pow(y - cy, 2)/Math.pow(ry, 2) <= 1;
    }

    @Override
    public String getSubType() {
        return "Oval";
    }
}
