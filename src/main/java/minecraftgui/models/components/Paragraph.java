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

    public Paragraph(String id, Component parent, Class<? extends Rectangle> shape) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        super(id, parent, shape);
        fontSize = new AttributeGroupDouble(this);
        fontColor = new AttributeGroupColor(this);
        font = new AttributeGroupFont(this);
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

    public String getText() {
        return text;
    }

    public void setText(String value) {
        ArrayList<String> lines = updateTextLines(value);

        if(!lines.isEmpty()) {
            this.lines = lines;
            this.text = value;
        }
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

        if(widthLastUpdate != getWidth() || fontLastUpdate != font.getValue()){
            widthLastUpdate = getWidth();
            fontLastUpdate = font.getValue();

            ArrayList<String> lines = updateTextLines(text);

            if(!lines.isEmpty()) {
                this.lines = lines;
            }
        }
    }

    @Override
    public void draw(Render render) {
        super.draw(render);
        int fontHeight = getFont().getStringHeight(fontSize.getValue().intValue(), fontColor.getValue());

        for(int i = 0; i < lines.size(); i++)
            getFont().drawString(lines.get(i), (int) getY() + fontHeight * i, (int) getX(), fontSize.getValue().intValue(), fontColor.getValue());
    }

    @Override
    public void onCopy(){
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), this);
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {}

    private ArrayList<String> updateTextLines(String text) {
        ArrayList<String> lines = new ArrayList<String>();
        ArrayList<String> words = split(text);
        int lineMaxWidth = (int) getWidth();
        boolean canUpdateText = true;
        String line = "";
        int i = 0;

        while (i < words.size() && lines.size() < maxLines && canUpdateText && lineMaxWidth > 0) {
            String word = words.get(i);

            if (word.equals("") && lines.size() + 1 < maxLines) {
                lines.add(line);
                line = "";
            } else if(word.equals("") && lines.size()+1 >= maxLines){//si peut pas ajouter nouvelle ligne au text lors d'un retour de ligne
                canUpdateText = false;
            } else if (getFont().getStringWidth(line + word, fontSize.getValue().intValue(), fontColor.getValue()) <= lineMaxWidth) {
                line += word;
            } else if (getFont().getStringWidth(word, fontSize.getValue().intValue(), fontColor.getValue()) > lineMaxWidth){
                if(i+1 == words.size())
                    words.addAll(breakWord(word, getFont().getStringWidth(line, fontSize.getValue().intValue(), fontColor.getValue()), lineMaxWidth));
                else
                    words.addAll(i+1, breakWord(word, getFont().getStringWidth(line, fontSize.getValue().intValue(), fontColor.getValue()), lineMaxWidth));
            } else if (lines.size() + 1 < maxLines) {
                lines.add(line);
                line = word;
            } else {
                canUpdateText = false;
            }
            i++;
        }

        if (canUpdateText) {
            lines.add(line);
        } else {
            lines.clear();
        }

        return lines;
    }

    private ArrayList<String> breakWord(String word, int currentLineWidth, int maxLineWidth){
        ArrayList<String> words = new ArrayList<String>();
        int index = 0;

        for(int i = 0; i < word.length(); i++){
            if(getFont().getStringWidth(word.substring(index, i+1), fontSize.getValue().intValue(), fontColor.getValue()) + currentLineWidth > maxLineWidth){
                words.add(word.substring(index, i));
                currentLineWidth = 0;
                index = i;
            }
        }

        words.add(word.substring(index));

        return words;
    }

    private ArrayList<String> split(String text){
        ArrayList<String> words = new ArrayList<String>();
        String word = "";

        for(int i = 0; i < text.length(); i++){
            if(text.charAt(i) == 13) {
                if(!word.equals("")) {
                    words.add(word);
                    word = "";
                }
                words.add("");
            }
            else if(text.charAt(i) == 32) {
                if(!word.equals("")) {
                    words.add(word);
                    word = "";
                }
                words.add(" ");
            }
            else
                word += text.charAt(i);

            if(i+1 == text.length() && text.charAt(i) != 32 && text.charAt(i) != 13)
                words.add(word);
        }

        return words;
    }
}
