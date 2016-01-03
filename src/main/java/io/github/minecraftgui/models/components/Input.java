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

import io.github.minecraftgui.controllers.KeyBoard;
import io.github.minecraftgui.controllers.Mouse;
import io.github.minecraftgui.controllers.Render;
import io.github.minecraftgui.models.attributes.*;
import io.github.minecraftgui.models.listeners.*;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;

/**
 * Created by Samuel on 2015-11-07.
 */
public class Input extends ComponentEditableText implements ClipboardOwner {

    private static final long textCursorVisibleTime = 1000;
    private long lastInputOrKeyPressed = System.currentTimeMillis();
    private AttributeGroupDouble fontSize;
    private AttributeGroupColor fontColor;
    private AttributeGroupFont font;
    private Line line;

    public Input(String id, Class<? extends io.github.minecraftgui.models.shapes.Rectangle> shape) {
        super(id, shape);
        fontSize = new AttributeGroupDouble(this);
        fontColor = new AttributeGroupColor(this);
        font = new AttributeGroupFont(this);
        line = new Line(this);

        this.addOnKeyPressedListener(new OnKeyPressedListener() {
            @Override
            public void onKeyPressed(Component component, KeyBoard keyBoard) {
                lastInputOrKeyPressed = System.currentTimeMillis();

                if(keyBoard.getKeyListener(Keyboard.KEY_LEFT).isPressed())
                    line.moveCursorLeft();
                else if(keyBoard.getKeyListener(Keyboard.KEY_RIGHT).isPressed())
                    line.moveCursorRight();
                else if(keyBoard.getKeyListener(Keyboard.KEY_DELETE).isPressed())
                    line.deleteNextChar();
            }
        });

        this.addOnClickListener(new OnClickListener() {
            @Override
            public void onClick(Component component, Mouse mouse) {
                lastInputOrKeyPressed = System.currentTimeMillis();

                if (mouse.getX() >= getX() && mouse.getX() <= getX() + getWidth() && mouse.getY() >= getY() && mouse.getY() <= getY() + getHeight())
                    line.setCursorLocation(mouse.getX() - getX(), 1);
                else
                    line.setCursorLocation(mouse.getX() - getX(), 1);
            }
        });

        this.addOnInputListener(new OnInputListener() {
            @Override
            public void onInput(Component component, char input) {
                lastInputOrKeyPressed = System.currentTimeMillis();

                switch (input) {
                    case 8:
                        line.deleteChar();
                        valueChanged();
                        break;
                    case 9:
                        line.addInput((char) 32);
                        line.addInput((char) 32);
                        line.addInput((char) 32);
                        line.addInput((char) 32);
                        valueChanged();
                        break;
                    case 13:
                        line.addInput(input);
                        valueChanged();
                        break;
                    default:
                        if (input >= 32) {
                            line.addInput(input);
                            valueChanged();
                        }
                        break;
                }
            }
        });

        this.addOnPasteListener(new OnPasteListener() {
            @Override
            public void onPaste(Component component) {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                Transferable contents = clipboard.getContents(null);
                boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
                String result = "";

                if (hasTransferableText) {
                    try {
                        result = (String) contents.getTransferData(DataFlavor.stringFlavor);
                    } catch (UnsupportedFlavorException | IOException ex) {
                        ex.printStackTrace();
                    }
                }

                setText(getText() + result);
            }
        });

        this.addOnCopyListener(new OnCopyListener() {
            @Override
            public void onCopy(Component component) {
                ClipboardOwner clipboardOwner = (ClipboardOwner) component;

                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(line.toString()), clipboardOwner);
            }
        });
    }

    public String getText() {
        return line.toString();
    }

    public void setText(String value) {
        line.setText(value);
        valueChanged();
    }

    @Override
    public void update(long updateId) {
        super.update(updateId);
        font.update(updateId);
        fontColor.update(updateId);
        fontSize.update(updateId);

        line.update(updateId);
    }

    @Override
    public void draw(Render render) {
        super.draw(render);

        this.line.draw(render);

        if(keyBoard != null){
            long time = System.currentTimeMillis();

            //Le fois deux c'est pour qu'il puisse etre plus grand que le temps, donc n'est plus visible
            if(lastInputOrKeyPressed+textCursorVisibleTime >= time || time % textCursorVisibleTime*2 <= textCursorVisibleTime) {
                double height = getFont().getStringHeight(fontSize.getValue().intValue(), fontColor.getValue());

                render.fillRectangle(getX()+line.getCursorX(), getY()+line.getCursorY(), .5, height, Color.WHITE);
            }
        }
    }

    public void setFont(State state, io.github.minecraftgui.models.fonts.Font font){
        this.font.getAttribute(state).setValue(font);
    }

    public void setFontSize(State state, int size){
        fontSize.getAttribute(state).setAttribute(new AttributeDouble((double) size));
    }

    public void setFontColor(State state, Color color){
        fontColor.getAttribute(state).setAttribute(new AttributeColor(color));
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {}

    @Override
    public String getValue() {
        return line.toString();
    }

    @Override
    public void setValue(String value) {
        setText(value);
    }
}
