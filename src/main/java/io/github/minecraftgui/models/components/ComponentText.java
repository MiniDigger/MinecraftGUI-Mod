package io.github.minecraftgui.models.components;

import io.github.minecraftgui.models.attributes.*;
import io.github.minecraftgui.models.fonts.Font;
import io.github.minecraftgui.models.shapes.Shape;

import java.awt.*;

/**
 * Created by Samuel on 2015-11-15.
 */
public abstract class ComponentText extends ComponentValuable<String> {

    private final AttributeGroupDouble fontSize;
    private final AttributeGroupColor fontColor;
    private final AttributeGroupFont font;

    public ComponentText(String id, Class<? extends Shape> shape) {
        super(id, shape);
        fontSize = new AttributeGroupDouble(this);
        fontColor = new AttributeGroupColor(this);
        font = new AttributeGroupFont(this);
    }

    public void setFont(State state, Font font){
        this.font.getAttribute(state).setValue(font);
    }

    public void setFontSize(State state, int size){
        fontSize.getAttribute(state).setAttribute(new AttributeDouble((double) size));
    }

    public void setFontColor(State state, Color color){
        fontColor.getAttribute(state).setAttribute(new AttributeColor(color));
    }

    public Font getFont(){
        return font.getValue();
    }

    public Integer getFontSize() {
        return fontSize.getValue().intValue();
    }

    public Color getFontColor() {
        return fontColor.getValue();
    }

    public Double getStringWidth(String str) {
        return getFont().getStringWidth(str, fontSize.getValue().intValue(), fontColor.getValue());
    }

    public Double getStringHeight() {
        return getFont().getStringHeight(fontSize.getValue().intValue(), fontColor.getValue());
    }

    @Override
    public void update(long updateId) {
        super.update(updateId);
        font.update(updateId);
        fontColor.update(updateId);
        fontSize.update(updateId);
    }
}
