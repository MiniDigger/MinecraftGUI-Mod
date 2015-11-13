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

import minecraftgui.controllers.KeyBoard;
import minecraftgui.controllers.Mouse;
import minecraftgui.controllers.Render;
import minecraftgui.models.Updatable;
import minecraftgui.models.attributes.Attribute;
import minecraftgui.models.attributes.AttributeGroupCursor;
import minecraftgui.models.attributes.AttributeGroupDouble;
import minecraftgui.models.attributes.PositionRelative;
import minecraftgui.models.shapes.Shape;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Samuel on 2015-10-22.
 */
public abstract class Component implements Updatable, Drawable {

    private final String id;
    private final Component parent;
    private final CopyOnWriteArrayList<Component> children;
    protected Shape shape;
    protected KeyBoard keyBoard = null;
    private Mouse mouse = null;
    private boolean isFocus = false;
    private AttributeGroupDouble xRelative;
    private AttributeGroupDouble yRelative;
    private AttributeGroupCursor cursor;
    private Visibility visibility =  Visibility.VISIBLE;
    private State state = State.NORMAL;
    private double x = 0;
    private double y = 0;

    /*
    //Bouger un component
    @Override
    public void onMouseButtonDown(Mouse.Button button, Mouse mouse){
        PositionRelative y = (PositionRelative) getyRelative(State.NORMAL);
        PositionRelative x = (PositionRelative) getxRelative(State.NORMAL);

        y.setRelative(y.getRelative() - (mouse.getYLastUpdate() - mouse.getY()));
        x.setRelative(x.getRelative() - (mouse.getXLastUpdate() - mouse.getX()));
    }
     */

    protected void onMouseOver(){}
    protected void onMouseButtonDown(Mouse.Button button, Mouse mouse){}
    protected void onMouseButtonUp(Mouse.Button button, Mouse mouse){}
    protected void onFocus(){}
    protected void onBlur(){}
    protected void onClick(){}
    protected void onDoubleClick(){}

    protected void onInput(char input){
        switch(input){
            case 3: onCopy(); break;
            case 22: onPaste(); break;
        }
    }

    protected void onKeyPressed(KeyBoard keyBoard){}
    protected void onCopy(){}
    protected void onPaste(){}

    public Component(String id, Component parent, Class<? extends Shape> shape) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        this.id = id;
        this.parent = parent;
        this.children = new CopyOnWriteArrayList<>();
        this.xRelative = new AttributeGroupDouble(this);
        this.yRelative = new AttributeGroupDouble(this);
        this.cursor = new AttributeGroupCursor(this);
        this.shape = shape.getDeclaredConstructor(Component.class).newInstance(this);

        if(parent != null)
            parent.children.add(this);
    }

    public Component getParent() {
        return parent;
    }

    public Shape getShape() {
        return shape;
    }

    public Attribute<Double> getAttributeWidth(){
        return shape.getAttributeWidth();
    }

    public Attribute<Double> getAttributHeight(){
        return shape.getAttributHeight();
    }

    public boolean isFocus() {
        return isFocus;
    }

    public void setFocus(boolean isFocus) {
        this.isFocus = isFocus;

        if(isFocus)
            onFocus();
        else
            onBlur();
    }

    public void setKeyBoard(KeyBoard keyBoard) {
        this.keyBoard = keyBoard;
    }

    public void setMouse(Mouse mouse) {
        this.mouse = mouse;
    }

    public CopyOnWriteArrayList<Component> getChildren() {
        return children;
    }

    public void remove(){
        parent.children.remove(this);
    }

    public void setCursor(State state, Attribute<Mouse.Cursor> cursor) {
        this.cursor.setAttribute(state, cursor);
    }

    public Mouse.Cursor getCursor(){
        return cursor.getValue();
    }

    public Attribute<Double> getxRelative(State state) {
        return xRelative.getAttribute(state);
    }

    public void setxRelative(State state, PositionRelative.XAxis xRelative) {
        this.xRelative.setAttribute(state, xRelative);
    }

    public Attribute<Double> getyRelative(State state) {
        return yRelative.getAttribute(state);
    }

    public void setyRelative(State state, PositionRelative.YAxis yRelative) {
        this.yRelative.setAttribute(state, yRelative);
    }

    public double getX(){
        return x + xRelative.getValue() + shape.getMargin(Margin.LEFT);
    }

    public double getY(){
        return y + yRelative.getValue() + shape.getMargin(Margin.TOP);
    }

    public double getWidth(){
        return shape.getWidth();
    }

    public double getHeight(){
        return shape.getHeight();
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    @Override
    public void update(long updateId) {
        x = parent.getX();
        y = parent.getY();
        cursor.update(updateId);
        xRelative.update(updateId);
        yRelative.update(updateId);
        shape.update(updateId);
        updateKeyBoard();
        updateMouse();

        if(state == State.HOVER)
            onMouseOver();
    }

    public void updateMouse(){
        if(mouse != null) {
            if (mouse.isClick())
                onClick();
            else if (mouse.isDoubleClick())
                onDoubleClick();

            if (mouse.isLeftPressed())
                onMouseButtonDown(Mouse.Button.LEFT, mouse);
            else if (mouse.isLeftPressedLastUpdate())
                onMouseButtonUp(Mouse.Button.LEFT, mouse);

            if (mouse.isRightPressed())
                onMouseButtonDown(Mouse.Button.RIGHT, mouse);
            else if (mouse.isRightPressedLastUpdate())
                onMouseButtonUp(Mouse.Button.RIGHT, mouse);

            if (mouse.isMiddlePressed())
                onMouseButtonDown(Mouse.Button.MIDDLE, mouse);
            else if (mouse.isMiddlePressedLastUpdate())
                onMouseButtonUp(Mouse.Button.MIDDLE, mouse);
        }
    }

    private void updateKeyBoard(){
        if(keyBoard != null){
            if(keyBoard.getInput() != 0)
                onInput(keyBoard.getInput());

            if(keyBoard.isKeyPressed())
                onKeyPressed(keyBoard);
        }
    }

    @Override
    public void draw(Render render) {
        shape.draw(render);
    }

}
