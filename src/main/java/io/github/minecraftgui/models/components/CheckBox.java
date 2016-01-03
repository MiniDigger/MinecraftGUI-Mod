package io.github.minecraftgui.models.components;

import io.github.minecraftgui.controllers.Mouse;
import io.github.minecraftgui.models.listeners.OnClickListener;
import io.github.minecraftgui.models.shapes.Shape;

/**
 * Created by Samuel on 2015-11-21.
 */
public class CheckBox extends ComponentValuable<Boolean> {

    private boolean value = false;
    private final Shape shapeOnValueTrue;
    private final Shape shapeOnValueFalse;

    public CheckBox(String id, Class<? extends Shape> shapeOnValueFalse, Class<? extends Shape> shapeOnValueTrue) {
        super(id, shapeOnValueFalse);
        this.shapeOnValueFalse = this.shape;
        this.shapeOnValueTrue = getShapeByClass(shapeOnValueTrue);

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

    public void setChecked(boolean value) {
        this.value = value;

        if (value)
            shape = shapeOnValueTrue;
        else
            shape = shapeOnValueFalse;

        valueChanged();
    }

    public Shape getShapeOnValueTrue() {
        return shapeOnValueTrue;
    }

    public Shape getShapeOnValueFalse() {
        return shapeOnValueFalse;
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public void setValue(Boolean value) {
        setChecked(value);
    }
}
