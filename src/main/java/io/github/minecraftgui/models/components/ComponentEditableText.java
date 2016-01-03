package io.github.minecraftgui.models.components;

import io.github.minecraftgui.models.attributes.AttributeGroupColor;
import io.github.minecraftgui.models.attributes.AttributeVariableColor;
import io.github.minecraftgui.models.shapes.Shape;

/**
 * Created by Samuel on 2015-12-27.
 */
public abstract class ComponentEditableText extends ComponentText {

    protected final AttributeGroupColor textCursorColor;

    public ComponentEditableText(String id, Class<? extends Shape> shape) {
        super(id, shape);
        this.textCursorColor = new AttributeGroupColor(this);
    }

    public AttributeVariableColor getTextCursorColor(State state){
        return textCursorColor.getAttribute(state);
    }

    @Override
    public void update(long updateId) {
        super.update(updateId);
        textCursorColor.update(updateId);
    }

}
