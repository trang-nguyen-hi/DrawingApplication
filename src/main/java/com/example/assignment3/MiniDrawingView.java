package com.example.assignment3;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class MiniDrawingView extends DrawingView{
    public MiniDrawingView(double docWidth, double docHeight, double width, double height){
        super(docWidth, docHeight, width, height);
        this.setStyle("-fx-border-color: red;\n" +
                "-fx-border-insets: 2;\n" +
                "-fx-border-width: 2;\n" +
                "-fx-border-style: dashed;\n");
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
        myCanvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                controller.handlePressed(viewXtoNorm(mouseEvent.getX()), viewYtoNorm(mouseEvent.getY()), mouseEvent);
            }
        });
        myCanvas.setOnMouseDragged(e -> controller.handleDragged( viewXtoNorm(e.getX()), viewYtoNorm(e.getY()), e));
        myCanvas.setOnMouseReleased(e -> controller.handleReleased(e));
    }

    @Override
    public void iModelChanged() {
        draw();
    }
}
