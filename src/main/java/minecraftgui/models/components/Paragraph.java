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

import minecraftgui.controllers.Render;
import minecraftgui.models.attributes.*;
import minecraftgui.models.fonts.Font;
import minecraftgui.models.shapes.Rectangle;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Created by Samuel on 2015-11-04.
 */
public class Paragraph extends Component implements ClipboardOwner {

    protected AttributeGroupDouble fontSize;
    protected AttributeGroupColor fontColor;
    protected AttributeGroupFont font;
    protected String text = "";
    protected double maxLines = Double.MAX_VALUE;
    protected ArrayList<String> lines = new ArrayList<>();
    private Font fontLastUpdate = null;
    private double widthLastUpdate = 0;
    protected Text textObj;

    public void onTextChange(String lastText, String newText){}

    public Paragraph(String id, Component parent, Class<? extends Rectangle> shape) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        super(id, parent, shape);
        fontSize = new AttributeGroupDouble(this);
        fontColor = new AttributeGroupColor(this);
        font = new AttributeGroupFont(this);
        textObj = new Text(this);
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

    public void setMaxLines(double maxLines) {
        this.maxLines = maxLines;
    }

    public double getMaxLines() {
        return maxLines;
    }

    public String getText() {
        return text;
    }

    public void setText(String value) {
        textObj.setText(value);
        /*ArrayList<String> lines = updateTextLines(value);

        if(!lines.isEmpty()) {
            String lastText = text;
            this.lines = lines;
            this.text = value;
            onTextChange(lastText, value);
        }*/
    }

    public Font getFont(){
        return font.getValue();
    }

    @Override
    public void update(long updateId) {
        super.update(updateId);
        font.update(updateId);
        fontColor.update(updateId);
        fontSize.update(updateId);

        textObj.update(updateId);

        /*if(widthLastUpdate != getWidth() || fontLastUpdate != font.getValue()){
            widthLastUpdate = getWidth();
            fontLastUpdate = font.getValue();

            ArrayList<String> lines = updateTextLines(text);

            if(!lines.isEmpty()) {
                this.lines = lines;
            }
        }*/
    }

    @Override
    public void draw(Render render) {
        super.draw(render);
        double fontHeight = getFont().getStringHeight(fontSize.getValue().intValue(), fontColor.getValue());

        /*for(int i = 0; i < lines.size(); i++)
            getFont().drawString(lines.get(i), (int) getY() + fontHeight * i, (int) getX(), fontSize.getValue().intValue(), fontColor.getValue());*/

        for(int i = 0; i < textObj.getLines().size(); i++)
            getFont().drawString(textObj.getLines().get(i), (int) ((int) getY() + fontHeight * i), (int) getX(), fontSize.getValue().intValue(), fontColor.getValue());
    }

    @Override
    public void onCopy(){
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), this);
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {}

}
