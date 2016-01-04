package io.github.minecraftgui.models.components;

import io.github.minecraftgui.controllers.KeyBoard;
import io.github.minecraftgui.controllers.Mouse;
import io.github.minecraftgui.controllers.Render;
import io.github.minecraftgui.models.listeners.*;
import io.github.minecraftgui.models.shapes.Rectangle;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;

/**
 * Created by Samuel on 2015-11-21.
 */
public class TextArea extends ComponentEditableText implements ClipboardOwner {

    private static final long textCursorVisibleTime = 1000;

    private Text text;
    private final Component buttonLineBefore;
    private final Component buttonLineAfter;
    private long lastInputOrKeyPressed = System.currentTimeMillis();
    private boolean canUpdateText;

    public TextArea(String id, Class<? extends Rectangle> shape, Component buttonLineBefore, Component buttonLineAfter) {
        super(id, shape);
        this.add(buttonLineAfter);
        this.add(buttonLineBefore);
        this.add(buttonLineBefore);
        this.add(buttonLineAfter);
        this.text = new Text(this);
        this.buttonLineBefore = buttonLineBefore;
        this.buttonLineAfter = buttonLineAfter;

        this.buttonLineAfter.addOnClickListener(new OnClickListener() {
            @Override
            public void onClick(io.github.minecraftgui.models.components.Component component, Mouse mouse) {
                text.showLineAfter();
            }
        });

        this.buttonLineBefore.addOnClickListener(new OnClickListener() {
            @Override
            public void onClick(io.github.minecraftgui.models.components.Component component, Mouse mouse) {
                text.showLineBefore();
            }
        });

        this.addOnCopyListener(new OnCopyListener() {
            @Override
            public void onCopy(io.github.minecraftgui.models.components.Component component) {
                ClipboardOwner clipboardOwner = (ClipboardOwner) component;

                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text.toString()), clipboardOwner);
            }
        });

        this.addOnKeyPressedListener(new OnKeyPressedListener() {
            @Override
            public void onKeyPressed(Component component, KeyBoard keyBoard) {
                lastInputOrKeyPressed = System.currentTimeMillis();

                if (keyBoard.getKeyListener(Keyboard.KEY_LEFT).isPressed())
                    text.moveCursorLeft();
                else if (keyBoard.getKeyListener(Keyboard.KEY_RIGHT).isPressed())
                    text.moveCursorRight();
                else if (keyBoard.getKeyListener(Keyboard.KEY_UP).isPressed())
                    text.moveCursorUp();
                else if (keyBoard.getKeyListener(Keyboard.KEY_DOWN).isPressed())
                    text.moveCursorDown();
                else if (keyBoard.getKeyListener(Keyboard.KEY_DELETE).isPressed())
                    text.deleteNextChar();
            }
        });

        this.addOnClickListener(new OnClickListener() {
            @Override
            public void onClick(Component component, Mouse mouse) {
                lastInputOrKeyPressed = System.currentTimeMillis();

                if (mouse.getX() >= getX() && mouse.getX() <= getX() + getWidth() && mouse.getY() >= getY() && mouse.getY() <= getY() + getHeight())
                    text.setCursorLocation(mouse.getX() - getX(), mouse.getY() + getStringHeight() * text.getLineIndex() - getY());
                else
                    text.setCursorLocation(mouse.getX() - getX(), getStringHeight() * text.getLineIndex());
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
    public void add(Component component){
        if(buttonLineAfter != component && buttonLineBefore != component)
            super.add(component);
    }

    public void setAlignment(Text.TextAlignement alignment){
        text.setAlignement(alignment);
    }

    public Component getButtonLineBefore() {
        return buttonLineBefore;
    }

    public Component getButtonLineAfter() {
        return buttonLineAfter;
    }

    public void setNbLinesToDisplay(int nbLinesToDisplay) {
        this.text.setNbLinesToDisplay(nbLinesToDisplay);
    }

    public String getText() {
        return text.toString();
    }

    public void setText(String value) {
        text.setText(value);
        valueChanged();
    }

    @Override
    public void update(long updateId) {
        super.update(updateId);
        canUpdateText = getFont() != null && getStringHeight() != null;

        if(canUpdateText){
            text.update(updateId);

            if(text.isTextUpdated())
                valueChanged();
        }
    }

    @Override
    public void draw(Render render) {
        super.draw(render);

        if(canUpdateText) {
            text.draw(render);

            if (keyBoard != null) {
                long time = System.currentTimeMillis();

                //Le fois deux c'est pour qu'il puisse etre plus grand que le temps, donc n'est plus visible
                if (lastInputOrKeyPressed + textCursorVisibleTime >= time || time % textCursorVisibleTime * 2 <= textCursorVisibleTime) {
                    double fontHeight = getStringHeight();
                    render.fillRectangle(getX() + text.getCursorX(), getY() + text.getCursorY() + fontHeight * -text.getLineIndex(), .5, fontHeight, textCursorColor.getValue());
                }
            }
        }
    }

    @Override
    public String getValue() {
        return text.toString();
    }

    @Override
    public void setValue(String value) {
        setText(value);
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {}
}
