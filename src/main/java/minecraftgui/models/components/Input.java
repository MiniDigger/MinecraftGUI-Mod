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
import minecraftgui.controllers.Mouse;
import minecraftgui.controllers.Render;
import minecraftgui.models.attributes.*;
import minecraftgui.models.components.listeners.*;
import minecraftgui.models.fonts.Font;
import minecraftgui.models.shapes.Rectangle;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Samuel on 2015-11-07.
 */
public class Input extends Component implements ClipboardOwner, ComponentText {

    private static final long textCursorVisibleTime = 1000;
    private long lastInputOrKeyPressed = System.currentTimeMillis();
    private AttributeGroupDouble fontSize;
    private AttributeGroupColor fontColor;
    private AttributeGroupFont font;
    private Line line;

    public Input(String id, Component parent, Class<? extends Rectangle> shape) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        super(id, parent, shape);
        fontSize = new AttributeGroupDouble(this);
        fontColor = new AttributeGroupColor(this);
        font = new AttributeGroupFont(this);
        line = new Line(this, this);

        this.addOnKeyPressedListener(new OnKeyPressedListener() {
            @Override
            public void onKeyPressed(Component component, KeyBoard keyBoard) {
                lastInputOrKeyPressed = System.currentTimeMillis();

                if(keyBoard.getKeyListener(Keyboard.KEY_LEFT).isPressed())
                    line.moveCursorLeft();
                if(keyBoard.getKeyListener(Keyboard.KEY_RIGHT).isPressed())
                    line.moveCursorRight();
                if(keyBoard.getKeyListener(Keyboard.KEY_DELETE).isPressed())
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
                        break;
                    case 9:
                        line.addInput((char) 32);
                        line.addInput((char) 32);
                        line.addInput((char) 32);
                        line.addInput((char) 32);
                        break;
                    case 13:
                        line.addInput(input);
                        break;
                    default:
                        if (input >= 32)
                            line.addInput(input);
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

        getFont().drawString(line.getVisibleText(), (int) getY(), (int) getX(), fontSize.getValue().intValue(), fontColor.getValue());

        if(keyBoard != null){
            long time = System.currentTimeMillis();

            //Le fois deux c'est pour qu'il puisse etre plus grand que le temps, donc n'est plus visible
            if(lastInputOrKeyPressed+textCursorVisibleTime >= time || time % textCursorVisibleTime*2 <= textCursorVisibleTime) {
                double height = getFont().getStringHeight(fontSize.getValue().intValue(), fontColor.getValue());

                render.fillRectangle(getX()+line.getCursorX(), getY()+line.getCursorY(), .5, height, Color.WHITE);
            }
        }
    }

    public void setFont(State state, AttributeFont font){
        this.font.setAttribute(state, font);
    }

    public void setFontSize(State state, AttributeDouble size){
        fontSize.setAttribute(state, size);
    }

    public void setFontColor(State state, AttributeColor color){
        fontColor.setAttribute(state, color);
    }

    @Override
    public Font getFont(){
        return font.getValue();
    }

    @Override
    public double getStringWidth(String str) {
        return getFont().getStringWidth(str, fontSize.getValue().intValue(), fontColor.getValue());
    }

    @Override
    public double getStringHeight() {
        return getFont().getStringHeight(fontSize.getValue().intValue(), fontColor.getValue());
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {}

}
