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
import io.github.minecraftgui.models.attributes.AttributeGroupImage;
import io.github.minecraftgui.models.components.Component;
import io.github.minecraftgui.models.images.Image;

/**
 * Created by Samuel on 2015-10-23.
 */
public class RectangleImage extends Rectangle {

    private final double borderTopPositions[][] = new double[4][2];
    private final double borderBottomPositions[][] = new double[4][2];
    private final double borderRightPositions[][] = new double[4][2];
    private final double borderLeftPositions[][] = new double[4][2];

    public RectangleImage(Component component) {
        super(component, new AttributeGroupImage(component));
    }

    @Override
    public void draw(Render render) {
        AttributeGroupImage groupImage = (AttributeGroupImage) background;

        if(groupImage.getValue() != null && groupImage.getValue().getTexture() != null) {
            borderTopPositions[0][0] = component.getX() - getPadding(Padding.LEFT) - getBorder(Border.LEFT);
            borderTopPositions[0][1] = component.getY() - getPadding(Padding.TOP) - getBorder(Border.TOP);
            borderTopPositions[1][0] = component.getX() - getPadding(Padding.LEFT);
            borderTopPositions[1][1] = component.getY() - getPadding(Padding.TOP);
            borderTopPositions[2][0] = component.getX() + getWidth() + getPadding(Padding.RIGHT);
            borderTopPositions[2][1] = component.getY() - getPadding(Padding.TOP);
            borderTopPositions[3][0] = component.getX() + getWidth() + getPadding(Padding.RIGHT) + getBorder(Border.RIGHT);
            borderTopPositions[3][1] = component.getY() - getPadding(Padding.TOP) - getBorder(Border.TOP);

            borderLeftPositions[0][0] = component.getX() - getPadding(Padding.LEFT) - getBorder(Border.LEFT);
            borderLeftPositions[0][1] = component.getY() - getPadding(Padding.TOP) - getBorder(Border.TOP);
            borderLeftPositions[1][0] = component.getX() - getPadding(Padding.LEFT) - getBorder(Border.LEFT);
            borderLeftPositions[1][1] = component.getY() + getHeight() + getPadding(Padding.BOTTOM) + getBorder(Border.BOTTOM);
            borderLeftPositions[2][0] = component.getX() - getPadding(Padding.LEFT);
            borderLeftPositions[2][1] = component.getY() + getHeight() + getPadding(Padding.BOTTOM);
            borderLeftPositions[3][0] = component.getX() - getPadding(Padding.LEFT);
            borderLeftPositions[3][1] = component.getY() - getPadding(Padding.TOP);

            borderRightPositions[0][0] = component.getX() + getWidth() + getPadding(Padding.RIGHT) + getBorder(Border.RIGHT);
            borderRightPositions[0][1] = component.getY() - getPadding(Padding.TOP) - getBorder(Border.TOP);
            borderRightPositions[1][0] = component.getX() + getWidth() + getPadding(Padding.RIGHT);
            borderRightPositions[1][1] = component.getY() - getPadding(Padding.TOP);
            borderRightPositions[2][0] = component.getX() + getWidth() + getPadding(Padding.RIGHT);
            borderRightPositions[2][1] = component.getY() + getHeight() + getPadding(Padding.BOTTOM);
            borderRightPositions[3][0] = component.getX() + getWidth() + getPadding(Padding.RIGHT) + getBorder(Border.RIGHT);
            borderRightPositions[3][1] = component.getY() + getHeight() + getPadding(Padding.BOTTOM) + getBorder(Border.BOTTOM);

            borderBottomPositions[0][0] = component.getX() - getPadding(Padding.LEFT);
            borderBottomPositions[0][1] = component.getY() + getHeight() + getPadding(Padding.BOTTOM);
            borderBottomPositions[1][0] = component.getX() - getPadding(Padding.LEFT) - getBorder(Border.LEFT);
            borderBottomPositions[1][1] = component.getY() + getHeight() + getPadding(Padding.BOTTOM) + getBorder(Border.BOTTOM);
            borderBottomPositions[2][0] = component.getX() + getWidth() + getPadding(Padding.RIGHT) + getBorder(Border.RIGHT);
            borderBottomPositions[2][1] = component.getY() + getHeight() + getPadding(Padding.BOTTOM) + getBorder(Border.BOTTOM);
            borderBottomPositions[3][0] = component.getX() + getWidth() + getPadding(Padding.RIGHT);
            borderBottomPositions[3][1] = component.getY() + getHeight() + getPadding(Padding.BOTTOM);

            render.fillCustomPolygon(0, 0, borderTopPositions, getBorderColor(Border.TOP));
            render.fillCustomPolygon(0, 0, borderLeftPositions, getBorderColor(Border.LEFT));
            render.fillCustomPolygon(0, 0, borderRightPositions, getBorderColor(Border.RIGHT));
            render.fillCustomPolygon(0, 0, borderBottomPositions, getBorderColor(Border.BOTTOM));

            render.drawTexture(((Image) background.getValue()).getTexture(), component.getX() - getPadding(Padding.LEFT), component.getY() - getPadding(Padding.TOP), getWidth() + getPadding(Padding.LEFT) + getPadding(Padding.RIGHT), getHeight() + getPadding(Padding.TOP) + getPadding(Padding.BOTTOM));
        }
    }

}
