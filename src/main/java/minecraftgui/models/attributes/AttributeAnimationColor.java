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

package minecraftgui.models.attributes;

import java.awt.*;

/**
 * Created by Samuel on 2015-10-22.
 */
public class AttributeAnimationColor extends AttributeAnimation<Color> {

    public AttributeAnimationColor(Attribute<Color> relativeTo, AttributeGroup<Color> attributeGroup) {
        super(relativeTo, attributeGroup);
        this.valueAtStartOfAnimation = new Color(0,0,0,0);
    }

    @Override
    public void update(long updateId) {
        super.update(updateId);
        Color colorAtStart = valueAtStartOfAnimation;
        Color colorRelativeTo = (Color) relativeTo.getValue();
        double percent = percentage * timePercentage;

        int r = colorAtStart.getRed() + (int) ((colorRelativeTo.getRed() - colorAtStart.getRed())*percent);
        int g = colorAtStart.getGreen() + (int) ((colorRelativeTo.getGreen() - colorAtStart.getGreen())*percent);
        int b = colorAtStart.getBlue() + (int) ((colorRelativeTo.getBlue() - colorAtStart.getBlue())*percent);
        int a = colorAtStart.getAlpha() + (int) ((colorRelativeTo.getAlpha() - colorAtStart.getAlpha())*percent);

        r = r > 255?255:r;
        r = r < 0?0:r;

        g = g > 255?255:g;
        g = g < 0?0:g;

        b = b > 255?255:b;
        b = b < 0?0:b;

        a = a > 255?255:a;
        a = a < 0?0:a;

        value = new Color(r, g, b, a);
    }
}
