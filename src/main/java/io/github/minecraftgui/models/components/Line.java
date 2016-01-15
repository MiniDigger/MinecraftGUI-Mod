package io.github.minecraftgui.models.components;

import io.github.minecraftgui.controllers.Render;
import io.github.minecraftgui.models.Updatable;
import io.github.minecraftgui.models.fonts.Font;

import java.util.ArrayList;

/**
 * Created by Samuel on 2015-11-15.
 */
public class Line implements Updatable, Drawable {

    private final ComponentText componentText;
    private ArrayList<Char> lineChar;
    private Char cursorLocation = null;
    private boolean updateText = false;
    private Font fontLastUpdate = null;
    private double widthLastUpdate = 0;
    private double xRelative = 0;
    private String text = "";
    private String lastTextVersion = "";
    private String visibleText = "";
    private Char firstCharVisible = null;
    private Char lastCharVisible = null;
    private boolean isTextUpdated = false;

    public Line(ComponentText componentText) {
        this.componentText = componentText;
        lineChar = new ArrayList<>();
    }

    public boolean isTextUpdated() {
        return isTextUpdated;
    }

    public String getVisibleText(){
        return visibleText;
    }

    @Override
    public String toString() {
        return text;
    }

    public double getCursorX(){
        if(cursorLocation != null)
            return cursorLocation.cursorX - xRelative;
        else
            return 0;
    }

    public double getCursorY(){
        return 0;
    }

    public void setCursorLocation(double x, double y){
        double lineHeight = componentText.getStringHeight();
        int lineIndex = (int) (y/lineHeight);

        if(lineIndex == 0) {
            String currentLine = "";

            for (Char c : lineChar) {
                currentLine = currentLine+c.getValue();

                if (componentText.getStringWidth(currentLine) >= x+xRelative) {
                    cursorLocation = c;
                    return;
                }
            }

            cursorLocation = null;
        }
    }

    public void moveCursorLeft(){
        if(cursorLocation != null) {
            cursorLocation = cursorLocation.before;
            updateVisibleText();
        }
    }

    public void moveCursorRight(){
        if(cursorLocation != null && cursorLocation.after != null) {
            cursorLocation = cursorLocation.after;
            updateVisibleText();
        }
        else if(cursorLocation == null) {
            if (lineChar.size() != 0 && lineChar.get(0) != null) {
                cursorLocation = lineChar.get(0);
                updateVisibleText();
            }
        }
    }

    public void setText(String text){
        ArrayList<String> words = split(text);
        lineChar.clear();

        for(String word : words) {
            for (char c : word.toCharArray()) {
                Char ch = new Char(c);

                lineChar.add(ch);
            }
        }

        if(lineChar.size() == 1)
            cursorLocation = lineChar.get(0);
        else
            cursorLocation = null;

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
            if (text.length() == 0)
                setText(String.valueOf(input));
            else if (cursorLocation != null) {
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
        isTextUpdated = false;

        if(widthLastUpdate != componentText.getWidth() || updateText){
            widthLastUpdate = componentText.getWidth();

            updateText = true;
        }

        if(fontLastUpdate != componentText.getFont() || updateText){
            fontLastUpdate = componentText.getFont();

            updateText = true;
        }

        if(updateText){
            lastTextVersion = text;
            text = "";
            updateLine();

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

            updateVisibleText();
            updateText = false;
            isTextUpdated = true;
        }
    }

    private void updateVisibleText(){
        if(text.length() != 0) {
            if (componentText.getStringWidth(text) <= componentText.getWidth()) {
                visibleText = text;
                firstCharVisible = null;
                lastCharVisible = lineChar.get(lineChar.size()-1);
                xRelative = 0;
            }
            else if (cursorLocation == null) {
                firstCharVisible = null;

                for (int i = 0; i < lineChar.size(); i++){
                    if (componentText.getStringWidth(text.substring(0, i)) > componentText.getWidth()) {
                        visibleText = text.substring(0, i - 1);
                        firstCharVisible = null;
                        lastCharVisible = lineChar.get(i-2);
                        xRelative = 0;
                        return;
                    }
                }
            } else if(firstCharVisible == cursorLocation){
                int firstIndex = lineChar.indexOf(firstCharVisible);

                if(firstIndex != 0){

                    for (int i = --firstIndex; i <= lineChar.size(); i++){
                        if (componentText.getStringWidth(text.substring(firstIndex, i)) > componentText.getWidth()) {
                            visibleText = text.substring(firstIndex, i - 1);
                            firstCharVisible = lineChar.get(firstIndex);
                            lastCharVisible = lineChar.get(i-2);
                            xRelative = componentText.getStringWidth(text.substring(0, firstIndex));
                            return;
                        }
                    }

                    visibleText = text.substring(firstIndex, text.length());
                    firstCharVisible = lineChar.get(firstIndex);
                    lastCharVisible = lineChar.get(lineChar.size()-1);
                    xRelative = componentText.getStringWidth(text.substring(0, firstIndex));
                }
                else{
                    firstCharVisible = null;

                    for (int i = 0; i < lineChar.size(); i++){
                        if (componentText.getStringWidth(text.substring(0, i)) > componentText.getWidth()) {
                            visibleText = text.substring(0, i - 1);
                            firstCharVisible = null;
                            lastCharVisible = lineChar.get(i-2);
                            xRelative = 0;
                            return;
                        }
                    }
                }
            }
            else  if(lastCharVisible == cursorLocation){
                int lastIndex = lineChar.indexOf(lastCharVisible);

                if(lastIndex+1 != lineChar.size()) {
                    ++lastIndex;

                    for (int i = lastIndex; i >= 0; i--) {
                        if (componentText.getStringWidth(text.substring(i, lastIndex + 1)) > componentText.getWidth()) {
                            visibleText = text.substring(i+1, lastIndex + 1);
                            firstCharVisible = lineChar.get(i + 1);
                            lastCharVisible = lineChar.get(lastIndex);
                            xRelative = componentText.getStringWidth(text.substring(0, i + 1));
                            return;
                        }

                        visibleText = text.substring(0, lastIndex + 1);
                        firstCharVisible = lineChar.get(0);
                        lastCharVisible = lineChar.get(lastIndex);
                        xRelative = 0;
                    }
                }
            }
            else if(lastTextVersion.length() < text.length()){
                int indexFirst = lineChar.indexOf(firstCharVisible);
                int indexCursor = lineChar.indexOf(cursorLocation);
                int distanceBetweenFirstAndCusor = indexFirst - indexCursor;

                if(distanceBetweenFirstAndCusor == 1 || distanceBetweenFirstAndCusor == -1){
                    if(distanceBetweenFirstAndCusor > 0)
                        --indexFirst;
                    if( indexFirst == -1)
                        indexFirst = 0;

                    for (int i = indexFirst; i <= lineChar.size(); i++){
                        if (componentText.getStringWidth(text.substring(indexFirst, i)) > componentText.getWidth()) {
                            visibleText = text.substring(indexFirst, i - 1);
                            firstCharVisible = lineChar.get(indexFirst);
                            lastCharVisible = lineChar.get(i-2);
                            xRelative = componentText.getStringWidth(text.substring(0, indexFirst));
                            return;
                        }
                    }

                    visibleText = text.substring(indexFirst, text.length());
                    firstCharVisible = lineChar.get(indexFirst);
                    lastCharVisible = lineChar.get(lineChar.size()-1);
                    xRelative = componentText.getStringWidth(text.substring(0, indexFirst));
                }
                else {
                    int lastIndex = lineChar.indexOf(lastCharVisible);

                    if (lineChar.indexOf(cursorLocation) + 1 == lineChar.size())
                        lastIndex++;

                    for (int i = lastIndex; i >= 0; i--) {
                        if (componentText.getStringWidth(text.substring(i, lastIndex + 1)) > componentText.getWidth()) {
                            visibleText = text.substring(i + 1, lastIndex + 1);
                            firstCharVisible = lineChar.get(i + 1);
                            lastCharVisible = lineChar.get(lastIndex);
                            xRelative = componentText.getStringWidth(text.substring(0, i + 1));
                            return;
                        }

                        visibleText = text.substring(0, lastIndex + 1);
                        firstCharVisible = lineChar.get(0);
                        lastCharVisible = lineChar.get(lastIndex);
                        xRelative = 0;
                    }
                }
            }
            else if(lastTextVersion.length() > text.length()){
                int firstIndex = lineChar.indexOf(firstCharVisible);

                if(firstIndex >= 1){
                    for (int i = firstIndex; i <= lineChar.size(); i++){
                        if (componentText.getStringWidth(text.substring(firstIndex, i)) > componentText.getWidth()) {
                            visibleText = text.substring(firstIndex, i - 1);
                            firstCharVisible = lineChar.get(firstIndex);
                            lastCharVisible = lineChar.get(i-2);
                            xRelative = componentText.getStringWidth(text.substring(0, firstIndex));
                            return;
                        }
                    }

                    visibleText = text.substring(firstIndex, text.length());
                    firstCharVisible = lineChar.get(firstIndex);
                    lastCharVisible = lineChar.get(lineChar.size()-1);
                    xRelative = componentText.getStringWidth(text.substring(0, firstIndex));
                }
                else{
                    firstCharVisible = null;

                    for (int i = 0; i < lineChar.size(); i++){
                        if (componentText.getStringWidth(text.substring(0, i)) > componentText.getWidth()) {
                            visibleText = text.substring(0, i - 1);
                            firstCharVisible = null;
                            lastCharVisible = lineChar.get(i-2);
                            xRelative = 0;
                            return;
                        }
                    }
                }
            }
        }
        else
            visibleText = "";
    }

    private void updateLine(){
        for(int i = 0; i < lineChar.size(); i++) {
            Char c = lineChar.get(i);

            this.text += c.getValue();
            c.cursorX = componentText.getStringWidth(text.substring(0, i+1));
        }
    }

    private ArrayList<String> split(String text){
        ArrayList<String> words = new ArrayList<>();
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

    @Override
    public void draw(Render render) {
        componentText.getFont().drawString(getVisibleText(), componentText.getY(), componentText.getX(), componentText.getFontSize(), componentText.getFontColor());
    }

    private class Char {

        private double cursorX;
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
