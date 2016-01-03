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

package io.github.minecraftgui.models.attributes;

import java.awt.*;

/**
 * Created by Samuel on 2015-10-22.
 */
public class AttributeVariableColor extends AttributeVariable<Color> {

    public AttributeVariableColor(Attribute<Color> value, AttributeGroup<Color> attributeGroup) {
        super(value, attributeGroup);
    }

    public AttributeVariableColor(Attribute<Color> value, AttributeGroup<Color> attributeGroup, double percentage) {
        super(value, attributeGroup, percentage);
    }

    public AttributeVariableColor(Attribute<Color> value, AttributeGroup<Color> attributeGroup, long time) {
        super(value, attributeGroup, time);
    }

    public AttributeVariableColor(Attribute<Color> value, AttributeGroup<Color> attributeGroup, double percentage, long time) {
        super(value, attributeGroup, percentage, time);
    }

    @Override
    protected Color getValue(Color value, double percentage) {
        int r = (int) (value.getRed() * percentage);
        int g = (int) (value.getGreen() * percentage);
        int b = (int) (value.getBlue() * percentage);
        int a = (int) (value.getAlpha() * percentage);

        r = r > 255?255:r;
        r = r < 0?0:r;

        g = g > 255?255:g;
        g = g < 0?0:g;

        b = b > 255?255:b;
        b = b < 0?0:b;

        a = a > 255?255:a;
        a = a < 0?0:a;

        return new Color(r, g, b, a);
    }

    @Override
    protected Color getValue(Color valueAtTheEnd, Color valueAtStart, double percentage, double timePercentage) {
        double percent = percentage * timePercentage;

        int r = valueAtStart.getRed() + (int) ((valueAtTheEnd.getRed() - valueAtStart.getRed())*percent);
        int g = valueAtStart.getGreen() + (int) ((valueAtTheEnd.getGreen() - valueAtStart.getGreen())*percent);
        int b = valueAtStart.getBlue() + (int) ((valueAtTheEnd.getBlue() - valueAtStart.getBlue())*percent);
        int a = valueAtStart.getAlpha() + (int) ((valueAtTheEnd.getAlpha() - valueAtStart.getAlpha())*percent);

        r = r > 255?255:r;
        r = r < 0?0:r;

        g = g > 255?255:g;
        g = g < 0?0:g;

        b = b > 255?255:b;
        b = b < 0?0:b;

        a = a > 255?255:a;
        a = a < 0?0:a;

        return new Color(r, g, b, a);
    }
}
