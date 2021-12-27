package com.example.assignment3;

import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class DrawingView extends StackPane implements DrawingModelSubscriber, InteractionModelSubscriber{
    Canvas myCanvas;
    GraphicsContext gc;

    DrawingModel model;
    InteractionModel imodel;
    DrawingController controller;

    double docWidth, docHeight, width, height;

    double viewLeft, viewTop;

    public DrawingView(double docWidth, double docHeight, double canvasWidth, double canvasHeight ) {
        // create canvas
        this.docWidth = docWidth;
        this.docHeight = docHeight;
        this.width = canvasWidth;
        this.height = canvasHeight;

        myCanvas = new Canvas(this.width, this.height);
        this.setMinSize(0, 0);

        gc = myCanvas.getGraphicsContext2D();
        gc.setLineWidth(5.0);
        gc.strokeLine(20, 20,100,100);
        this.getChildren().add(myCanvas);

        //initialize view
        viewLeft = 500;
        viewTop = 500;
    }

    public void setModel(DrawingModel newModel) {
        model = newModel;
    }

    public void setInteractionModel(InteractionModel imodel){
        this.imodel = imodel;
        imodel.updateView(viewLeft/docWidth, viewTop/docHeight,
                myCanvas.getWidth()/docWidth, myCanvas.getHeight()/docHeight );
    }

    public void setController(DrawingController newController) {
        controller = newController;
//        // set up canvas event handling
        // Why if i have this code for the children class, it would never stop growing
        if (width != 100) {
            this.widthProperty().addListener((observable, oldVal, newVal) -> {
                if ((viewLeft + newVal.doubleValue()) < docWidth) {
                    myCanvas.setWidth(newVal.doubleValue());
                    draw();
                    controller.handleResizeView(myCanvas.getWidth() / docWidth, myCanvas.getHeight() / docHeight);
                }
            });
            this.heightProperty().addListener((observable, oldVal, newVal) -> {
                if ((viewTop + newVal.doubleValue()) < docHeight) {
                    myCanvas.setHeight(newVal.doubleValue());
                    draw();
                    controller.handleResizeView(myCanvas.getWidth() / docWidth, myCanvas.getHeight() / docHeight);
                }
            });
        }
        myCanvas.setOnMousePressed(e -> controller.handlePressed( viewXtoNorm(e.getX()), viewYtoNorm(e.getY()), e));
        myCanvas.setOnMouseDragged(e -> controller.handleDragged( viewXtoNorm(e.getX()), viewYtoNorm(e.getY()), e));
        myCanvas.setOnMouseReleased(controller::handleReleased);
    }

    public double viewXtoNorm(double x) {
        return (x + this.viewLeft)/this.docWidth;
    }

    public double viewYtoNorm (double y){
        return (y + this.viewTop) / this.docHeight;
    }

    public double normXToView(double num){
        return num * this.docWidth - this.viewLeft;
    }

    public double normYToView(double num){
        return num * this.docHeight - this.viewTop;
    }

    @Override
    public void modelChanged() {
        draw();
    }


    public void draw() {
        gc.clearRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());
        drawShapes(true);
    }

    protected void drawShapes(boolean isDashed){
        model.getShapes().forEach(shape -> {
            switch (shape) {
                case XRectangle rectangle -> this.drawRectangle(rectangle);
                case XSquare square -> this.drawSquare(square);
                case XCircle circle -> this.drawCircle(circle);
                case XLine line -> this.drawLine(line);
                case XOval oval -> this.drawOval(oval);
                case XShape di -> System.out.println("Error: fell through to XShape");
            }
            if ( imodel.getSelectedObject() == shape){
                drawBorderSelected(isDashed, shape);
            }
        });
    }


    protected void drawBorderSelected(boolean isDashed, XShape shape){
        gc.setStroke(Color.RED);
        if (isDashed) {
            gc.setLineWidth(2.5);
            gc.setLineDashes(10);
        }else{
            gc.setLineWidth(5);
            gc.setLineDashes(0);
        }
        if (shape.getSubType().equals("Line")) gc.strokeLine(normXToView(shape.left), normYToView(shape.top), normXToView(shape.right), normYToView(shape.bottom));
        else gc.strokeRect(normXToView(shape.left), normYToView(shape.top), (shape.right - shape.left) * this.docWidth, (shape.bottom - shape.top) * this.docHeight);
        if (isDashed) {
            drawCircle(imodel.getHandle());
        }
    }

    public void drawRectangle(XRectangle rectangle){
        gc.setFill(rectangle.getColour());
        gc.fillRect(normXToView(rectangle.left), normYToView(rectangle.top), rectangle.width * this.docWidth, rectangle.height * this.docHeight);
        gc.setLineWidth(1);
        gc.setLineDashes(0);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(normXToView(rectangle.left), normYToView(rectangle.top), rectangle.width * this.docWidth, rectangle.height * this.docHeight);
    }

    public void drawSquare(XSquare square){
        Double side = Math.min(square.side * this.docWidth, square.side * this.docHeight);
        gc.setFill(square.getColour());
        gc.fillRect(normXToView(square.left), normYToView(square.top), side, side);
        gc.setLineWidth(1);
        gc.setLineDashes(0);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(normXToView(square.left), normYToView(square.top), side, side);
    }
    public void drawCircle(XCircle circle){
        Double diameter = Math.min(circle.diameter * this.docWidth, circle.diameter * this.docHeight);
        gc.setFill(circle.getColour());
        gc.fillOval(normXToView(circle.left), normYToView(circle.top), diameter, diameter);
        gc.setLineWidth(1);
        gc.setLineDashes(0);
        gc.setStroke(Color.BLACK);
        gc.strokeOval(normXToView(circle.left), normYToView(circle.top), diameter, diameter);
    }
    public void drawLine(XLine line){
        gc.setLineDashes(0);
        gc.setLineWidth(5);
        gc.setStroke(line.getColour());
        gc.strokeLine(normXToView(line.left), normYToView(line.top), normXToView(line.right), normYToView(line.bottom));
    }
    public void drawOval(XOval oval){
        gc.setFill(oval.getColour());
        gc.fillOval( normXToView(oval.left), normYToView(oval.top) , (oval.width) * this.docWidth , (oval.height) * this.docHeight);

        gc.setLineWidth(1);
        gc.setLineDashes(0);
        gc.setStroke(Color.BLACK);
        gc.strokeOval(normXToView(oval.left), normYToView(oval.top) , (oval.width) * this.docWidth , (oval.height) * this.docHeight);

    }

    @Override
    public void iModelChanged() {
        this.viewLeft = imodel.viewLeft * docWidth;
        this.viewTop = imodel.viewTop * docHeight;
        draw();
    }
}
