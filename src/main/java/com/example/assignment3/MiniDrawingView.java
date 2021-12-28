package com.example.assignment3;

import javafx.scene.paint.Color;

import java.lang.reflect.InvocationTargetException;

public class MiniDrawingView extends DrawingView{
    public MiniDrawingView(double docWidth, double docHeight, double width, double height){
        super(docWidth, docHeight, width, height);
        this.setStyle("""
                -fx-border-color: red;
                -fx-border-insets: 2;
                -fx-border-width: 2;
                -fx-border-style: dashed;
                """);
        this.setMaxSize(100.0, 100.0);
    }

    @Override
    public double viewXtoNorm(double x) {
        return x/this.width;
    }

    @Override
    public double viewYtoNorm (double y){
        return y/this.height;
    }

    @Override
    public void draw() {
        gc.clearRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());
        gc.setFill(Color.GRAY);
        gc.fillRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());
        gc.save();
        gc.scale(this.width/this.docWidth, this.height/this.docHeight);
        drawShapes(false);
        drawViewFinder();
        gc.restore();
    }

    private void drawViewFinder(){
        gc.setGlobalAlpha(0.2);
        gc.setFill(Color.YELLOW);
        gc.fillRect(normXToView(imodel.viewLeft),
                normYToView(imodel.viewTop),
                normXToView(imodel.viewWidth),
                normYToView(imodel.viewHeight));
        gc.setGlobalAlpha(1.0);
    }

    @Override
    public void setInteractionModel(InteractionModel imodel){
        this.imodel = imodel;
        this.viewLeft = 0;
        this.viewTop = 0;
    }

    @Override
    public void setController(DrawingController newController) {
        controller = newController;
        myCanvas.setOnMousePressed(mouseEvent -> controller.handlePressed(viewXtoNorm(mouseEvent.getX()), viewYtoNorm(mouseEvent.getY()), mouseEvent));
        myCanvas.setOnMouseDragged(e -> {
            try {
                controller.handleDragged( viewXtoNorm(e.getX()), viewYtoNorm(e.getY()), e);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException ex) {
                ex.printStackTrace();
            }
        });
        myCanvas.setOnMouseReleased(e -> controller.handleReleased(e));
    }

    @Override
    public void iModelChanged() {
        draw();
    }
}
