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

import io.github.minecraftgui.controllers.Mouse;
import io.github.minecraftgui.controllers.Render;
import io.github.minecraftgui.models.listeners.OnClickListener;
import io.github.minecraftgui.models.listeners.OnCopyListener;
import io.github.minecraftgui.models.shapes.Rectangle;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

/**
 * Created by Samuel on 2015-11-04.
 */
public class Paragraph extends ComponentText implements ClipboardOwner  {

    protected final Component buttonLineBefore;
    protected final Component buttonLineAfter;
    protected Text text;
    private boolean canUpdateText;

    public Paragraph(String id, Class<? extends Rectangle> shape, Component buttonLineBefore, Component buttonLineAfter) {
        super(id, shape);
        text = new Text(this);
        this.add(buttonLineAfter);
        this.add(buttonLineBefore);
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

    public void setAlignment(Text.TextAlignement alignment){
        text.setAlignement(alignment);
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

        if(canUpdateText)
            text.draw(render);
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {}

    @Override
    public String getValue() {
        return text.toString();
    }

    @Override
    public void setValue(String value) {
        setText(value);
    }
}
