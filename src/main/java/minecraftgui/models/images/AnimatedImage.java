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

import java.util.ArrayList;

/**
 * Created by Samuel on 2015-10-22.
 */
public class AnimatedImage extends Image {

    private final ArrayList<AnimatedImageFrame> animatedImageFrames;
    private long lastFrameUpdate = System.currentTimeMillis();
    private int frameIndex = 0;

    public AnimatedImage(ArrayList<AnimatedImageFrame> animatedImageFrames) {
        this.animatedImageFrames = animatedImageFrames;
    }

    @Override
    public Texture getTexture() {
        long time = System.currentTimeMillis();
        AnimatedImageFrame animatedImageFrame = animatedImageFrames.get(frameIndex);

        if(animatedImageFrame.getTime()+lastFrameUpdate < time) {
            lastFrameUpdate = time;

            if (frameIndex + 1 < animatedImageFrames.size())
                frameIndex++;
            else
                frameIndex = 0;
        }

        return animatedImageFrames.get(frameIndex).getTexture();
    }

}
