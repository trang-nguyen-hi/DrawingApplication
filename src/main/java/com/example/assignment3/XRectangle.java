package com.example.assignment3;

import javafx.scene.paint.Paint;

public class XRectangle extends XShape{
    double width;
    double height;

    public XRectangle(double x1, double y1, double x2, double y2, Paint colour){
        super(x1, y1, x2, y2, colour);
        this.width = x2 - x1;
        this.height = y2 - y1;
    }

    @Override
    public void update(double x2, double y2) {
        super.update(x2, y2);
        this.width = this.right - this.left;
        this.height = this.bottom - this.top;
    }

    @Override
    public boolean contains(double x, double y) {
        return x >= left && x <= left+width && y >= top && y <= top+height;
    }

    @Override
    public String getSubType() {
        return "Rectangle";
    }
}
