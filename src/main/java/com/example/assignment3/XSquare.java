package com.example.assignment3;

import javafx.scene.paint.Paint;

public class XSquare extends XShape{
    double side;

    public XSquare(double x1, double y1, double x2, double y2, Paint colour){
        super(x1, y1, x2, y2, colour);
        this.update(x2, y2);
    }

    @Override
    public void update(double x2, double y2) {
        super.update(x2, y2);
        if ( this.right - this.left < this.bottom - this.top){
            this.side = this.right - this.left;
            if (this.rootY == this.top){
                this.bottom = this.top + this.side;
            }
            else{
                this.top = this.bottom - this.side;
            }
        }else{
            this.side = this.bottom - this.top;
            if (this.rootX == this.left){
                this.right = this.left + this.side;
            }
            else{
                this.left = this.right - this.side;
            }
        }
    }

    @Override
    public boolean contains(double x, double y) {
        return x >= left && x <= left+side && y >= top && y <= top+side;
    }

    @Override
    public String getSubType() {
        return "Square";
    }
}
