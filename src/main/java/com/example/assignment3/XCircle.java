package com.example.assignment3;

import javafx.scene.paint.Paint;

public class XCircle extends XShape{
    double cx, cy, diameter;

    public XCircle(double x1, double y1, double x2, double y2, Paint colour) {
        super(x1, y1, x2, y2, colour);
        this.update(x2, y2);
    }

    @Override
    public boolean contains(double x, double y) {
        cx = left + diameter/2;
        cy = top + diameter/2;
        return Math.hypot(x-cx,y-cy) <= diameter/2;
    }

    @Override
    public String getSubType() {
        return "Circle";
    }

    @Override
    public void update(double x2, double y2) {
        super.update(x2, y2);
        if ( this.right - this.left < this.bottom - this.top){
            this.diameter = this.right - this.left;
            if (this.rootY == this.top){
                this.bottom = this.top + this.diameter;
            }
            else{
                this.top = this.bottom - this.diameter;
            }
        }else{
            this.diameter = this.bottom - this.top;
            if (this.rootX == this.left){
                this.right = this.left + this.diameter;
            }
            else{
                this.left = this.right - this.diameter;
            }
        }
    }
}
