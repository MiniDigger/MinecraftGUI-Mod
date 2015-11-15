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
    private int cursorDistanceWithTheEnd = 0;
    private int cursorLine = -1;
    private int cursorXIndex = -1;

    public Input(String id, Component parent, Class<? extends Rectangle> shape) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        super(id, parent, shape);
        lines.add("");
    }

    @Override
    public void onTextChange(String lastText, String newText){
        if(cursorDistanceWithTheEnd != 0 && lastText.length() < newText.length())
            moveCursorLeft();

        updateCursor();
    }

    @Override
    public void setFocus(boolean focus){
        super.setFocus(focus);

        cursorDistanceWithTheEnd = 0;
        updateCursor();
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
        if(keyBoard.getKeyListener(Keyboard.KEY_DELETE).isPressed())
            textObj.deleteNextChar();
    }

    private void moveCursorDown(){
        textObj.moveCursorDown();
    }

    private void moveCursorUp() {
        textObj.moveCursorUp();
    }

    private void moveCursorRight(){
        textObj.moveCursorRight();
    }

    private void moveCursorLeft(){
        textObj.moveCursorLeft();
    }

    private void updateCursor(){
        int i = lines.size();
        int length = 0;

        do{
            i--;
            length += lines.get(i).length();
        }while (length < cursorDistanceWithTheEnd);

        cursorLine = i;
        cursorXIndex = length - cursorDistanceWithTheEnd;
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

                    render.fillRectangle(textObj.getCursorX(), textObj.getCursorY(), .5, height, Color.BLACK);
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
        String text;

        switch(input){
            case 8:
                textObj.deleteChar();
                if(getText().length() != 0){
                    /*text = this.text.substring(0, this.text.length()-cursorDistanceWithTheEnd-1)+this.text.substring(this.text.length()-cursorDistanceWithTheEnd);

                    setText(text);*/
                }
                break;
            case 9:
                textObj.addInput((char) 32);
                textObj.addInput((char) 32);
                textObj.addInput((char) 32);
                textObj.addInput((char) 32);
                /*text = this.text.substring(0, this.text.length()-cursorDistanceWithTheEnd)+"    "+this.text.substring(this.text.length()-cursorDistanceWithTheEnd);

                setText(text);*/
                break;
            case 13:
                textObj.addInput(input);
                /*text = this.text.substring(0, this.text.length()-cursorDistanceWithTheEnd)+input+this.text.substring(this.text.length()-cursorDistanceWithTheEnd);

                setText(text);*/
                break;
            default:
                if(input >= 32) {
                    textObj.addInput(input);
                    /*text = this.text.substring(0, this.text.length()-cursorDistanceWithTheEnd)+input+this.text.substring(this.text.length()-cursorDistanceWithTheEnd);

                    setText(text);*/
                }
                break;
        }
    }
}
