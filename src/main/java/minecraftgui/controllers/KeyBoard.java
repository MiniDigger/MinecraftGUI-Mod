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

import minecraftgui.models.Updatable;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;

/**
 * Created by Samuel on 2015-09-26.
 */
public class KeyBoard implements Updatable{

    private static final long timeAfterFirstInput = 650;
    private static final long timeAfterSecondInput = 70;

    private static boolean keyPressed = false;

    private final HashMap<Integer, KeyListener> keyListeners;
    private long lastInputTime = System.currentTimeMillis();
    private int lastInput = 0;
    private char input;
    private boolean secondInput = false;

    public KeyBoard() {
        keyListeners = new HashMap<>();

        addKeyListener(Keyboard.KEY_LEFT);
        addKeyListener(Keyboard.KEY_UP);
        addKeyListener(Keyboard.KEY_DOWN);
        addKeyListener(Keyboard.KEY_RIGHT);
        addKeyListener(Keyboard.KEY_LSHIFT);
        addKeyListener(Keyboard.KEY_RSHIFT);
        addKeyListener(Keyboard.KEY_INSERT);
        addKeyListener(Keyboard.KEY_DELETE);
        addKeyListener(Keyboard.KEY_LMENU);
        addKeyListener(Keyboard.KEY_RMENU);
        addKeyListener(Keyboard.KEY_LCONTROL);
        addKeyListener(Keyboard.KEY_RCONTROL);
    }

    private void addKeyListener(int keyCode){
        keyListeners.put(keyCode, new KeyListener(keyCode));
    }

    public boolean isKeyPressed() {
        return keyPressed;
    }

    public char getInput() {
        return input;
    }

    public KeyListener getKeyListener(int keyCode){
        return keyListeners.get(keyCode);
    }

    @Override
    public void update(long updateId) {
        Keyboard.next();

        if(Keyboard.getEventKeyState()) {
            int c =  Keyboard.getEventCharacter();

            if(c != 0){
                if(c != lastInput){
                    lastInput = c;
                    lastInputTime = System.currentTimeMillis();
                    secondInput = false;

                    this.input = c >= 63000?0:(char) c;
                }
                else if(secondInput && System.currentTimeMillis() - lastInputTime >= timeAfterSecondInput){
                    lastInputTime = System.currentTimeMillis();

                    this.input = c >= 63000?0:(char) c;
                }
                else if(System.currentTimeMillis() - lastInputTime >= timeAfterFirstInput){
                    lastInputTime = System.currentTimeMillis();
                    secondInput = true;

                    this.input = c >= 63000?0:(char) c;
                }
                else
                    input =  0;
            }
            else {
                input =  0;
                lastInput = 0;
                lastInputTime = System.currentTimeMillis();
                secondInput = false;
            }
        }
        else {
            input =  0;
            lastInput = 0;
            lastInputTime = System.currentTimeMillis();
            secondInput = false;
        }

        keyPressed = false;

        for(KeyListener keyListener : keyListeners.values())
            keyListener.update(updateId);
    }

    public static class KeyListener implements Updatable{

        private final int keyCode;
        private long lastInputTime = System.currentTimeMillis();
        private boolean secondInput = false;
        private boolean pressed = false;
        private boolean down = false;
        private boolean downLastUpdate = false;

        public KeyListener(int keyCode) {
            this.keyCode = keyCode;
        }

        public boolean isPressed() {
            return pressed;
        }

        public boolean isDown() {
            return down;
        }

        @Override
        public void update(long updateId) {
            if(Keyboard.isKeyDown(keyCode)) {
                downLastUpdate = down;
                down = true;

                if(!downLastUpdate){
                    lastInputTime = System.currentTimeMillis();
                    secondInput = false;

                    pressed = true;
                }
                else if(secondInput && System.currentTimeMillis() - lastInputTime >= timeAfterSecondInput){
                    lastInputTime = System.currentTimeMillis();

                    pressed = true;
                }
                else if(System.currentTimeMillis() - lastInputTime >= timeAfterFirstInput){
                    lastInputTime = System.currentTimeMillis();
                    secondInput = true;

                    pressed = true;
                }
                else
                    pressed = false;
            }
            else {
                down = false;
                pressed = false;
                lastInputTime = System.currentTimeMillis();
                secondInput = false;
            }

            if(pressed)
                keyPressed = true;
        }
    }
}
