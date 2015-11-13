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

package minecraftgui.models.components;


import minecraftgui.controllers.Render;
import minecraftgui.models.attributes.Attribute;
import minecraftgui.models.attributes.AttributeDouble;
import minecraftgui.models.shapes.RectangleColor;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Samuel on 2015-10-23.
 */
public class Root extends Component {

    private AttributeDouble width = new AttributeDouble();
    private AttributeDouble height = new AttributeDouble();

    public Root() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        super("", null, RectangleColor.class);
    }

    @Override
    public double getX(){
        return 0;
    }

    @Override
    public double getY(){
        return 0;
    }

    @Override
    public double getWidth(){
        return width.getValue();
    }

    @Override
    public double getHeight(){
        return height.getValue();
    }

    @Override
    public Attribute<Double> getAttributeWidth(){
        return width;
    }

    @Override
    public Attribute<Double> getAttributHeight(){
        return height;
    }

    public void setWidth(double width) {
        this.width.setValue(width);
    }

    public void setHeight(double height) {
        this.height.setValue(height);
    }

    @Override
    public void draw(Render render) {
    }

    @Override
    public void update(long updateId) {
    }

}
