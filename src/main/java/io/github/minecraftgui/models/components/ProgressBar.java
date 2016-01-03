package io.github.minecraftgui.models.components;

import io.github.minecraftgui.controllers.Render;
import io.github.minecraftgui.models.shapes.Rectangle;

/**
 * Created by Samuel on 2015-11-20.
 */
public abstract class ProgressBar extends ComponentValuable<Double> {

    protected final Rectangle progressShape;
    protected double barPercentage = 0;

    public ProgressBar(String id, Class<? extends Rectangle> shape, Class<? extends Rectangle> progressShape) {
        super(id, shape);
        this.progressShape = (Rectangle) getShapeByClass(progressShape);
        this.progressShape.getWidth(State.NORMAL).setAttribute(this.getAttributeWidth());
        this.progressShape.getHeight(State.NORMAL).setAttribute(this.getAttributeHeight());
    }

    public Rectangle getProgressShape() {
        return progressShape;
    }

    public void setBarPercentage(double percentage){
        if(percentage < 0)
            barPercentage = 0;
        else if(percentage > 1)
            barPercentage = 1;
        else
            barPercentage = percentage;

        valueChanged();
    }

    public double getBarPercentage() {
        return barPercentage;
    }

    @Override
    public void update(long updateId) {
        super.update(updateId);
        this.progressShape.update(updateId);
    }

    @Override
    public void draw(Render render) {
        super.draw(render);

        this.progressShape.draw(render);
    }

    @Override
    public Double getValue() {
        return barPercentage;
    }

    @Override
    public void setValue(Double value) {
        setBarPercentage(value);
    }

    public static class Horizontal extends ProgressBar{

        public Horizontal(String id, Class<? extends Rectangle> shape, Class<? extends Rectangle> progressShape) {
            super(id, shape, progressShape);
        }

        @Override
        public void setBarPercentage(double percentage){
            super.setBarPercentage(percentage);
            progressShape.getWidth(State.NORMAL).setPercentage(barPercentage);
        }
    }

    public static class Vertical extends ProgressBar{

        public Vertical(String id, Class<? extends Rectangle> shape, Class<? extends Rectangle> progressShape) {
            super(id, shape, progressShape);
        }

        @Override
        public void setBarPercentage(double percentage){
            super.setBarPercentage(percentage);
            progressShape.getHeight(State.NORMAL).setPercentage(barPercentage);
        }

    }

}
