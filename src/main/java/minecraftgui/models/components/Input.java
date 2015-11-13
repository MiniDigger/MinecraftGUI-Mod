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

import minecraftgui.controllers.KeyBoard;
import minecraftgui.controllers.Render;
import minecraftgui.models.shapes.Rectangle;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Samuel on 2015-11-07.
 */
public class Input extends Paragraph {

    private static final long textCursorVisibleTime = 1000;
    private long lastInputOrKeyPressed = System.currentTimeMillis();
    private int cursorLine = -1;
    private int cursorXIndex = -1;
    private boolean isAddInput = false;

    public Input(String id, Component parent, Class<? extends Rectangle> shape) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        super(id, parent, shape);
        lines.add("");
    }

    @Override
    public void setText(String value) {
        if(!isAddInput) {
            String text = this.text;

            super.setText(value);

            if (!text.equals(this.text))
                moveCursorAtTheEnd();
        }
        else {
            super.setText(value);
            isAddInput = false;
        }
    }

    @Override
    public void setFocus(boolean focus){
        super.setFocus(focus);

        moveCursorAtTheEnd();
    }

    @Override
    public void onKeyPressed(KeyBoard keyBoard){
        super.onKeyPressed(keyBoard);
        lastInputOrKeyPressed = System.currentTimeMillis();

        if(keyBoard.getKeyListener(Keyboard.KEY_LEFT).isPressed())
            moveCursorLeft();
        if(keyBoard.getKeyListener(Keyboard.KEY_UP).isPressed())
            moveCursorUp();
        if(keyBoard.getKeyListener(Keyboard.KEY_DOWN).isPressed())
            moveCursorDown();
        if(keyBoard.getKeyListener(Keyboard.KEY_RIGHT).isPressed())
            moveCursorRight();
    }

    public void moveCursorAtTheEnd(){
        cursorLine = lines.size()-1;
        cursorXIndex = lines.get(lines.size()-1).length();
    }

    public void moveCursorDown(){
        if(cursorLine != lines.size()-1)
            cursorLine++;
    }

    public void moveCursorUp(){
        if(cursorLine != 0)
            cursorLine--;
    }

    public void moveCursorRight(){
        String line = lines.get(cursorLine);

        if (cursorXIndex == line.length()) {
            if(cursorLine != lines.size()-1){
                cursorLine++;
                cursorXIndex = 0;
            }
        }
        else
            cursorXIndex++;
    }

    public void moveCursorLeft(){
        if (cursorXIndex == 0) {
            if(cursorLine != 0){
                cursorLine--;
                cursorXIndex = lines.get(cursorLine).length();
            }
        }
        else
            cursorXIndex--;
    }

    @Override
    public void draw(Render render) {
        super.draw(render);

        //Le fois deux c'est pour qu'il puisse etre plus grand que le temps, donc n'est plus visible
        if(keyBoard != null){
            if(lines.size() > 0){
                long time = System.currentTimeMillis();

                if(lastInputOrKeyPressed+textCursorVisibleTime >= time || time % textCursorVisibleTime*2 <= textCursorVisibleTime) {
                    double height = getFont().getStringHeight(fontSize.getValue().intValue(), fontColor.getValue());
                    double x = getX() + getFont().getStringWidth(lines.get(cursorLine).substring(0, cursorXIndex), fontSize.getValue().intValue(), fontColor.getValue());
                    double y =  getY() + cursorLine * height;

                    render.fillRectangle(x, y, .5, height, Color.BLACK);
                }
            }
        }
    }

    @Override
    public void onPaste(){
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = clipboard.getContents(null);
        boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
        String result = "";

        if (hasTransferableText) {
            try {
                result = (String)contents.getTransferData(DataFlavor.stringFlavor);
            }
            catch (UnsupportedFlavorException | IOException ex){
                ex.printStackTrace();
            }
        }

        setText(getText() + result);
    }

    @Override
    public void onInput(char input){
        super.onInput(input);
        lastInputOrKeyPressed = System.currentTimeMillis();

        switch(input){
            case 8: if(getText().length() != 0) setText(getText().substring(0, getText().length() - 1)); break;
            case 9: addInput("    "); break;
            case 13: addInput(String.valueOf(input)); break;
            default: if(input >= 32) addInput(String.valueOf(input)); break;
        }
    }

    private void addInput(String input){
        isAddInput = true;

        setText(getText()+input);

        for(int i = 0; i <= input.length();i++)
            moveCursorRight();
    }
}
