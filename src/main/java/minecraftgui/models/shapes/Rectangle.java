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

import minecraftgui.models.components.*;
import minecraftgui.models.attributes.Attribute;
import minecraftgui.models.attributes.AttributeGroupColor;
import minecraftgui.models.attributes.AttributeGroupDouble;
import minecraftgui.models.components.Component;

import java.awt.*;

/**
 * Created by Samuel on 2015-10-23.
 */
public abstract class Rectangle<V> extends Shape<V> {

    private final AttributeGroupDouble marginTop;
    private final AttributeGroupDouble marginLeft;
    private final AttributeGroupDouble marginRight;
    private final AttributeGroupDouble marginBottom;
    private final AttributeGroupDouble paddingTop;
    private final AttributeGroupDouble paddingLeft;
    private final AttributeGroupDouble paddingRight;
    private final AttributeGroupDouble paddingBottom;
    private final AttributeGroupDouble borderTop;
    private final AttributeGroupDouble borderLeft;
    private final AttributeGroupDouble borderRight;
    private final AttributeGroupDouble borderBottom;
    private final AttributeGroupColor borderTopColor;
    private final AttributeGroupColor borderLeftColor;
    private final AttributeGroupColor borderRightColor;
    private final AttributeGroupColor borderBottomColor;

    public Rectangle(Component component, V defaultValue) {
        super(component, defaultValue);
        this.marginTop = new AttributeGroupDouble(this.component);
        this.marginRight = new AttributeGroupDouble(this.component);
        this.marginLeft = new AttributeGroupDouble(this.component);
        this.marginBottom = new AttributeGroupDouble(this.component);
        this.borderTop = new AttributeGroupDouble(this.component);
        this.borderRight = new AttributeGroupDouble(this.component);
        this.borderLeft = new AttributeGroupDouble(this.component);
        this.borderBottom = new AttributeGroupDouble(this.component);
        this.paddingTop = new AttributeGroupDouble(this.component);
        this.paddingRight = new AttributeGroupDouble(this.component);
        this.paddingLeft = new AttributeGroupDouble(this.component);
        this.paddingBottom = new AttributeGroupDouble(this.component);
        this.borderTopColor = new AttributeGroupColor(this.component);
        this.borderRightColor = new AttributeGroupColor(this.component);
        this.borderLeftColor = new AttributeGroupColor(this.component);
        this.borderBottomColor = new AttributeGroupColor(this.component);
    }

    @Override
    public void update(long updateId) {
        super.update(updateId);
        this.marginTop.update(updateId);
        this.marginRight.update(updateId);
        this.marginLeft.update(updateId);
        this.marginBottom.update(updateId);
        this.borderTop.update(updateId);
        this.borderRight.update(updateId);
        this.borderLeft.update(updateId);
        this.borderBottom.update(updateId);
        this.paddingTop.update(updateId);
        this.paddingRight.update(updateId);
        this.paddingLeft.update(updateId);
        this.paddingBottom.update(updateId);
        this.borderTopColor.update(updateId);
        this.borderRightColor.update(updateId);
        this.borderLeftColor.update(updateId);
        this.borderBottomColor.update(updateId);
    }

    @Override
    public boolean isLocationInside(double x, double y) {
        double minX = component.getX()-getPadding(Padding.LEFT)-getBorder(Border.LEFT);
        double maxX = component.getX()+getWidth()+getPadding(Padding.RIGHT)+getBorder(Border.RIGHT);
        double minY = component.getY()-getPadding(Padding.TOP)-getBorder(Border.TOP);
        double maxY = component.getY()+getHeight()+getPadding(Padding.BOTTOM)+getBorder(Border.BOTTOM);

        return minX <= x && maxX >= x && minY <= y && maxY >= y;
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
        switch (border){
            case BOTTOM: return (double) borderBottom.getValue();
            case TOP: return (double) borderTop.getValue();
            case LEFT: return (double) borderLeft.getValue();
            case RIGHT: return (double) borderRight.getValue();
            default: return 0;
        }
    }

    @Override
    public double getPadding(Padding padding) {
        switch (padding){
            case BOTTOM: return (double) paddingBottom.getValue();
            case TOP: return (double) paddingTop.getValue();
            case LEFT: return (double) paddingLeft.getValue();
            case RIGHT: return (double) paddingRight.getValue();
            default: return 0;
        }
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
        switch (border){
            case BOTTOM: return borderBottom;
            case TOP: return borderTop;
            case LEFT: return borderLeft;
            case RIGHT: return borderRight;
            default: return null;
        }
    }

    @Override
    public AttributeGroupDouble getAttributePadding(Padding padding){
        switch (padding){
            case BOTTOM: return paddingBottom;
            case TOP: return paddingTop;
            case LEFT: return paddingLeft;
            case RIGHT: return paddingRight;
            default: return null;
        }
    }

    @Override
    public void setMargin(State state, Margin margin, Attribute<Double> attributeDouble) {
        switch (margin){
            case BOTTOM: marginBottom.setAttribute(state, attributeDouble); break;
            case TOP: marginTop.setAttribute(state, attributeDouble); break;
            case LEFT: marginLeft.setAttribute(state, attributeDouble); break;
            case RIGHT: marginRight.setAttribute(state, attributeDouble); break;
            case ALL:
                marginBottom.setAttribute(state, attributeDouble);
                marginTop.setAttribute(state, attributeDouble);
                marginLeft.setAttribute(state, attributeDouble);
                marginRight.setAttribute(state, attributeDouble);
                break;
        }
    }

    @Override
    public void setBorder(State state, Border border, Attribute<Double> attributeDouble) {
        switch (border){
            case BOTTOM: borderBottom.setAttribute(state, attributeDouble); break;
            case TOP: borderTop.setAttribute(state, attributeDouble); break;
            case LEFT: borderLeft.setAttribute(state, attributeDouble); break;
            case RIGHT: borderRight.setAttribute(state, attributeDouble); break;
            case ALL:
                borderBottom.setAttribute(state, attributeDouble);
                borderTop.setAttribute(state, attributeDouble);
                borderLeft.setAttribute(state, attributeDouble);
                borderRight.setAttribute(state, attributeDouble);
                break;
        }
    }

    @Override
    public void setPadding(State state, Padding padding, Attribute<Double> attributeDouble) {
        switch (padding){
            case BOTTOM: paddingBottom.setAttribute(state, attributeDouble); break;
            case TOP: paddingTop.setAttribute(state, attributeDouble); break;
            case LEFT: paddingLeft.setAttribute(state, attributeDouble); break;
            case RIGHT: paddingRight.setAttribute(state, attributeDouble); break;
            case ALL:
                paddingBottom.setAttribute(state, attributeDouble);
                paddingTop.setAttribute(state, attributeDouble);
                paddingLeft.setAttribute(state, attributeDouble);
                paddingRight.setAttribute(state, attributeDouble);
                break;
        }
    }

    @Override
    public Color getBorderColor(Border border) {
        switch (border){
            case BOTTOM: return (Color) borderBottomColor.getValue();
            case TOP: return (Color) borderTopColor.getValue();
            case LEFT: return (Color) borderLeftColor.getValue();
            case RIGHT: return (Color) borderRightColor.getValue();
            default: return TRANSPARENT;
        }
    }

    @Override
    public AttributeGroupColor getAttributeBorderColor(Border border) {
        switch (border){
            case BOTTOM: return borderBottomColor;
            case TOP: return borderTopColor;
            case LEFT: return borderLeftColor;
            case RIGHT: return borderRightColor;
            default: return null;
        }
    }

    @Override
    public void setBorderColor(State state, Border border, Attribute<Color> attributeColor) {
        switch (border){
            case BOTTOM: borderBottomColor.setAttribute(state, attributeColor); break;
            case TOP: borderTopColor.setAttribute(state, attributeColor); break;
            case LEFT: borderLeftColor.setAttribute(state, attributeColor); break;
            case RIGHT: borderRightColor.setAttribute(state, attributeColor); break;
            case ALL:
                borderBottomColor.setAttribute(state, attributeColor);
                borderTopColor.setAttribute(state, attributeColor);
                borderLeftColor.setAttribute(state, attributeColor);
                borderRightColor.setAttribute(state, attributeColor);
                break;
        }
    }

}
