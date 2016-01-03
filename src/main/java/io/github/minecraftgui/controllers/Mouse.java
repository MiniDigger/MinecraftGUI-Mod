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

package io.github.minecraftgui.controllers;

import io.github.minecraftgui.models.Updatable;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.nio.IntBuffer;

/**
 * Created by Samuel on 2015-09-26.
 */
public class Mouse implements Updatable{

    //Ajouter option pour modifier
    private static final long timeDoubleClick = 225;

    public enum Button{LEFT, MIDDLE, RIGHT};
    public enum Cursor{NORMAL, HAND, TEXT};

    private final org.lwjgl.input.Cursor hand;
    private final org.lwjgl.input.Cursor text;
    private final org.lwjgl.input.Cursor normal;
    private Cursor lastCursor = Cursor.NORMAL;
    private Screen screen;
    private int mouseXLastUpdate = 0;
    private int mouseYLastUpdate = 0;
    private int mouseX = 0;
    private int mouseY = 0;
    private long timeLastClick = Long.MAX_VALUE;//Juste pour le bouton de gauche
    private boolean leftPressedLastUpdate = false;
    private boolean leftPressed = false;
    private boolean rightPressedLastUpdate = false;
    private boolean rightPressed = false;
    private boolean middlePressedLastUpdate = false;
    private boolean middlePressed = false;
    private boolean click = false;//True, si cette update a un click
    private boolean doubleClick = false;//True, si cette update a un double click

    public Mouse(Screen screen) {
        this.screen = screen;
        normal = org.lwjgl.input.Mouse.getNativeCursor();
        hand = loadCursor("handCursor.png", 12, 27);
        text = loadCursor("textCursor.png", 15, 16);
    }

    public void setCursor(Cursor cursor){
        try {
            if (lastCursor != cursor) {
                switch (cursor) {
                    case NORMAL: org.lwjgl.input.Mouse.setNativeCursor(normal); break;
                    case HAND: org.lwjgl.input.Mouse.setNativeCursor(hand); break;
                    case TEXT: org.lwjgl.input.Mouse.setNativeCursor(text); break;
                }

                lastCursor = cursor;
            }
        }catch (Exception e){}
    }

    public int getYLastUpdate(){
        return mouseYLastUpdate;
    }

    public int getXLastUpdate(){
        return mouseXLastUpdate;
    }

    public int getX() {
        return mouseX;
    }

    public int getY() {
        return mouseY;
    }

    public boolean isLeftPressedLastUpdate() {
        return leftPressedLastUpdate;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressedLastUpdate() {
        return rightPressedLastUpdate;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isMiddlePressedLastUpdate() {
        return middlePressedLastUpdate;
    }

    public boolean isMiddlePressed() {
        return middlePressed;
    }

    public boolean isGrabbed() {
        return org.lwjgl.input.Mouse.isGrabbed();
    }

    public boolean isClick(){
        return click;
    }

    public boolean isDoubleClick() {
        return doubleClick;
    }

    @Override
    public void update(long updateId) {
        long time = System.currentTimeMillis();

        click = false;
        doubleClick = false;
        leftPressedLastUpdate = leftPressed;
        rightPressedLastUpdate = rightPressed;
        middlePressedLastUpdate = middlePressed;
        leftPressed = org.lwjgl.input.Mouse.isButtonDown(0);
        rightPressed = org.lwjgl.input.Mouse.isButtonDown(1);
        middlePressed = org.lwjgl.input.Mouse.isButtonDown(2);
        mouseXLastUpdate = mouseX;
        mouseYLastUpdate = mouseY;
        mouseX = org.lwjgl.input.Mouse.getX() / screen.getScaleFactor();
        mouseY = screen.getHeight() - org.lwjgl.input.Mouse.getY() / screen.getScaleFactor();

        if(time >= timeLastClick)
            timeLastClick = Long.MAX_VALUE;

        if(isLeftPressed() && !isLeftPressedLastUpdate()){
            long timeWithDoubleClick = time+timeDoubleClick;
            click = true;

            if(timeWithDoubleClick >= timeLastClick){
                timeLastClick = Long.MAX_VALUE;
                doubleClick = true;
            }
            else
                timeLastClick = timeWithDoubleClick;
        }
    }


    private org.lwjgl.input.Cursor loadCursor(String name, int xHotSpot, int yHotSpot){
        try {
            BufferedImage bufferedImage = ImageIO.read(new FileInputStream("mods/MinecraftGUI/" + name));
            IntBuffer ib = BufferUtils.createIntBuffer(bufferedImage.getWidth() * bufferedImage.getHeight());
            int i = 0;

            for(int y = bufferedImage.getHeight()-1; 0 <= y; y--){
                for(int x = 0; x < bufferedImage.getWidth(); x++){
                    ib.put(i, bufferedImage.getRGB(x, y));
                    i++;
                }
            }

            return new org.lwjgl.input.Cursor(bufferedImage.getWidth(), bufferedImage.getHeight(), xHotSpot, yHotSpot, 1, ib, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
