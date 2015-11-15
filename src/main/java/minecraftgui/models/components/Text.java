package minecraftgui.models.components;

import minecraftgui.models.Updatable;
import minecraftgui.models.fonts.Font;

import java.util.ArrayList;

/**
 * Created by Samuel on 2015-11-14.
 */
public class Text implements Updatable {

    private final Paragraph paragraph;
    private ArrayList<String> lines;
    private ArrayList<ArrayList<Char>> linesChar;
    private final ArrayList<GroupChar> groupChars;
    private Char cursorLocation = null;
    private boolean updateLines = false;
    private boolean textChanged = false;
    private Font fontLastUpdate = null;
    private double widthLastUpdate = 0;

    public Text(Paragraph paragraph) {
        this.paragraph = paragraph;
        lines = new ArrayList<>();
        linesChar = new ArrayList<>();
        groupChars = new ArrayList<>();
    }

    public double getCursorX(){
        if(cursorLocation != null)
            return paragraph.getX()+cursorLocation.cursorX;
        else
            return paragraph.getX();
    }

    public double getCursorY(){
        if(cursorLocation != null)
            return paragraph.getY()+cursorLocation.cursorY;
        else
            return paragraph.getY();
    }

    public void setCursorLocation(double x, double y){
        double lineHeight = paragraph.getFont().getStringHeight(paragraph.fontSize.getValue().intValue(), paragraph.fontColor.getValue());
        x -= paragraph.getX();
        y -= paragraph.getY();
        int lineIndex = (int) (y/lineHeight);

        if(lineIndex >= 0) {
            ArrayList<Char> charLine;

            if (lineIndex >= lines.size())
                charLine = linesChar.get(lines.size() - 1);
            else
                charLine = linesChar.get(lineIndex);
            String currentLine = "";

            for (Char c : charLine) {
                currentLine = currentLine+c.getValue();

                if (paragraph.getFont().getStringWidth(currentLine.trim(), paragraph.fontSize.getValue().intValue(), paragraph.fontColor.getValue()) >= x) {
                    cursorLocation = c;
                    return;
                }
            }

            cursorLocation = charLine.get(charLine.size() - 1);
        }
    }

    public void moveCursorUp(){
        if(cursorLocation != null)
            setCursorLocation(getCursorX(), getCursorY()-5);
    }

    public void moveCursorDown(){
        double lineHeight = paragraph.getFont().getStringHeight(paragraph.fontSize.getValue().intValue(), paragraph.fontColor.getValue());

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
            if(linesChar.get(0) != null && linesChar.get(0).get(0) != null)
                cursorLocation = linesChar.get(0).get(0);
    }

    public ArrayList<String> getLines() {
        return lines;
    }

    public void setText(String text){
        ArrayList<String> words = split(text);

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

        textChanged = true;
    }

    public void deleteNextChar(){
        if(cursorLocation != null) {
            if (cursorLocation.after != null) {
                Char c = cursorLocation.after;
                c.groupChar.remove(c);

                if (c.groupChar.size() == 0)
                    groupChars.remove(c);

                textChanged = true;
                updateLines = true;
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
            textChanged = true;
            updateLines = true;
        }
    }

    public void addInput(char input){
        if(cursorLocation != null){
            GroupChar currentGroup = cursorLocation.groupChar;

            if(input > 32) {
                if (currentGroup.wordGroup) {
                    Char c = new Char(input, currentGroup);

                    currentGroup.add(currentGroup.indexOf(cursorLocation) + 1, c);
                    cursorLocation = c;
                    textChanged = true;
                    updateLines = true;
                } else if (currentGroup.index + 1 != groupChars.size() && groupChars.get(currentGroup.index + 1).wordGroup) {
                    GroupChar nextGroup = groupChars.get(currentGroup.index + 1);
                    Char c = new Char(input, nextGroup);

                    nextGroup.add(0, c);
                    cursorLocation = c;
                    textChanged = true;
                    updateLines = true;
                }
                else{
                    GroupChar groupChar = new GroupChar();
                    groupChars.add(currentGroup.index+1, groupChar);
                    Char c = new Char(input, groupChar);

                    groupChar.add(c);
                    cursorLocation = c;
                    textChanged = true;
                    updateLines = true;
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

                        groupCharCharAdded.add(c);
                        cursorLocation = c;
                        textChanged = true;
                        updateLines = true;
                    }
                    else {
                        GroupChar groupChar = new GroupChar();
                        groupChar.wordGroup = false;
                        groupChars.add(currentGroup.index+1, groupChar);
                        Char c = new Char(input, groupChar);

                        groupChar.add(c);
                        cursorLocation = c;
                        textChanged = true;
                        updateLines = true;
                    }
                }
                else{
                    GroupChar groupChar = new GroupChar();
                    groupChar.wordGroup = false;
                    groupChars.add(currentGroup.index+1, groupChar);
                    Char c = new Char(input, groupChar);

                    groupChar.add(c);
                    cursorLocation = c;
                    textChanged = true;
                    updateLines = true;
                }
            }
        }
        else if(groupChars.size() != 0 && linesChar.get(0).size() != 0 && linesChar.get(0) != null && linesChar.get(0).get(0) != null){
            GroupChar groupChar = linesChar.get(0).get(0).groupChar;

            if(groupChar.wordGroup) {
                Char c = new Char(input, groupChar);

                groupChar.add(0, c);
                cursorLocation = c;
                textChanged = true;
                updateLines = true;
            }
            else{
                groupChar = new GroupChar();
                groupChar.wordGroup = input > 32;
                groupChars.add(0, groupChar);
                Char c = new Char(input, groupChar);

                groupChar.add(c);
                cursorLocation = c;
                textChanged = true;
                updateLines = true;
            }
        }
        else
            setText(String.valueOf(input));
    }

    private void updateGroupCharsWidth(){
        for(GroupChar groupChar : groupChars)
            groupChar.updateWidth();
    }

    @Override
    public void update(long updateId) {
        if(lines.size() == 0)
            lines.add("");

        if(widthLastUpdate != paragraph.getWidth() || textChanged){
            widthLastUpdate = paragraph.getWidth();

            updateLines = true;
        }

        if(fontLastUpdate != paragraph.font.getValue() || textChanged){
            fontLastUpdate = paragraph.font.getValue();
            updateGroupCharsWidth();

            updateLines = true;
        }

        for(GroupChar groupChar : groupChars)
            groupChar.update(updateId);

        if(updateLines){
            lines.clear();
            linesChar.clear();
            ArrayList<Char> chars = new ArrayList<>();

            updateLines();

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

            textChanged = false;
            updateLines = false;
        }
    }

    private void updateLines(){
        double lineHeight = paragraph.getFont().getStringHeight(paragraph.fontSize.getValue().intValue(), paragraph.fontColor.getValue());
        String currentLine = "";
        ArrayList<Char> currentCharLine = new ArrayList<>();

        for(int g = 0; g < groupChars.size(); g++) {
            GroupChar groupChar = groupChars.get(g);
            groupChar.index = g;

            if(paragraph.getWidth() < groupChar.getWidth() || groupChar.size() == 1){
                for (int i = 0; i < groupChar.size(); i++) {
                    if(groupChar.get(i).getIntValue() == 10 || groupChar.get(i).getIntValue() == 13){
                        linesChar.add(currentCharLine);
                        lines.add(currentLine);
                        currentLine = "";
                        currentCharLine = new ArrayList<>();
                        currentCharLine.add(groupChar.get(i));
                        groupChar.get(i).cursorY = lines.size()*lineHeight;
                        groupChar.get(i).cursorX = 0;
                    }
                    else if(paragraph.getFont().getStringWidth(currentLine+groupChar.get(i).getValue(), paragraph.fontSize.getValue().intValue(), paragraph.fontColor.getValue()) <= paragraph.getWidth()){
                        currentLine += groupChar.get(i).getValue();
                        currentCharLine.add(groupChar.get(i));
                        groupChar.get(i).cursorY = lines.size()*lineHeight;
                        groupChar.get(i).cursorX = paragraph.getFont().getStringWidth(currentLine, paragraph.fontSize.getValue().intValue(), paragraph.fontColor.getValue());
                    }
                    else{
                        linesChar.add(currentCharLine);
                        lines.add(currentLine);
                        currentLine = groupChar.get(i).getValue();
                        currentCharLine = new ArrayList<>();
                        currentCharLine.add(groupChar.get(i));
                        groupChar.get(i).cursorY = lines.size()*lineHeight;
                        groupChar.get(i).cursorX = paragraph.getFont().getStringWidth(currentLine, paragraph.fontSize.getValue().intValue(), paragraph.fontColor.getValue());
                    }
                }
            }
            else if(paragraph.getFont().getStringWidth(currentLine, paragraph.fontSize.getValue().intValue(), paragraph.fontColor.getValue())+groupChar.getWidth() <= paragraph.getWidth()){
                for (Char c : groupChar) {
                    currentLine += c.getValue();
                    currentCharLine.add(c);
                    c.cursorY = lines.size()*lineHeight;
                    c.cursorX = paragraph.getFont().getStringWidth(currentLine, paragraph.fontSize.getValue().intValue(), paragraph.fontColor.getValue());
                }
            }
            else{
                lines.add(currentLine);
                currentLine = "";

                for (Char c : groupChar) {
                    currentLine += c.getValue();
                    currentCharLine.add(c);
                    c.cursorY = lines.size()*lineHeight;
                    c.cursorX = paragraph.getFont().getStringWidth(currentLine, paragraph.fontSize.getValue().intValue(), paragraph.fontColor.getValue());
                }
            }
        }

        linesChar.add(currentCharLine);
        lines.add(currentLine);
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

                updateWidth();
            }
        }

        public void updateWidth(){
            line = "";

            updateLines = true;

            for(Char c : this)
                line += c.getValue();

            width = paragraph.getFont().getStringWidth(line, paragraph.fontSize.getValue().intValue(), paragraph.fontColor.getValue());
        }

    }

    private class Char {

        private GroupChar groupChar;
        private double cursorX;
        private double cursorY;
        private Char before;
        private Char after;
        private final String value;

        private Char(char value, GroupChar groupChar) {
            this.value = String.valueOf(value);
            this.groupChar = groupChar;
        }

        public Char getBefore() {
            return before;
        }

        public Char getAfter() {
            return after;
        }

        public int getIntValue() {
            return (int) value.charAt(0);
        }

        public String getValue() {
            return value;
        }
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

}
