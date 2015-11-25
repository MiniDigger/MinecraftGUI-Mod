package minecraftgui.models.components;

import minecraftgui.models.attributes.AttributeDouble;
import minecraftgui.models.attributes.AttributeRelativeDouble;
import minecraftgui.models.shapes.Rectangle;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Samuel on 2015-11-20.
 */
public abstract class ProgressBar extends Component {

    protected final AttributeDouble progressComponentAttribute;
    protected final Component progressComponent;
    protected double barPercentage = 0;

    public ProgressBar(String id, Component parent, Class<? extends Rectangle> shape, Class<? extends Rectangle> shapeProgress) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        super(id, parent, shape);
        this.progressComponentAttribute = new AttributeDouble(0);
        progressComponent = new Div("", this, shapeProgress);
    }

    public Component getProgressComponent() {
        return progressComponent;
    }

    public void setBarPercentage(double percentage){
        if(percentage < 0)
            barPercentage = 0;
        else if(percentage > 1)
            barPercentage = 1;
        else
            barPercentage = percentage;
    }

    public double getBarPercentage() {
        return barPercentage;
    }

    public static class Horizontal extends ProgressBar{

        public Horizontal(String id, Component parent, Class<? extends Rectangle> shape, Class<? extends Rectangle> shapeProgress) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
            super(id, parent, shape, shapeProgress);

            progressComponent.getShape().setHeight(State.NORMAL, new AttributeRelativeDouble(this.getAttributHeight()));
            progressComponent.getShape().setWidth(State.NORMAL, progressComponentAttribute);
        }

        @Override
        public void update(long updateId) {
            super.update(updateId);

            progressComponentAttribute.setValue(this.getWidth() * barPercentage);
        }
    }

    public static class Vertical extends ProgressBar{

        public Vertical(String id, Component parent, Class<? extends Rectangle> shape, Class<? extends Rectangle> shapeProgress) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
            super(id, parent, shape, shapeProgress);

            progressComponent.getShape().setWidth(State.NORMAL, new AttributeRelativeDouble(this.getAttributeWidth()));
            progressComponent.getShape().setHeight(State.NORMAL, progressComponentAttribute);
        }

        @Override
        public void update(long updateId) {
            super.update(updateId);

            progressComponentAttribute.setValue(this.getHeight() * barPercentage);
        }
    }

}
