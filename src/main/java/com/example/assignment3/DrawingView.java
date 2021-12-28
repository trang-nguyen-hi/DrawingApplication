package com.example.assignment3;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.lang.reflect.InvocationTargetException;

public class DrawingView extends StackPane implements DrawingModelSubscriber, InteractionModelSubscriber{
    protected Canvas myCanvas;
    protected GraphicsContext gc;
    protected DrawingModel model;
    protected InteractionModel imodel;
    protected DrawingController controller;

    // The document size
    protected double docWidth, docHeight,
    // the size of this drawing view/canvas
    width, height;

    /**
     * The top-left corner's coordinates in the document of the drawing view
     */
    protected double viewLeft, viewTop;

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
        // set up canvas resize handlers
        // TODO: Why if i have this code for the MiniDrawingView class, it would never stop growing
        if (width != 100) {
            // if the width is changed
            this.widthProperty().addListener((observable, oldVal, newVal) -> {
                // check if the new right coordinate is outside the Document
                if ((viewLeft + newVal.doubleValue()) < docWidth) {
                    myCanvas.setWidth(newVal.doubleValue());
                    draw();
                    controller.handleResizeView(myCanvas.getWidth() / docWidth, myCanvas.getHeight() / docHeight);
                }
            });
            this.heightProperty().addListener((observable, oldVal, newVal) -> {
                // check if the new bottom coordinate is outside the Document
                if ((viewTop + newVal.doubleValue()) < docHeight) {
                    myCanvas.setHeight(newVal.doubleValue());
                    draw();
                    controller.handleResizeView(myCanvas.getWidth() / docWidth, myCanvas.getHeight() / docHeight);
                }
            });
        }
        myCanvas.setOnMousePressed(e -> controller.handlePressed( viewXtoNorm(e.getX()), viewYtoNorm(e.getY()), e));
        myCanvas.setOnMouseDragged(e -> {
            try {
                controller.handleDragged( viewXtoNorm(e.getX()), viewYtoNorm(e.getY()), e);
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            } catch (NoSuchMethodException ex) {
                ex.printStackTrace();
            } catch (InstantiationException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        });
        myCanvas.setOnMouseReleased(controller::handleReleased);
    }

    /**
     * Convert a x-axis coordinate in the view to normalized coordinate relatively to document size
     * @param x the x
     */
    public double viewXtoNorm(double x) {
        return (x + this.viewLeft)/this.docWidth;
    }

    /**
     * Convert a y-axis coordinate in the view to normalized coordinate relatively to document size
     * @param y the y
     */
    public double viewYtoNorm (double y){
        return (y + this.viewTop) / this.docHeight;
    }

    /**
     * Convert a normalized x to the view x
     * @param num the normalized x
     */
    public double normXToView(double num){
        return num * this.docWidth - this.viewLeft;
    }

    /**
     * Convert a normalized y to the view y
     * @param num the normalized y
     */
    public double normYToView(double num){
        return num * this.docHeight - this.viewTop;
    }

    @Override
    public void modelChanged() {
        draw();
    }


    /**
     * Draw the view
     */
    public void draw() {
        gc.clearRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());
        drawShapes(true);
    }

    /**
     * Draw shapes
     * @param isDashed if the bounding box of the selected shape should be dashed
     *                 (in the main drawing view, the bounding box outline would be dashed,
     *                 but in the miniature view, it is solid for better visibility)
     */
    protected void drawShapes(boolean isDashed){
        model.getShapes().forEach(shape -> {
            switch (shape) {
                case XRectangle rectangle -> this.drawRectangle(rectangle);
                case XSquare square -> this.drawSquare(square);
                case XCircle circle -> this.drawCircle(circle);
                case XLine line -> this.drawLine(line);
                case XOval oval -> this.drawOval(oval);
                default -> System.out.println("DrawingView/drawShapes(): shape is not a valid shape type");
            }
            // draw bounding box of the selected shape
            if ( imodel.getSelectedObject() == shape){
                drawBorder(isDashed, shape);
            }
        });
    }

    protected void drawBorder(boolean isDashed, XShape shape){
        gc.setStroke(Color.RED);
        if (isDashed) {
            gc.setLineWidth(2.5);
            gc.setLineDashes(10);
        }else{
            gc.setLineWidth(5);
            gc.setLineDashes(0);
        }
        // every shape has a bounding box except for line
        // the selected line will have a dashed line on top of it
        if (shape.getSubType().equals("Line")) gc.strokeLine(normXToView(shape.left), normYToView(shape.top), normXToView(shape.right), normYToView(shape.bottom));
        else gc.strokeRect(normXToView(shape.left), normYToView(shape.top), (shape.right - shape.left) * this.docWidth, (shape.bottom - shape.top) * this.docHeight);
        // draw the handle
        if (isDashed) {
            drawCircle(imodel.getHandle());
        }
    }

    /**
     * Draw rectangle.
     *
     * @param rectangle the rectangle
     */
    public void drawRectangle(XRectangle rectangle){
        gc.setFill(rectangle.getColour());
        gc.fillRect(normXToView(rectangle.left), normYToView(rectangle.top), rectangle.width * this.docWidth, rectangle.height * this.docHeight);
        gc.setLineWidth(1);
        gc.setLineDashes(0);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(normXToView(rectangle.left), normYToView(rectangle.top), rectangle.width * this.docWidth, rectangle.height * this.docHeight);
    }

    /**
     * Draw square.
     *
     * @param square the square
     */
    public void drawSquare(XSquare square){
        double side = Math.min(square.side * this.docWidth, square.side * this.docHeight);
        gc.setFill(square.getColour());
        gc.fillRect(normXToView(square.left), normYToView(square.top), side, side);
        gc.setLineWidth(1);
        gc.setLineDashes(0);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(normXToView(square.left), normYToView(square.top), side, side);
    }

    /**
     * Draw circle.
     *
     * @param circle the circle
     */
    public void drawCircle(XCircle circle){
        double diameter = Math.min(circle.diameter * this.docWidth, circle.diameter * this.docHeight);
        gc.setFill(circle.getColour());
        gc.fillOval(normXToView(circle.left), normYToView(circle.top), diameter, diameter);
        gc.setLineWidth(1);
        gc.setLineDashes(0);
        gc.setStroke(Color.BLACK);
        gc.strokeOval(normXToView(circle.left), normYToView(circle.top), diameter, diameter);
    }

    /**
     * Draw line.
     *
     * @param line the line
     */
    public void drawLine(XLine line){
        gc.setLineDashes(0);
        gc.setLineWidth(5);
        gc.setStroke(line.getColour());
        gc.strokeLine(normXToView(line.left), normYToView(line.top), normXToView(line.right), normYToView(line.bottom));
    }

    /**
     * Draw oval.
     *
     * @param oval the oval
     */
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
