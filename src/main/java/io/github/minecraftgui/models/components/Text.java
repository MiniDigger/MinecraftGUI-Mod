package io.github.minecraftgui.models.components;

import io.github.minecraftgui.controllers.Render;
import io.github.minecraftgui.models.Updatable;
import io.github.minecraftgui.models.attributes.AttributeDouble;
import io.github.minecraftgui.models.fonts.Font;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Samuel on 2015-11-14.
 */
public class Text implements Updatable, Drawable {

    public enum TextAlignement {LEFT, CENTER, RIGHT};

    private final ComponentText componentText;
    private ArrayList<Line> lines;
    private ArrayList<Line> visibleLines;
    private ArrayList<ArrayList<Char>> linesChar;
    private final ArrayList<GroupChar> groupChars;
    private Char cursorLocation = null;
    private boolean updateText = false;
    private boolean textUpdated = false;
    private Font fontLastUpdate = null;
    private double widthLastUpdate = 0;
    private String text = "";
    private int lineIndex = 0;//La première ligne qui va être affiché
    private int nbLinesToDisplay = Integer.MAX_VALUE;//La première ligne qui va être affiché
    private TextAlignement alignement = TextAlignement.LEFT;
    private final AttributeDouble textHeight;

    public Text(ComponentText componentText) {
        this.componentText = componentText;
        lines = new ArrayList<>();
        linesChar = new ArrayList<>();
        groupChars = new ArrayList<>();
        this.textHeight = new AttributeDouble(0.0);
    }

    public AttributeDouble getTextHeight() {
        return textHeight;
    }

    public void setAlignement(TextAlignement alignement) {
        this.alignement = alignement;
    }

    public int getLineIndex() {
        return lineIndex;
    }

    public void setNbLinesToDisplay(int nbLinesToDisplay) {
        if(nbLinesToDisplay != 0)
            this.nbLinesToDisplay = nbLinesToDisplay-1;
    }

    @Override
    public String toString() {
        return text;
    }

    public boolean isTextUpdated() {
        return textUpdated;
    }

    public int getCursorLine(){
        return cursorLocation != null?cursorLocation.lineIndex:0;
    }

    public double getCursorX(){
        if(cursorLocation != null)
            return lines.get(cursorLocation.lineIndex).xOffset+cursorLocation.cursorX;
        else
            return 0;
    }

    public double getCursorY(){
        if(cursorLocation != null)
            return cursorLocation.cursorY;
        else
            return 0;
    }

    public void setCursorLocation(double x, double y){
        double lineHeight = componentText.getStringHeight();
        int lineIndex = (int) (y/lineHeight);
        int firstLineIndex = lines.indexOf(visibleLines.get(0));

        if(lineIndex >= 0 && lineIndex - firstLineIndex <= nbLinesToDisplay) {
            ArrayList<Char> charLine;
            String currentLine = "";

            if (lineIndex >= lines.size())
                charLine = linesChar.get(lines.size() - 1);
            else
                charLine = linesChar.get(lineIndex);

            for (Char c : charLine) {
                if(c.getIntValue() != 13 && c.getIntValue() != 10) {
                    currentLine = currentLine + c.getValue();

                    if (componentText.getStringWidth(currentLine) >= x) {
                        cursorLocation = c;
                        return;
                    }
                }
            }

            if(charLine.size() != 0)
                cursorLocation = charLine.get(charLine.size() - 1);
            else
                cursorLocation = null;
        }
    }

    public void moveCursorUp(){
        int cursorLineBefore = getCursorLine();

        if(cursorLocation != null)
            setCursorLocation(getCursorX(), getCursorY()-5);

        if(cursorLineBefore != getCursorLine())
            if(getCursorLine() < lineIndex)
                showLineBefore();
    }

    public void moveCursorDown(){
        int cursorLineBefore = getCursorLine();

        if(cursorLocation != null)
            setCursorLocation(getCursorX(), getCursorY()+componentText.getStringHeight()+5);

        if(cursorLineBefore != getCursorLine())
            if(getCursorLine() > lineIndex+nbLinesToDisplay)
                showLineAfter();
    }

    public void showLineBefore(){
        if(lineIndex != 0) {
            lineIndex--;
            updateVisibleLines();
        }
    }

    public void showLineAfter(){
        if(lineIndex +1 != lines.size()) {
            lineIndex++;
            updateVisibleLines();
        }
    }

    public void moveCursorLeft(){
        int cursorLineBefore = getCursorLine();

        if(cursorLocation != null)
            cursorLocation = cursorLocation.before;

        if(cursorLineBefore != getCursorLine())
            if(getCursorLine() < lineIndex)
                showLineBefore();
    }

    public void moveCursorRight(){
        int cursorLineBefore = getCursorLine();

        if(cursorLocation != null && cursorLocation.after != null)
            cursorLocation = cursorLocation.after;
        else if(cursorLocation == null)
            if(groupChars.size() != 0 && linesChar.get(0).size() != 0 && linesChar.get(0) != null && linesChar.get(0).get(0) != null)
                cursorLocation = linesChar.get(0).get(0);

        if(cursorLineBefore != getCursorLine())
            if(getCursorLine() > lineIndex+nbLinesToDisplay)
                showLineAfter();
    }

    public void setText(String text){
        lineIndex = 0;
        ArrayList<String> words = split(text);
        groupChars.clear();

        for(String word : words) {
            if(word.charAt(0) > 32) {
                GroupChar groupChar = new GroupChar();

                for (char c : word.toCharArray()) {
                    Char ch = new Char(c, groupChar);

                    cursorLocation = ch;

                    groupChar.add(ch);
                }

                groupChars.add(groupChar);
            }
            else{
                GroupChar groupChar = new GroupChar();
                groupChar.wordGroup = false;

                Char ch = new Char(word.charAt(0), groupChar);

                cursorLocation = ch;

                groupChar.add(ch);

                groupChars.add(groupChar);
            }
        }

        updateText = true;
    }

    public void deleteNextChar(){
        if(cursorLocation != null) {
            if (cursorLocation.after != null) {
                Char c = cursorLocation.after;
                c.groupChar.remove(c);

                if (c.groupChar.size() == 0)
                    groupChars.remove(c);

                updateText = true;
            }
        }
        else if(groupChars.size() != 0){
            for(int i = 0; i < linesChar.size(); i++) {
                if (linesChar.get(i).size() != 0 && linesChar.get(i) != null && linesChar.get(i).get(0) != null) {
                    Char c = linesChar.get(i).get(0);

                    if (c != null) {
                        c.groupChar.remove(c);

                        if (c.groupChar.size() == 0)
                            groupChars.remove(c);

                        updateText = true;
                        return;
                    }
                }
            }
        }
    }

    public void deleteChar(){
        if(cursorLocation != null){
            Char c = cursorLocation;
            c.groupChar.remove(c);

            if(c.groupChar.size() == 0)
                groupChars.remove(c);

            cursorLocation = c.before;
            updateText = true;
        }
    }

    public void addInput(char input){
        if(cursorLocation != null){
            GroupChar currentGroup = cursorLocation.groupChar;

            if(input > 32) {
                if (currentGroup.wordGroup) {
                    Char c = new Char(input, currentGroup);
                    c.lineIndex = currentGroup.get(currentGroup.indexOf(cursorLocation)).lineIndex;

                    currentGroup.add(currentGroup.indexOf(cursorLocation) + 1, c);
                    cursorLocation = c;
                    updateText = true;
                } else if (currentGroup.index + 1 != groupChars.size() && groupChars.get(currentGroup.index + 1).wordGroup) {
                    GroupChar nextGroup = groupChars.get(currentGroup.index + 1);
                    Char c = new Char(input, nextGroup);
                    c.lineIndex = currentGroup.get(0).lineIndex;

                    nextGroup.add(0, c);
                    cursorLocation = c;
                    updateText = true;
                }
                else{
                    GroupChar groupChar = new GroupChar();
                    groupChars.add(currentGroup.index+1, groupChar);
                    Char c = new Char(input, groupChar);
                    c.lineIndex = currentGroup.get(currentGroup.size()-1).lineIndex;

                    groupChar.add(c);
                    cursorLocation = c;
                    updateText = true;
                }
            }
            else{
                if(currentGroup.wordGroup){
                    if(cursorLocation.after != null && cursorLocation.after.groupChar == currentGroup){
                        int index = currentGroup.indexOf(cursorLocation);
                        GroupChar groupChar = new GroupChar();

                        for(int i = 0; i <= index; i++){
                            groupChar.add(currentGroup.get(i));
                            currentGroup.get(i).groupChar = groupChar;
                        }

                        while(index-- != -1)
                            currentGroup.remove(0);

                        groupChars.add(currentGroup.index, groupChar);

                        GroupChar groupCharCharAdded = new GroupChar();
                        groupCharCharAdded.wordGroup = false;
                        groupChars.add(currentGroup.index+1, groupCharCharAdded);
                        Char c = new Char(input, groupCharCharAdded);
                        c.lineIndex = currentGroup.get(currentGroup.size()-1).lineIndex;

                        groupCharCharAdded.add(c);
                        cursorLocation = c;
                        updateText = true;
                    }
                    else {
                        GroupChar groupChar = new GroupChar();
                        groupChar.wordGroup = false;
                        groupChars.add(currentGroup.index+1, groupChar);
                        Char c = new Char(input, groupChar);
                        c.lineIndex = currentGroup.get(currentGroup.size()-1).lineIndex;

                        groupChar.add(c);
                        cursorLocation = c;
                        updateText = true;
                    }
                }
                else{
                    GroupChar groupChar = new GroupChar();
                    groupChar.wordGroup = false;
                    groupChars.add(currentGroup.index+1, groupChar);
                    Char c = new Char(input, groupChar);
                    c.lineIndex = currentGroup.get(currentGroup.size()-1).lineIndex;

                    groupChar.add(c);
                    cursorLocation = c;
                    updateText = true;
                }
            }
        }
        else if(groupChars.size() != 0 && linesChar.get(0).size() != 0 && linesChar.get(0) != null && linesChar.get(0).get(0) != null){
            GroupChar groupChar = linesChar.get(0).get(0).groupChar;

            if(groupChar.wordGroup) {
                Char c = new Char(input, groupChar);

                groupChar.add(0, c);
                cursorLocation = c;
                updateText = true;
            }
            else{
                groupChar = new GroupChar();
                groupChar.wordGroup = input > 32;
                groupChars.add(0, groupChar);
                Char c = new Char(input, groupChar);

                groupChar.add(c);
                cursorLocation = c;
                updateText = true;
            }
        }
        else
            setText(String.valueOf(input));
    }

    private void updateGroupCharsWidth(){
        for(GroupChar groupChar : groupChars)
            groupChar.update();
    }

    @Override
    public void draw(Render render) {
        double fontHeight = componentText.getStringHeight();
        ArrayList<Line> lines = visibleLines;
        Font font = componentText.getFont();
        int fontSize = componentText.getFontSize();
        Color fontColor = componentText.getFontColor();

        for(int i = 0; i < lines.size(); i++) {
            Line line = lines.get(i);
            font.drawString(line.text, componentText.getY() + fontHeight * i, componentText.getX() +line.xOffset, fontSize, fontColor);
        }
    }

    @Override
    public void update(long updateId) {
        textUpdated = false;

        if(lines.size() == 0)
            lines.add(new Line());

        if(widthLastUpdate != componentText.getWidth() || updateText){
            widthLastUpdate = componentText.getWidth();

            updateText = true;
        }

        if(fontLastUpdate != componentText.getFont() || updateText){
            fontLastUpdate = componentText.getFont();
            updateGroupCharsWidth();

            updateText = true;
        }

        for(GroupChar groupChar : groupChars)
            groupChar.update(updateId);

        if(updateText){
            lines.clear();
            linesChar.clear();
            ArrayList<Char> chars = new ArrayList<>();
            int cursorLineBeforeUpdate = getCursorLine();

            updateLines();
            text = "";

            for(Line line : lines)
                text += line.text;

            for(ArrayList<Char> charsLine : linesChar)
                chars.addAll(charsLine);

            for(int i = 0; i < chars.size(); i++){
                Char c = chars.get(i);

                if(i == 0)
                    c.before = null;
                else
                    c.before = chars.get(i-1);

                if(i+1 == chars.size())
                    c.after = null;
                else
                    c.after = chars.get(i+1);
            }

            if(cursorLineBeforeUpdate != getCursorLine()){
                if(getCursorLine() < lineIndex)
                    showLineBefore();
                else if(getCursorLine() > lineIndex+nbLinesToDisplay)
                    showLineAfter();
            }

            while(lineIndex >= lines.size() && lineIndex != 0)
                lineIndex--;

            updateVisibleLines();

            textHeight.setValue(visibleLines.size()*componentText.getStringHeight());

            updateText = false;
            textUpdated = true;
        }
    }

    private void updateVisibleLines(){
        visibleLines = new ArrayList<>();
        int nbLineToRender = lineIndex+nbLinesToDisplay >= lines.size()?lines.size()-1: lineIndex+nbLinesToDisplay;

        for(int i = lineIndex; i <= nbLineToRender; i++)
            visibleLines.add(lines.get(i));
    }

    private double getXOffset(double textWidth){
        switch (alignement){
            case LEFT: return 0;
            case CENTER: return (componentText.getWidth() - textWidth)/2;
            case RIGHT: return componentText.getWidth() - textWidth;
        }

        return 0;
    }

    private void updateLines(){
        double lineHeight = componentText.getStringHeight();
        String currentLine = "";
        int lineIndex = 0;
        ArrayList<Char> currentCharLine = new ArrayList<>();

        for(int g = 0; g < groupChars.size(); g++) {
            GroupChar groupChar = groupChars.get(g);
            groupChar.index = g;

            if(componentText.getWidth() < groupChar.getWidth() || groupChar.size() == 1){
                for (int i = 0; i < groupChar.size(); i++) {
                    if(groupChar.get(i).getIntValue() == 10 || groupChar.get(i).getIntValue() == 13){
                        linesChar.add(currentCharLine);
                        addLine(currentLine);
                        lineIndex++;
                        currentLine = "";
                        currentCharLine = new ArrayList<>();
                        currentCharLine.add(groupChar.get(i));
                        groupChar.get(i).cursorY = lines.size()*lineHeight;
                        groupChar.get(i).cursorX = 0;
                        groupChar.get(i).lineIndex = lineIndex;
                    }
                    else if(componentText.getStringWidth(currentLine + groupChar.get(i).getValue()) <= componentText.getWidth()){
                        currentLine += groupChar.get(i).getValue();
                        currentCharLine.add(groupChar.get(i));
                        groupChar.get(i).cursorY = lines.size()*lineHeight;
                        groupChar.get(i).cursorX = componentText.getStringWidth(currentLine);
                        groupChar.get(i).lineIndex = lineIndex;
                    }
                    else{
                        linesChar.add(currentCharLine);
                        addLine(currentLine);
                        lineIndex++;
                        currentLine = groupChar.get(i).getValue();
                        currentCharLine = new ArrayList<>();
                        currentCharLine.add(groupChar.get(i));
                        groupChar.get(i).cursorY = lines.size()*lineHeight;
                        groupChar.get(i).cursorX = componentText.getStringWidth(currentLine);
                        groupChar.get(i).lineIndex = lineIndex;
                    }
                }
            }
            else if(componentText.getStringWidth(currentLine)+groupChar.getWidth() <= componentText.getWidth()){
                for (Char c : groupChar) {
                    currentLine += c.getValue();
                    currentCharLine.add(c);
                    c.cursorY = lines.size()*lineHeight;
                    c.cursorX = componentText.getStringWidth(currentLine);
                    c.lineIndex = lineIndex;
                }
            }
            else{
                addLine(currentLine);
                linesChar.add(currentCharLine);
                lineIndex++;
                currentCharLine = new ArrayList<>();
                currentLine = "";

                for (Char c : groupChar) {
                    currentLine += c.getValue();
                    currentCharLine.add(c);
                    c.cursorY = lines.size()*lineHeight;
                    c.cursorX = componentText.getStringWidth(currentLine);
                    c.lineIndex = lineIndex;
                }
            }
        }

        linesChar.add(currentCharLine);
        addLine(currentLine);
    }

    private void addLine(String lineStr){
        Line line = new Line();
        line.text = lineStr;
        line.xOffset = getXOffset(componentText.getStringWidth(lineStr));
        lines.add(line);
    }

    private ArrayList<String> split(String text){
        ArrayList<String> words = new ArrayList<String>();
        String word = "";

        for(int i = 0; i < text.length(); i++){
            if(text.charAt(i) == 13 || text.charAt(i) == 10) {
                if(!word.equals("")) {
                    words.add(word);
                    word = "";
                }
                words.add((char) 10+"");
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

    private class GroupChar extends ArrayList<Char> implements Updatable{

        private boolean wordGroup = true;
        private int index;
        private int sizeLastWidth = 0;
        private double width = 0;
        private String line;

        private double getWidth(){
            return width;
        }

        @Override
        public void update(long updateId) {
            if(size() != sizeLastWidth){
                sizeLastWidth = size();

                update();
            }
        }

        public void update(){
            line = "";

            for(Char c : this)
                line += c.getValue();

            width = componentText.getStringWidth(line);
        }

    }

    private class Line{
        private String text = "";
        private double xOffset = 0;
    }

    private class Char {

        private GroupChar groupChar;
        private double cursorX;
        private double cursorY;
        private int lineIndex;
        private Char before;
        private Char after;
        private final String value;

        private Char(char value, GroupChar groupChar) {
            this.value = String.valueOf(value);
            this.groupChar = groupChar;
        }

        public int getIntValue() {
            return (int) value.charAt(0);
        }

        public String getValue() {
            return value;
        }
    }

}
