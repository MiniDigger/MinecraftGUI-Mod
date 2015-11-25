package minecraftgui.models.components;

import minecraftgui.models.Updatable;
import minecraftgui.models.fonts.Font;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samuel on 2015-11-14.
 */
public class Text implements Updatable {

    private final ComponentText componentText;
    private final Component component;
    private ArrayList<String> lines;
    private ArrayList<ArrayList<Char>> linesChar;
    private final ArrayList<GroupChar> groupChars;
    private Char cursorLocation = null;
    private boolean updateText = false;
    private boolean textUpdated = false;
    private Font fontLastUpdate = null;
    private double widthLastUpdate = 0;
    private String text = "";

    public Text(ComponentText componentText, Component component) {
        this.componentText = componentText;
        this.component = component;
        lines = new ArrayList<>();
        linesChar = new ArrayList<>();
        groupChars = new ArrayList<>();
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
            return cursorLocation.cursorX;
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

        if(lineIndex >= 0) {
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
        if(cursorLocation != null)
            setCursorLocation(getCursorX(), getCursorY()-5);
    }

    public void moveCursorDown(){
        double lineHeight = componentText.getStringHeight();

        if(cursorLocation != null)
            setCursorLocation(getCursorX(), getCursorY()+lineHeight+5);
    }

    public void moveCursorLeft(){
        if(cursorLocation != null)
            cursorLocation = cursorLocation.before;
    }

    public void moveCursorRight(){
        if(cursorLocation != null && cursorLocation.after != null)
            cursorLocation = cursorLocation.after;
        else if(cursorLocation == null)
            if(groupChars.size() != 0 && linesChar.get(0).size() != 0 && linesChar.get(0) != null && linesChar.get(0).get(0) != null)
                cursorLocation = linesChar.get(0).get(0);
    }

    public List<String> getLines() {
        return lines;
    }

    public void setText(String text){
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
    public void update(long updateId) {
        textUpdated = false;

        if(lines.size() == 0)
            lines.add("");

        if(widthLastUpdate != component.getWidth() || updateText){
            widthLastUpdate = component.getWidth();

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

            updateLines();
            text = "";

            for(String line : lines)
                text += line;

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

            updateText = false;
            textUpdated = true;
        }
    }

    private void updateLines(){
        double lineHeight = componentText.getStringHeight();
        String currentLine = "";
        int lindeIndex = 0;
        ArrayList<Char> currentCharLine = new ArrayList<>();

        for(int g = 0; g < groupChars.size(); g++) {
            GroupChar groupChar = groupChars.get(g);
            groupChar.index = g;

            if(component.getWidth() < groupChar.getWidth() || groupChar.size() == 1){
                for (int i = 0; i < groupChar.size(); i++) {
                    if(groupChar.get(i).getIntValue() == 10 || groupChar.get(i).getIntValue() == 13){
                        linesChar.add(currentCharLine);
                        lines.add(currentLine);
                        lindeIndex++;
                        currentLine = "";
                        currentCharLine = new ArrayList<>();
                        currentCharLine.add(groupChar.get(i));
                        groupChar.get(i).cursorY = lines.size()*lineHeight;
                        groupChar.get(i).cursorX = 0;
                        groupChar.get(i).lineIndex = lindeIndex;
                    }
                    else if(componentText.getStringWidth(currentLine + groupChar.get(i).getValue()) <= component.getWidth()){
                        currentLine += groupChar.get(i).getValue();
                        currentCharLine.add(groupChar.get(i));
                        groupChar.get(i).cursorY = lines.size()*lineHeight;
                        groupChar.get(i).cursorX = componentText.getStringWidth(currentLine);
                        groupChar.get(i).lineIndex = lindeIndex;
                    }
                    else{
                        linesChar.add(currentCharLine);
                        lines.add(currentLine);
                        lindeIndex++;
                        currentLine = groupChar.get(i).getValue();
                        currentCharLine = new ArrayList<>();
                        currentCharLine.add(groupChar.get(i));
                        groupChar.get(i).cursorY = lines.size()*lineHeight;
                        groupChar.get(i).cursorX = componentText.getStringWidth(currentLine);
                        groupChar.get(i).lineIndex = lindeIndex;
                    }
                }
            }
            else if(componentText.getStringWidth(currentLine)+groupChar.getWidth() <= component.getWidth()){
                for (Char c : groupChar) {
                    currentLine += c.getValue();
                    currentCharLine.add(c);
                    c.cursorY = lines.size()*lineHeight;
                    c.cursorX = componentText.getStringWidth(currentLine);
                    c.lineIndex = lindeIndex;
                }
            }
            else{
                lines.add(currentLine);
                linesChar.add(currentCharLine);
                lindeIndex++;
                currentCharLine = new ArrayList<>();
                currentLine = "";

                for (Char c : groupChar) {
                    currentLine += c.getValue();
                    currentCharLine.add(c);
                    c.cursorY = lines.size()*lineHeight;
                    c.cursorX = componentText.getStringWidth(currentLine);
                    c.lineIndex = lindeIndex;
                }
            }
        }

        linesChar.add(currentCharLine);
        lines.add(currentLine);
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
