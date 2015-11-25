package minecraftgui.models.components;

import minecraftgui.controllers.KeyBoard;
import minecraftgui.controllers.Mouse;
import minecraftgui.controllers.Render;
import minecraftgui.models.components.listeners.OnClickListener;
import minecraftgui.models.components.listeners.OnInputListener;
import minecraftgui.models.components.listeners.OnKeyPressedListener;
import minecraftgui.models.components.listeners.OnPasteListener;
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
 * Created by Samuel on 2015-11-21.
 */
public class TextArea extends Paragraph {

    private static final long textCursorVisibleTime = 1000;
    private long lastInputOrKeyPressed = System.currentTimeMillis();
    private int cursorLineBeforeUpdate = 0;

    public TextArea(String id, Component parent, Class<? extends Rectangle> shape, Class<? extends Rectangle> shapeButtonLineBefore, Class<? extends Rectangle> shapeButtonLineAfter) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        super(id, parent, shape, shapeButtonLineBefore, shapeButtonLineAfter);

        this.addOnKeyPressedListener(new OnKeyPressedListener() {
            @Override
            public void onKeyPressed(Component component, KeyBoard keyBoard) {
                lastInputOrKeyPressed = System.currentTimeMillis();

                if (keyBoard.getKeyListener(Keyboard.KEY_LEFT).isPressed()) {
                    int cursorLineBefore = text.getCursorLine();
                    text.moveCursorLeft();

                    if(cursorLineBefore != text.getCursorLine())
                        if(text.getCursorLine() < lineIndex)
                            showLineBefore();
                }
                if (keyBoard.getKeyListener(Keyboard.KEY_RIGHT).isPressed()) {
                    int cursorLineBefore = text.getCursorLine();
                    text.moveCursorRight();

                    if(cursorLineBefore != text.getCursorLine())
                        if(text.getCursorLine() > lineIndex+nbLinesToDisplay)
                            showLineAfter();
                }
                if (keyBoard.getKeyListener(Keyboard.KEY_UP).isPressed()) {
                    int cursorLineBefore = text.getCursorLine();
                    text.moveCursorUp();

                    if(cursorLineBefore != text.getCursorLine())
                        if(text.getCursorLine() < lineIndex)
                            showLineBefore();
                }
                if (keyBoard.getKeyListener(Keyboard.KEY_DOWN).isPressed()) {
                    int cursorLineBefore = text.getCursorLine();
                    text.moveCursorDown();

                    if(cursorLineBefore != text.getCursorLine())
                        if(text.getCursorLine() > lineIndex+nbLinesToDisplay)
                            showLineAfter();
                }
                if (keyBoard.getKeyListener(Keyboard.KEY_DELETE).isPressed())
                    text.deleteNextChar();
            }
        });

        this.addOnClickListener(new OnClickListener() {
            @Override
            public void onClick(Component component, Mouse mouse) {
                lastInputOrKeyPressed = System.currentTimeMillis();

                if (mouse.getX() >= getX() && mouse.getX() <= getX() + getWidth() && mouse.getY() >= getY() && mouse.getY() <= getY() + getHeight())
                    text.setCursorLocation(mouse.getX() - getX(), mouse.getY() + getStringHeight() * lineIndex - getY());
                else
                    text.setCursorLocation(mouse.getX() - getX(), getStringHeight() * lineIndex);
            }
        });

        this.addOnInputListener(new OnInputListener() {
            @Override
            public void onInput(Component component, char input) {
                lastInputOrKeyPressed = System.currentTimeMillis();

                switch (input) {
                    case 8:
                        text.deleteChar();
                        break;
                    case 9:
                        text.addInput((char) 32);
                        text.addInput((char) 32);
                        text.addInput((char) 32);
                        text.addInput((char) 32);
                        break;
                    case 13:
                        text.addInput(input);
                        break;
                    default:
                        if (input >= 32)
                            text.addInput(input);
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
    }

    @Override
    protected void beforeTextUpdate() {
        cursorLineBeforeUpdate = text.getCursorLine();
    }

    @Override
    protected void afterTextUpdate() {
        if(cursorLineBeforeUpdate != text.getCursorLine()){
            if(text.getCursorLine() < lineIndex)
                showLineBefore();
            else if(text.getCursorLine() > lineIndex+nbLinesToDisplay)
                showLineAfter();
        }
    }

    @Override
    public void draw(Render render) {
        super.draw(render);

        if(keyBoard != null){
            long time = System.currentTimeMillis();

            //Le fois deux c'est pour qu'il puisse etre plus grand que le temps, donc n'est plus visible
            if(lastInputOrKeyPressed+textCursorVisibleTime >= time || time % textCursorVisibleTime*2 <= textCursorVisibleTime) {
                double fontHeight = getStringHeight();
                render.fillRectangle(getX()+text.getCursorX(), getY()+text.getCursorY()+fontHeight * -lineIndex, .5, fontHeight, Color.WHITE);
            }
        }
    }
}
