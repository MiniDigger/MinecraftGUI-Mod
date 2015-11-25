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

import minecraftgui.controllers.Mouse;
import minecraftgui.controllers.Render;
import minecraftgui.models.attributes.*;
import minecraftgui.models.components.listeners.OnClickListener;
import minecraftgui.models.components.listeners.OnCopyListener;
import minecraftgui.models.fonts.Font;
import minecraftgui.models.shapes.Rectangle;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Samuel on 2015-11-04.
 */
public class Paragraph extends Component implements ClipboardOwner, ComponentText {

    protected final Div buttonLineBefore;
    protected final Div buttonLineAfter;
    protected AttributeGroupDouble fontSize;
    protected AttributeGroupColor fontColor;
    protected AttributeGroupFont font;
    protected Text text;
    protected int lineIndex = 0;//La première ligne qui va être affiché
    protected int nbLinesToDisplay = Integer.MAX_VALUE;//La première ligne qui va être affiché

    protected void beforeTextUpdate(){}
    protected void afterTextUpdate(){}

    public Paragraph(String id, Component parent, Class<? extends Rectangle> shape, Class<? extends Rectangle> shapeButtonLineBefore, Class<? extends Rectangle> shapeButtonLineAfter) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        super(id, parent, shape);
        fontSize = new AttributeGroupDouble(this);
        fontColor = new AttributeGroupColor(this);
        buttonLineBefore = new Div("", this, shapeButtonLineBefore);
        buttonLineAfter = new Div("", this, shapeButtonLineAfter);
        font = new AttributeGroupFont(this);
        text = new Text(this, this);

        this.addOnCopyListener(new OnCopyListener() {
            @Override
            public void onCopy(Component component) {
                ClipboardOwner clipboardOwner = (ClipboardOwner) component;

                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text.toString()), clipboardOwner);
            }
        });

        this.buttonLineBefore.addOnClickListener(new OnClickListener() {
            @Override
            public void onClick(Component component, Mouse mouse) {
                showLineBefore();
            }
        });

        this.buttonLineAfter.addOnClickListener(new OnClickListener() {
            @Override
            public void onClick(Component component, Mouse mouse) {
                showLineAfter();
            }
        });
    }

    public Div getButtonLineBefore() {
        return buttonLineBefore;
    }

    public Div getButtonLineAfter() {
        return buttonLineAfter;
    }

    public int getNbLinesToDisplay() {
        return nbLinesToDisplay;
    }

    public void setNbLinesToDisplay(int nbLinesToDisplay) {
        this.nbLinesToDisplay = nbLinesToDisplay;
    }

    public void showLineAfter(){
        if(lineIndex +1 != text.getLines().size())
            lineIndex++;
    }

    public void showLineBefore(){
        if(lineIndex != 0)
            lineIndex--;
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

    public String getText() {
        return text.toString();
    }

    public void setText(String value) {
        lineIndex = 0;
        text.setText(value);
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
    public void update(long updateId) {
        super.update(updateId);
        font.update(updateId);
        fontColor.update(updateId);
        fontSize.update(updateId);

        beforeTextUpdate();
        text.update(updateId);
        afterTextUpdate();

        if(text.isTextUpdated()){
            while(lineIndex >= text.getLines().size() && lineIndex != 0)
                lineIndex--;
        }
    }

    @Override
    public void draw(Render render) {
        super.draw(render);
        double fontHeight = getStringHeight();
        int nbLineToRender = lineIndex+nbLinesToDisplay >= text.getLines().size()?text.getLines().size()-1: lineIndex+nbLinesToDisplay;

        for(int i = lineIndex; i <= nbLineToRender; i++)
            getFont().drawString(text.getLines().get(i), (int) ((int) getY() + fontHeight * i + fontHeight * -lineIndex), (int) getX(), fontSize.getValue().intValue(), fontColor.getValue());
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {}

}
