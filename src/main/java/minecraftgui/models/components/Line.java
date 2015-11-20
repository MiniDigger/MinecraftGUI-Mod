package minecraftgui.models.components;

import minecraftgui.models.Updatable;
import minecraftgui.models.fonts.Font;

import java.util.ArrayList;

/**
 * Created by Samuel on 2015-11-15.
 */
public class Line implements Updatable {

    private final ComponentText componentText;
    private final Component component;
    private ArrayList<Char> lineChar;
    private Char cursorLocation = null;
    private boolean updateText = false;
    private Font fontLastUpdate = null;
    private double widthLastUpdate = 0;
    private String text = "";

    public Line(ComponentText componentText, Component component) {
        this.componentText = componentText;
        this.component = component;
        lineChar = new ArrayList<>();
    }

    @Override
    public String toString() {
        return text;
    }

    public double getCursorX(){
        if(cursorLocation != null)
            return component.getX()+cursorLocation.cursorX;
        else
            return component.getX();
    }

    public double getCursorY(){
        if(cursorLocation != null)
            return component.getY()+cursorLocation.cursorY;
        else
            return component.getY();
    }

    public void setCursorLocation(double x, double y){
        double lineHeight = componentText.getStringHeight();
        x -= component.getX();
        y -= component.getY();
        int lineIndex = (int) (y/lineHeight);

        if(lineIndex == 0) {
            String currentLine = "";

            for (Char c : lineChar) {
                currentLine = currentLine+c.getValue();

                if (componentText.getStringWidth(currentLine) >= x) {
                    cursorLocation = c;
                    return;
                }
            }

            cursorLocation = null;
        }
    }

    public void moveCursorLeft(){
        if(cursorLocation != null)
            cursorLocation = cursorLocation.before;
    }

    public void moveCursorRight(){
        if(cursorLocation != null && cursorLocation.after != null)
            cursorLocation = cursorLocation.after;
        else if(cursorLocation == null)
            if(lineChar.size() != 0 && lineChar.get(0) != null)
                cursorLocation = lineChar.get(0);
    }

    public void setText(String text){
        ArrayList<String> words = split(text);

        for(String word : words) {
            for (char c : word.toCharArray()) {
                Char ch = new Char(c);
                cursorLocation = ch;

                lineChar.add(ch);
            }
        }

        updateText = true;
    }

    public void deleteNextChar(){
        if(cursorLocation != null) {
            if (cursorLocation.after != null) {
                Char c = cursorLocation.after;
                lineChar.remove(c);

                updateText = true;
            }
        }
        else if (lineChar.size() != 0 && lineChar.get(0) != null) {
            Char c = lineChar.get(0);
            lineChar.remove(c);

            updateText = true;
        }
    }

    public void deleteChar(){
        if(cursorLocation != null){
            Char c = cursorLocation;
            lineChar.remove(c);

            cursorLocation = c.before;
            updateText = true;
        }
    }

    public void addInput(char input){
        if(input != 10 && input != 13) {
            if (cursorLocation != null) {
                Char c = new Char(input);

                lineChar.add(lineChar.indexOf(cursorLocation)+1, c);
                cursorLocation = c;
                updateText = true;
            } else {
                Char c = new Char(input);

                lineChar.add(0, c);
                cursorLocation = c;
                updateText = true;
            }
        }
    }

    @Override
    public void update(long updateId) {
        if(widthLastUpdate != component.getWidth() || updateText){
            widthLastUpdate = component.getWidth();

            updateText = true;
        }

        if(fontLastUpdate != componentText.getFont() || updateText){
            fontLastUpdate = componentText.getFont();

            updateText = true;
        }

        if(updateText){
            text = "";

            updateLines();

            for(int i = 0; i < lineChar.size(); i++){
                Char c = lineChar.get(i);

                if(i == 0)
                    c.before = null;
                else
                    c.before = lineChar.get(i-1);

                if(i+1 == lineChar.size())
                    c.after = null;
                else
                    c.after = lineChar.get(i+1);
            }

            updateText = false;
        }
    }

    private void updateLines(){
        for(int i = 0; i < lineChar.size(); i++) {
            Char c = lineChar.get(i);

            text += c.getValue();
            c.cursorX = componentText.getStringWidth(text);
            c.cursorY = 0;
        }
    }

    private ArrayList<String> split(String text){
        ArrayList<String> words = new ArrayList<String>();
        String word = "";

        for(int i = 0; i < text.length(); i++){
            if(text.charAt(i) != 13 && text.charAt(i) != 10) {
                if (text.charAt(i) == 32) {
                    if (!word.equals("")) {
                        words.add(word);
                        word = "";
                    }
                    words.add(" ");
                } else
                    word += text.charAt(i);
            }

            if(i+1 == text.length() && text.charAt(i) != 32 && text.charAt(i) != 13 && text.charAt(i) != 10)
                words.add(word);
        }

        return words;
    }

    private class Char {

        private double cursorX;
        private double cursorY;
        private Char before;
        private Char after;
        private final String value;

        private Char(char value) {
            this.value = String.valueOf(value);
        }

        public String getValue() {
            return value;
        }
    }

}
