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

package minecraftgui.models.images;

import org.newdawn.slick.opengl.Texture;

/**
 * Created by Samuel on 2015-10-22.
 */
public class StaticImage extends Image {

    private final Texture texture;

    public StaticImage(Texture texture) {
        this.texture = texture;
    }

    @Override
    public Texture getTexture() {
        return null;
    }

}
