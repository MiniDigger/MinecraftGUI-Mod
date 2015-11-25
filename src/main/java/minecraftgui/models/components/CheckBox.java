package minecraftgui.models.components;

import minecraftgui.controllers.Mouse;
import minecraftgui.models.components.listeners.OnClickListener;
import minecraftgui.models.shapes.Shape;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Samuel on 2015-11-21.
 */
public class CheckBox extends Component {

    private boolean value = false;
    private final Shape shapeOnValueTrue;
    private final Shape shapeOnValueFalse;

    public CheckBox(String id, Component parent, final Class<? extends Shape> shapeOnValueFalse, final Class<? extends Shape> shapeOnValueTrue) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        super(id, parent, shapeOnValueFalse);
        this.shapeOnValueFalse = this.shape;
        this.shapeOnValueTrue = shapeOnValueTrue.getDeclaredConstructor(Component.class).newInstance(this);

        this.addOnClickListener(new OnClickListener() {
            @Override
            public void onClick(Component component, Mouse mouse) {
                CheckBox checkBox = (CheckBox) component;
                value = !value;

                if (value)
                    component.shape = checkBox.shapeOnValueTrue;
                else
                    component.shape = checkBox.shapeOnValueFalse;
            }
        });
    }

    public Shape getShapeOnValueTrue() {
        return shapeOnValueTrue;
    }

    public Shape getShapeOnValueFalse() {
        return shapeOnValueFalse;
    }

}
