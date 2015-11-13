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

import minecraftgui.views.ModInterface;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import java.awt.*;

public class Render {

    private final ModInterface modInterface;

    public Render(ModInterface modInterface) {
        this.modInterface = modInterface;
    }

    public void drawOval(int x, int y, int width, int height, Color color){
        if(color.getAlpha() != 0) {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f((color.getRed() / 255.0f), (color.getGreen() / 255.0f), (color.getBlue() / 255.0f), (color.getAlpha() / 255.0f));

            GL11.glBegin(GL11.GL_POINTS);
            {
                for(double angle = 0; angle <= 90; angle += .5){
                    double xLoc = x + width * Math.cos(angle * (Math.PI / 180));
                    double yLoc = y + height * Math.sin(angle * (Math.PI / 180));
                    double distanceY = yLoc-y;
                    double distanceX = xLoc-x;
                    GL11.glVertex2d(xLoc - distanceX*2, yLoc-(distanceY)*2);

                    GL11.glVertex2d(xLoc - distanceX*2, yLoc-(distanceY)*2 + distanceY*2);

                    GL11.glVertex2d(xLoc + 1, yLoc-(distanceY)*2 + distanceY*2);

                    GL11.glVertex2d(xLoc + 1, yLoc-(distanceY)*2);
                }
            }
            GL11.glEnd();

            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
    }

    public void fillOval(double x0, double y0, double width, double height, Color color){
        if(color.getAlpha() != 0) {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f((color.getRed() / 255.0f), (color.getGreen() / 255.0f), (color.getBlue() / 255.0f), (color.getAlpha() / 255.0f));

            GL11.glBegin(GL11.GL_POLYGON);
            {
                for(int i = 360; 0 <= i; i-=2)
                    GL11.glVertex2d(x0 + width * Math.cos(i * (Math.PI / 180)), y0 + height * Math.sin(i * (Math.PI / 180)));
            }
            GL11.glEnd();

            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
    }

    public void drawCustomPolygon(int[] x, int[] y, Color color){
        if(color.getAlpha() != 0 && x.length == y.length) {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f((color.getRed() / 255.0f), (color.getGreen() / 255.0f), (color.getBlue() / 255.0f), (color.getAlpha() / 255.0f));

            GL11.glBegin(GL11.GL_LINES);
            {
                for(int i = 0; i < x.length; i++){
                    if(i < x.length-1){
                        GL11.glVertex2f(x[i], y[i]);
                        GL11.glVertex2f(x[i+1], y[i+1]);
                    }
                    else{
                        GL11.glVertex2f(x[i], y[i]);
                        GL11.glVertex2f(x[0], y[0]);
                    }
                }
            }
            GL11.glEnd();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
    }

    public void fillCustomPolygon(double x, double y, double[][] positions, Color color){
        if(color.getAlpha() != 0) {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f((color.getRed() / 255.0f), (color.getGreen() / 255.0f), (color.getBlue() / 255.0f), (color.getAlpha() / 255.0f));

            GL11.glBegin(GL11.GL_POLYGON);
            {
                for(int i = 0; i < positions.length; i++)
                    GL11.glVertex2d(x + positions[i][0], y + positions[i][1]);
            }
            GL11.glEnd();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
    }

    public void drawLine(int x0, int y0, int x1, int y1, Color color){
        if(color.getAlpha() != 0) {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f((color.getRed() / 255.0f), (color.getGreen() / 255.0f), (color.getBlue() / 255.0f), (color.getAlpha() / 255.0f));

            GL11.glBegin(GL11.GL_LINES);
            {
                GL11.glVertex2f(x0, y0);

                GL11.glVertex2f(x1, y1);
            }
            GL11.glEnd();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
    }

    public void fillRectangle(double x, double y, double width, double height, Color color){
        if(color.getAlpha() != 0) {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f((color.getRed() / 255.0f), (color.getGreen() / 255.0f), (color.getBlue() / 255.0f), (color.getAlpha() / 255.0f));

            GL11.glBegin(GL11.GL_QUADS);
            {
                GL11.glVertex2d(x, y);

                GL11.glVertex2d(x, y + height);

                GL11.glVertex2d(x + width, y + height);

                GL11.glVertex2d(x + width, y);
            }
            GL11.glEnd();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
    }

    public void drawRectangle(double x, double y, double width, double height, Color color){
        if(color.getAlpha() != 0) {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            GL11.glColor4f((color.getRed() / 255.0f), (color.getGreen() / 255.0f), (color.getBlue() / 255.0f), (color.getAlpha() / 255.0f));

            GL11.glBegin(GL11.GL_LINES);
            {
                GL11.glVertex2d(x, y);
                GL11.glVertex2d(x, y + height);

                GL11.glVertex2d(x, y + height);
                GL11.glVertex2d(x + width, y + height);

                GL11.glVertex2d(x + width, y + height);
                GL11.glVertex2d(x + width, y);

                GL11.glVertex2d(x + width, y);
                GL11.glVertex2d(x, y);
            }
            GL11.glEnd();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
    }

    public void drawTexture(Texture texture, double x, double y, double width, double height){
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glColor4f(1, 1, 1, 1);

        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glTexCoord2f(0, 0);
            GL11.glVertex2d(x, y);

            GL11.glTexCoord2f(0, texture.getHeight());
            GL11.glVertex2d(x, y + height);

            GL11.glTexCoord2f(texture.getWidth(), texture.getHeight());
            GL11.glVertex2d(x+width, y+height);

            GL11.glTexCoord2f(texture.getWidth(), 0);
            GL11.glVertex2d(x + width, y);
        }
        GL11.glEnd();

        modInterface.bindMinecraftTextures();
    }

}
