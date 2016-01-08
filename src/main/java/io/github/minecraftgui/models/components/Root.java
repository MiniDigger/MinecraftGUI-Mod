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

package io.github.minecraftgui.models.components;


import io.github.minecraftgui.controllers.Render;
import io.github.minecraftgui.models.attributes.AttributeDouble;
import io.github.minecraftgui.models.network.NetworkInterface;
import io.github.minecraftgui.models.shapes.RectangleColor;

/**
 * Created by Samuel on 2015-10-23.
 */
public class Root extends Component {

    AttributeDouble width = new AttributeDouble(0.0);
    AttributeDouble heigth = new AttributeDouble(0.0);

    public Root() {
        super(NetworkInterface.ROOT_ID.toString(), RectangleColor.class);
        this.shape.getWidth(State.NORMAL).setAttribute(width);
        this.shape.getHeight(State.NORMAL).setAttribute(heigth);
    }

    @Override
    public void setState(State state) {
    }

    @Override
    public void setFocus(boolean isFocus) {
    }

    @Override
    public double getX(){
        return 0;
    }

    @Override
    public double getY(){
        return 0;
    }

    public void setWidth(double width) {
        this.width.setValue(width);
    }

    public void setHeight(double height) {
        this.heigth.setValue(height);
    }

    @Override
    public void draw(Render render) {
    }

    @Override
    public void update(long updateId) {
        this.shape.getAttributeWidth().update(updateId);
        this.shape.getAttributeHeight().update(updateId);
    }

}
