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

import io.github.minecraftgui.controllers.Render;
import io.github.minecraftgui.models.attributes.Attribute;
import io.github.minecraftgui.models.attributes.AttributeGroupColor;
import io.github.minecraftgui.models.attributes.AttributeGroupDouble;
import io.github.minecraftgui.models.components.*;
import io.github.minecraftgui.models.components.Component;

import java.awt.*;

/**
 * Created by Samuel on 2015-10-23.
 */
public class EllipseColor extends Shape {

    private final AttributeGroupDouble marginTop;
    private final AttributeGroupDouble marginLeft;
    private final AttributeGroupDouble marginRight;
    private final AttributeGroupDouble marginBottom;

    public EllipseColor(Component component) {
        super(component, new AttributeGroupColor(component));
        this.marginTop = new AttributeGroupDouble(this.component);
        this.marginRight = new AttributeGroupDouble(this.component);
        this.marginLeft = new AttributeGroupDouble(this.component);
        this.marginBottom = new AttributeGroupDouble(this.component);
    }

    @Override
    public void update(long updateId) {
        super.update(updateId);
        this.marginTop.update(updateId);
        this.marginRight.update(updateId);
        this.marginLeft.update(updateId);
        this.marginBottom.update(updateId);
    }

    @Override
    public boolean isLocationInside(double x, double y) {
        double yAxis = ((y-component.getY())/getHeight());
        double xAxis = ((x-component.getX())/getWidth());

        return xAxis*xAxis + yAxis*yAxis <= 1;
    }

    @Override
    public double getMargin(Margin margin) {
        switch (margin){
            case BOTTOM: return (double) marginBottom.getValue();
            case TOP: return (double) marginTop.getValue();
            case LEFT: return (double) marginLeft.getValue();
            case RIGHT: return (double) marginRight.getValue();
            default: return 0;
        }
    }

    @Override
    public double getBorder(Border border) {
        return 0;
    }

    @Override
    public Color getBorderColor(Border border) {
        return TRANSPARENT;
    }

    @Override
    public double getPadding(Padding padding) {
        return 0;
    }

    @Override
    public void setMargin(State state, Margin margin, Attribute<Double> attributeDouble) {
        switch (margin){
            case BOTTOM: marginBottom.getAttribute(state).setAttribute(attributeDouble); break;
            case TOP: marginTop.getAttribute(state).setAttribute(attributeDouble); break;
            case LEFT: marginLeft.getAttribute(state).setAttribute(attributeDouble); break;
            case RIGHT: marginRight.getAttribute(state).setAttribute(attributeDouble); break;
            case ALL:
                marginBottom.getAttribute(state).setAttribute(attributeDouble);
                marginTop.getAttribute(state).setAttribute(attributeDouble);
                marginLeft.getAttribute(state).setAttribute(attributeDouble);
                marginRight.getAttribute(state).setAttribute(attributeDouble);
                break;
        }
    }

    @Override
    public void setBorder(State state, Border border, Attribute<Double> attributeDouble) {

    }

    @Override
    public void setBorderColor(State state, Border border, Attribute<Color> attributeColor) {

    }

    @Override
    public void setPadding(State state, Padding padding, Attribute<Double> attributeDouble) {

    }

    @Override
    public AttributeGroupDouble getAttributeMargin(Margin margin){
        switch (margin){
            case BOTTOM: return marginBottom;
            case TOP: return marginTop;
            case LEFT: return marginLeft;
            case RIGHT: return marginRight;
            default: return null;
        }
    }

    @Override
    public AttributeGroupDouble getAttributeBorder(Border border){
        return null;
    }

    @Override
    public AttributeGroupColor getAttributeBorderColor(Border border) {
        return null;
    }

    @Override
    public AttributeGroupDouble getAttributePadding(Padding padding){
        return null;
    }

    @Override
    public void draw(Render render) {
        render.fillOval(component.getX(), component.getY(), getWidth(), getHeight(), (Color) background.getValue());
    }

}
