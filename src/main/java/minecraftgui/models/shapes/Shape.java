/*
 *     Minecraft GUI mod
 *     Copyright (C) 2015  Samuel Marchildon-Lavoie
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package minecraftgui.models.shapes;

import minecraftgui.models.*;
import minecraftgui.models.components.*;
import minecraftgui.models.attributes.Attribute;
import minecraftgui.models.attributes.AttributeGroup;
import minecraftgui.models.attributes.AttributeGroupColor;
import minecraftgui.models.attributes.AttributeGroupDouble;
import minecraftgui.models.components.Component;
import minecraftgui.models.images.Image;
import minecraftgui.models.images.StaticImage;

import java.awt.*;

/**
 * Created by Samuel on 2015-10-22.
 */
public abstract class Shape<V> extends AttributeGroup implements Updatable, Drawable {

    public static final Color TRANSPARENT = new Color(0,0,0,0);
    public static final Image DEFAULT_IMAGE = new StaticImage(null);

    private final AttributeGroupDouble width;
    private final AttributeGroupDouble height;

    public abstract boolean isLocationInside(double x, double y);
    public abstract double getMargin(Margin margin);
    public abstract double getBorder(Border border);
    public abstract Color getBorderColor(Border border);
    public abstract double getPadding(Padding padding);
    public abstract AttributeGroupDouble getAttributeMargin(Margin margin);
    public abstract AttributeGroupDouble getAttributeBorder(Border border);
    public abstract AttributeGroupColor getAttributeBorderColor(Border border);
    public abstract AttributeGroupDouble getAttributePadding(Padding padding);
    public abstract void setMargin(State state, Margin margin, Attribute<Double> attribute);
    public abstract void setBorder(State state, Border border, Attribute<Double> attributeDouble);
    public abstract void setBorderColor(State state, Border border, Attribute<Color> attributeColor);
    public abstract void setPadding(State state, Padding padding, Attribute<Double> attributeDouble);

    public Shape(Component component, V defaultValue) {
        super(component, defaultValue);
        this.width = new AttributeGroupDouble(this.component);
        this.height = new AttributeGroupDouble(this.component);
    }

    @Override
    public void update(long updateId) {
        super.update(updateId);
        this.width.update(updateId);
        this.height.update(updateId);
    }

    public Component getComponent() {
        return component;
    }

    public void setBackground(State state, Attribute<V> background){
        setAttribute(state, background);
    }

    public void setWidth(State state, Attribute<Double> attributeDouble){
        width.setAttribute(state, attributeDouble);
    }

    public void setHeight(State state, Attribute<Double> attributeDouble){
        height.setAttribute(state, attributeDouble);
    }

    public double getWidth() {
        return (double) width.getValue() - getMargin(Margin.LEFT) - getMargin(Margin.RIGHT);
    }

    public double getHeight() {
        return (double) height.getValue() - getMargin(Margin.TOP) - getMargin(Margin.BOTTOM);
    }

    public AttributeGroupDouble getAttributeWidth(){
        return width;
    }

    public AttributeGroupDouble getAttributHeight(){
        return height;
    }

}
