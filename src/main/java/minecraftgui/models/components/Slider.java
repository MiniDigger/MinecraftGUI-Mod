package minecraftgui.models.components;

import minecraftgui.controllers.Mouse;
import minecraftgui.models.attributes.AttributeColor;
import minecraftgui.models.attributes.AttributeDouble;
import minecraftgui.models.attributes.PositionRelative;
import minecraftgui.models.shapes.EllipseColor;
import minecraftgui.models.shapes.Rectangle;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Samuel on 2015-11-16.
 */
public abstract class Slider extends Component {

    protected Component button;
    protected double barPercentage = 0;

    public Slider(String id, Component parent, Class<? extends Rectangle> shape) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        super(id, parent, shape);
    }

    public void setBarPercentage(double percentage){
        this.barPercentage = percentage;
    }

    public double getBarPercentage() {
        return barPercentage;
    }

    public static class Horizontal extends Slider{

        public Horizontal(String id, Component parent, Class<? extends Rectangle> shape) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
            super(id, parent, shape);
            this.button = new Div("", this, EllipseColor.class){

                @Override
                public void onMouseButtonDown(Mouse.Button button, Mouse mouse){
                    if(button == Mouse.Button.LEFT){
                        PositionRelative y = (PositionRelative) this.getyRelative(State.NORMAL);
                        PositionRelative x = (PositionRelative) this.getxRelative(State.NORMAL);
                        double xLoc = x.getRelative() - (mouse.getXLastUpdate() - mouse.getX());

                        if(xLoc < 0)
                            xLoc = 0;
                        else if(xLoc > this.getParent().getWidth())
                            xLoc = this.getParent().getWidth();

                        y.setRelative(this.getParent().getHeight()/2);
                        x.setRelative(xLoc);
                    }
                }

                @Override
                public void onMouseButtonUp(Mouse.Button button, Mouse mouse){
                    if(button == Mouse.Button.LEFT){
                        PositionRelative x = (PositionRelative) this.getxRelative(State.NORMAL);

                        setBarPercentage(x.getValue() / this.getParent().getWidth());
                    }
                }

            };

            button.getShape().setWidth(State.NORMAL, new AttributeDouble(5));
            button.getShape().setHeight(State.NORMAL, new AttributeDouble(5));
            button.getShape().setBackground(State.NORMAL, new AttributeColor(Color.RED));
            button.getShape().setBackground(State.HOVER, new AttributeColor(Color.BLACK));
        }

    }

    public static class Vertical extends Slider{

        public Vertical(String id, Component parent, Class<? extends Rectangle> shape) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
            super(id, parent, shape);
            this.button = new Div("", this, EllipseColor.class){

                @Override
                public void onMouseButtonDown(Mouse.Button button, Mouse mouse){
                    if(button == Mouse.Button.LEFT){
                        PositionRelative y = (PositionRelative) this.getyRelative(State.NORMAL);
                        PositionRelative x = (PositionRelative) this.getxRelative(State.NORMAL);
                        double yLoc = y.getRelative() - (mouse.getYLastUpdate() - mouse.getY());

                        if(yLoc < 0)
                            yLoc = 0;
                        else if(yLoc > this.getParent().getHeight())
                            yLoc = this.getParent().getHeight();

                        y.setRelative(yLoc);
                        x.setRelative(this.getParent().getWidth()/2);
                    }
                }

                @Override
                public void onMouseButtonUp(Mouse.Button button, Mouse mouse){
                    if(button == Mouse.Button.LEFT){
                        PositionRelative y = (PositionRelative) this.getyRelative(State.NORMAL);

                        setBarPercentage(y.getValue()/this.getParent().getHeight());
                    }
                }

            };

            button.getShape().setWidth(State.NORMAL, new AttributeDouble(5));
            button.getShape().setHeight(State.NORMAL, new AttributeDouble(5));
            button.getShape().setBackground(State.NORMAL, new AttributeColor(Color.RED));
            button.getShape().setBackground(State.HOVER, new AttributeColor(Color.BLACK));
        }

    }

}
