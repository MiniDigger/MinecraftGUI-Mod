package minecraftgui.models.components;

import minecraftgui.controllers.Mouse;
import minecraftgui.models.attributes.AttributeDouble;
import minecraftgui.models.attributes.AttributeRelativeDouble;
import minecraftgui.models.attributes.PositionRelative;
import minecraftgui.models.components.listeners.OnMouseButtonDownListener;
import minecraftgui.models.shapes.Rectangle;
import minecraftgui.models.shapes.RectangleColor;
import minecraftgui.models.shapes.Shape;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Samuel on 2015-11-16.
 */
public abstract class Slider extends Component {

    protected final AttributeDouble relativeButtonAttribute;
    protected final ProgressBar progressBar;
    protected final Component button;

    public Slider(String id, Component parent, Class<? extends Rectangle> shape, Class<? extends ProgressBar> progressBar, Class<? extends Shape> buttonShape) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        super(id, parent, shape);
        this.progressBar = progressBar.getDeclaredConstructor(String.class, Component.class, Rectangle.class.getClass()).newInstance("", this, RectangleColor.class);
        this.relativeButtonAttribute = new AttributeDouble(0);
        button = new Div("", this, buttonShape);
        this.progressBar.getShape().setWidth(State.NORMAL, new AttributeRelativeDouble(getAttributeWidth(), 1));
        this.progressBar.getShape().setHeight(State.NORMAL, new AttributeRelativeDouble(getAttributHeight(), 1));
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public Component getButton() {
        return button;
    }

    public void setBarPercentage(double percentage){
        progressBar.setBarPercentage(percentage);
    }

    public double getBarPercentage() {
        return progressBar.getBarPercentage();
    }

    public static class Horizontal extends Slider{

        public Horizontal(String id, Component parent, Class<? extends Rectangle> shape, Class<? extends Shape> buttonShape) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
            super(id, parent, shape, ProgressBar.Horizontal.class, buttonShape);
            this.getProgressBar().addOnMouseButtonDownListener(new OnMouseButtonDownListener() {
                @Override
                public void onMouseButtonDown(Component component, Mouse mouse, Mouse.Button button) {
                    System.out.println("Progress bar");
                    if (button == Mouse.Button.LEFT) {
                        PositionRelative x = (PositionRelative) getxRelative(State.NORMAL);
                        double xLoc = mouse.getX() - x.getRelative();

                        setBarPercentage(xLoc / getWidth());
                    }
                }
            });

            this.getProgressBar().getProgressComponent().addOnMouseButtonDownListener(new OnMouseButtonDownListener() {
                @Override
                public void onMouseButtonDown(Component component, Mouse mouse, Mouse.Button button) {
                    System.out.println("Progress Component");
                    if (button == Mouse.Button.LEFT) {
                        PositionRelative x = (PositionRelative) getxRelative(State.NORMAL);
                        double xLoc = mouse.getX() - x.getRelative();

                        setBarPercentage(xLoc / getWidth());
                    }
                }
            });

            this.button.addOnMouseButtonDownListener(new OnMouseButtonDownListener() {
                @Override
                public void onMouseButtonDown(Component component, Mouse mouse, Mouse.Button button) {
                    System.out.println("CheckBox "+component.getX());
                    if(button == Mouse.Button.LEFT){
                        PositionRelative x = (PositionRelative) component.getxRelative(State.NORMAL);
                        double xLoc = x.getRelative() - (mouse.getXLastUpdate() - mouse.getX());

                        setBarPercentage(xLoc / component.getParent().getWidth());
                    }
                }
            });

            button.setxRelative(State.NORMAL, new PositionRelative.XAxis(0, new AttributeRelativeDouble(button.getAttributeWidth(), -0.5)));
            button.setyRelative(State.NORMAL, new PositionRelative.YAxis(0, new AttributeRelativeDouble(this.getAttributHeight(), 0.5), new AttributeRelativeDouble(button.getAttributHeight(), -0.5)));
        }

        @Override
        public void setBarPercentage(double percentage){
            super.setBarPercentage(percentage);
            PositionRelative x = (PositionRelative) button.getxRelative(State.NORMAL);

            x.setRelative(this.getWidth() * getBarPercentage());
        }

        @Override
        public void update(long updateId) {
            super.update(updateId);
            PositionRelative x = (PositionRelative) button.getxRelative(State.NORMAL);

            x.setRelative(button.getParent().getWidth() * getBarPercentage());
        }
    }

    public static class Vertical extends Slider{

        public Vertical(String id, Component parent, Class<? extends Rectangle> shape, Class<? extends Shape> buttonShape) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
            super(id, parent, shape, ProgressBar.Vertical.class, buttonShape);
            this.getProgressBar().addOnMouseButtonDownListener(new OnMouseButtonDownListener() {
                @Override
                public void onMouseButtonDown(Component component, Mouse mouse, Mouse.Button button) {
                    if (button == Mouse.Button.LEFT) {
                        PositionRelative y = (PositionRelative) getyRelative(State.NORMAL);
                        double yLoc = mouse.getY() - y.getRelative();

                        setBarPercentage(yLoc / getHeight());
                    }
                }
            });

            this.getProgressBar().getProgressComponent().addOnMouseButtonDownListener(new OnMouseButtonDownListener() {
                @Override
                public void onMouseButtonDown(Component component, Mouse mouse, Mouse.Button button) {
                    if (button == Mouse.Button.LEFT) {
                        PositionRelative y = (PositionRelative) getyRelative(State.NORMAL);
                        double yLoc = mouse.getY() - y.getRelative();

                        setBarPercentage(yLoc / getHeight());
                    }
                }
            });

            this.button.addOnMouseButtonDownListener(new OnMouseButtonDownListener() {
                @Override
                public void onMouseButtonDown(Component component, Mouse mouse, Mouse.Button button) {
                    if(button == Mouse.Button.LEFT){
                        PositionRelative y = (PositionRelative) component.getyRelative(State.NORMAL);
                        double yLoc = y.getRelative() - (mouse.getYLastUpdate() - mouse.getY());

                        setBarPercentage(yLoc / component.getParent().getHeight());
                    }
                }
            });

            button.setxRelative(State.NORMAL, new PositionRelative.XAxis(0, new AttributeRelativeDouble(this.getAttributeWidth(), 0.5), new AttributeRelativeDouble(button.getAttributeWidth(), -0.5)));
            button.setyRelative(State.NORMAL, new PositionRelative.YAxis(0, new AttributeRelativeDouble(button.getAttributHeight(), -0.5)));
        }

        @Override
        public void setBarPercentage(double percentage){
            super.setBarPercentage(percentage);
            PositionRelative y = (PositionRelative) button.getyRelative(State.NORMAL);

            y.setRelative(button.getParent().getHeight() * getBarPercentage());
        }

        @Override
        public void update(long updateId) {
            super.update(updateId);
            PositionRelative y = (PositionRelative) button.getyRelative(State.NORMAL);

            y.setRelative(button.getParent().getHeight() * getBarPercentage());
        }
    }

}
