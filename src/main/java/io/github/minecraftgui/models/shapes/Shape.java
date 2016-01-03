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

package io.github.minecraftgui.models.shapes;

import io.github.minecraftgui.models.attributes.*;
import io.github.minecraftgui.models.components.*;
import io.github.minecraftgui.models.components.Component;
import io.github.minecraftgui.models.images.StaticImage;
import io.github.minecraftgui.models.Updatable;

import java.awt.*;

/**
 * Created by Samuel on 2015-10-22.
 */
public abstract class Shape implements Updatable, Drawable {

    public static final Color TRANSPARENT = new Color(0,0,0,0);
    public static final io.github.minecraftgui.models.images.Image DEFAULT_IMAGE = new StaticImage(null);

    private final AttributeGroupDouble width;
    private final AttributeGroupDouble height;
    protected final AttributeGroup background;
    protected final Component component;

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

    public Shape(Component component, AttributeGroup background) {
        this.component = component;
        this.background = background;
        this.width = new AttributeGroupDouble(component);
        this.height = new AttributeGroupDouble(component);
    }

    @Override
    public void update(long updateId) {
        this.width.update(updateId);
        this.height.update(updateId);
        this.background.update(updateId);
    }

    public AttributeGroup getAttributeBackground() {
        return background;
    }

    public Attribute getBackground(State state) {
        return background.getAttribute(state);
    }

    public AttributeVariableDouble getWidth(State state){
        return width.getAttribute(state);
    }

    public AttributeVariableDouble getHeight(State state){
        return height.getAttribute(state);
    }

    public double getWidth() {
        return width.getValue() - getMargin(Margin.LEFT) - getMargin(Margin.RIGHT);
    }

    public double getHeight() {
        return height.getValue() - getMargin(Margin.TOP) - getMargin(Margin.BOTTOM);
    }

    public AttributeGroupDouble getAttributeWidth(){
        return width;
    }

    public AttributeGroupDouble getAttributeHeight(){
        return height;
    }

}
