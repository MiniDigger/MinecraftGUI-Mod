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

import minecraftgui.controllers.Mouse;
import minecraftgui.models.components.Component;

/**
 * Created by Samuel on 2015-11-06.
 */
public class AttributeGroupCursor extends AttributeGroup<Mouse.Cursor> {

    public AttributeGroupCursor(Component component) {
        super(component, Mouse.Cursor.NORMAL);
    }

    @Override
    public Mouse.Cursor getValue() {
        return (Mouse.Cursor) super.getValue();
    }
}
