package io.github.minecraftgui.models.components;

import io.github.minecraftgui.controllers.Mouse;
import io.github.minecraftgui.models.attributes.AttributeDouble;
import io.github.minecraftgui.models.attributes.AttributeVariableDouble;
import io.github.minecraftgui.models.attributes.Position;
import io.github.minecraftgui.models.listeners.OnMouseButtonDownListener;
import io.github.minecraftgui.models.shapes.Rectangle;
import io.github.minecraftgui.models.shapes.RectangleColor;

/**
 * Created by Samuel on 2015-11-16.
 */
public abstract class Slider extends ComponentValuable<Double> {

    protected final AttributeDouble relativeButtonAttribute;
    protected final ProgressBar progressBar;
    protected final Component button;

    public Slider(String id, Class<? extends Rectangle> shape, ProgressBar progressBar, Component button) {
        super(id, shape);
        this.relativeButtonAttribute = new AttributeDouble(0.0);
        this.progressBar = progressBar;
        this.add(this.progressBar);
        this.progressBar.getShape().getWidth(State.NORMAL).setAttribute(getAttributeWidth());
        this.progressBar.getShape().getHeight(State.NORMAL).setAttribute(getAttributeHeight());
        this.add(button);
        this.button = button;
    }

    public Component getButton() {
        return button;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setBarPercentage(double percentage){
        progressBar.setBarPercentage(percentage);
        valueChanged();
    }

    public double getBarPercentage() {
        return progressBar.getBarPercentage();
    }

    @Override
    public Double getValue() {
        return getBarPercentage();
    }

    @Override
    public void setValue(Double value) {
        setBarPercentage(value);
    }

    public static class Horizontal extends Slider{

        public Horizontal(String id, Class<? extends Rectangle> shape, Class<? extends Rectangle> progressShape, Component button) {
            super(id, shape, new ProgressBar.Horizontal("", RectangleColor.class, progressShape), button);
            this.getProgressBar().addOnMouseButtonDownListener(new OnMouseButtonDownListener() {
                @Override
                public void onMouseButtonDown(Component component, Mouse mouse, Mouse.Button button) {
                    if (button == Mouse.Button.LEFT) {
                        Position x = getPositionX();
                        double xLoc = mouse.getX() - x.getRelative();

                        setBarPercentage(xLoc / getWidth());
                    }
                }
            });
            this.button.addOnMouseButtonDownListener(new OnMouseButtonDownListener() {
                @Override
                public void onMouseButtonDown(Component component, Mouse mouse, Mouse.Button button) {
                    if (button == Mouse.Button.LEFT) {
                        Position x = component.getPositionX();
                        double xLoc = x.getRelative() - (mouse.getXLastUpdate() - mouse.getX());

                        setBarPercentage(xLoc / component.getParent().getWidth());
                    }
                }
            });
        }

        @Override
        public void setBarPercentage(double percentage){
            if(this.getBarPercentage() != percentage) {
                super.setBarPercentage(percentage);
                Position x = button.getPositionX();

                ((AttributeVariableDouble) x.getRelative(State.NORMAL)).setAttribute(new AttributeDouble(this.getWidth() * getBarPercentage()));
            }
        }

        @Override
        public void update(long updateId) {
            super.update(updateId);
            Position x = button.getPositionX();

            ((AttributeVariableDouble)x.getRelative(State.NORMAL)).setAttribute(new AttributeDouble(this.getWidth() * getBarPercentage()));
        }
    }

    public static class Vertical extends Slider{

        public Vertical(String id, Class<? extends Rectangle> shape, Class<? extends Rectangle> progressShape, Component button) {
            super(id, shape, new ProgressBar.Vertical("", RectangleColor.class, progressShape), button);
            this.getProgressBar().addOnMouseButtonDownListener(new OnMouseButtonDownListener() {
                @Override
                public void onMouseButtonDown(Component component, Mouse mouse, Mouse.Button button) {
                    if (button == Mouse.Button.LEFT) {
                        Position y = getPositionY();
                        double yLoc = mouse.getY() - y.getRelative();

                        setBarPercentage(yLoc / getHeight());
                    }
                }
            });
            this.button.addOnMouseButtonDownListener(new OnMouseButtonDownListener() {
                @Override
                public void onMouseButtonDown(Component component, Mouse mouse, Mouse.Button button) {
                    if (button == Mouse.Button.LEFT) {
                        Position y = component.getPositionY();
                        double yLoc = y.getRelative() - (mouse.getYLastUpdate() - mouse.getY());

                        setBarPercentage(yLoc / component.getParent().getHeight());
                    }
                }
            });
        }

        @Override
        public void setBarPercentage(double percentage){
            if(this.getBarPercentage() != percentage) {
                super.setBarPercentage(percentage);
                Position y = button.getPositionY();

                y.getRelative(State.NORMAL).setValue(button.getParent().getHeight() * getBarPercentage());
            }
        }

        @Override
        public void update(long updateId) {
            super.update(updateId);
            Position y = button.getPositionY();

            ((AttributeVariableDouble)y.getRelative(State.NORMAL)).setAttribute(new AttributeDouble(button.getParent().getHeight() * getBarPercentage()));
        }
    }

}
