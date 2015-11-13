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

package minecraftgui.controllers;

import minecraftgui.models.fonts.Font;

import java.util.HashMap;

/**
 * Created by Samuel on 2015-11-04.
 */
public class FontRepository {

    private final HashMap<String, Font> fonts = new HashMap<String, Font>();

    public FontRepository() {
    }

    public void addFont(String name, Font font){
        fonts.put(name.toLowerCase(), font);
    }

    public Font getFont(String name){
        return fonts.get(name.toLowerCase());
    }
}
