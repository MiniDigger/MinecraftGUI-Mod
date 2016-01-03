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

package io.github.minecraftgui.models.fonts;

import net.minecraft.client.Minecraft;

import java.awt.*;

/**
 * Created by Samuel on 2015-11-05.
 */
public class MinecraftNormalFont extends Font {

    @Override
    public double getStringWidth(String text, int size, Color color) {
        return Minecraft.getMinecraft().fontRendererObj.getStringWidth(text);
    }

    @Override
    public double getStringHeight(int size, Color color) {
        return Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;
    }

    @Override
    public void drawString(String text, double y, double x, int size, Color color) {
        Minecraft.getMinecraft().fontRendererObj.drawString(text, (int)x, (int)y, color.getRGB());
    }
}
