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
public class PolygonColor extends Shape {

    private double minX = 0;
    private double maxX = 0;
    private double minY = 0;
    private double maxY = 0;
    private double positions[][] = new double[0][];

    public PolygonColor(Component component) {
        super(component, new AttributeGroupColor(component));
    }

    public void setPositions(double[][] positions) {
        this.positions = positions;

        minX = Double.MIN_VALUE;
        maxX = Double.MAX_VALUE;
        minY = Double.MIN_VALUE;
        maxY = Double.MAX_VALUE;

        for (double position[]: positions){
            if(position[0] > maxX)
                maxX = position[0];
            else if(position[0] < minX)
                minX = position[0];

            if(position[0] > maxY)
                maxY = position[0];
            else if(position[0] < minY)
                minY = position[0];
        }
    }

    @Override
    public boolean isLocationInside(double x, double y) {
        if(minX <= x && maxX >= x && minY <= y && maxY >= y) {
            int intersect = 0;

            for (int i = 0; i < positions.length; i++)
                intersect += positionIntersectLine(x, y, positions[i], positions[(i + 1) % positions.length]) ? 1 : 0;

            return intersect % 2 == 1;
        }
        return false;
    }

    private boolean positionIntersectLine(double x, double y, double pos1[], double pos2[]){
        double pos1X = pos1[0]+component.getX();
        double pos2X = pos2[0]+component.getX();
        double pos1Y = pos1[1]+component.getY();
        double pos2Y = pos2[1]+component.getY();

        double a = (pos2Y - pos1Y)/(pos2X - pos1X);
        double b = pos2Y - (a*pos2X);

        if(pos2Y < pos1Y){
            if(pos1X == pos2X)
                return pos1X <= x && (pos2Y+1 <= y && y <= pos1Y);
            else if(pos1X < pos2X)
                return !(y <= a*x+b) && (pos2Y+1 <= y && y <= pos1Y);
            else if(pos2X < pos1X)
                return !(y >= a*x+b) && (pos2Y+1 <= y && y <= pos1Y);
        }
        else if(pos1Y < pos2Y){
            if(pos1X == pos2X)
                return pos1X <= x && (pos1Y+1 <= y && y <= pos2Y);
            else if(pos1X < pos2X)
                return !(y >= a*x+b) && (pos1Y+1 <= y && y <= pos2Y);
            else if(pos2X < pos1X)
                return !(y <= a*x+b) && (pos1Y+1 <= y && y <= pos2Y);
        }

        return false;
    }

    @Override
    public double getMargin(Margin margin) {
        return 0;
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
        return null;
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
        render.fillCustomPolygon(component.getX(), component.getY(), positions, (Color) background.getValue());
    }

}
