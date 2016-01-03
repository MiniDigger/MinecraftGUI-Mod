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

import io.github.minecraftgui.controllers.KeyBoard;
import io.github.minecraftgui.controllers.Mouse;
import io.github.minecraftgui.controllers.Render;
import io.github.minecraftgui.models.Updatable;
import io.github.minecraftgui.models.attributes.Attribute;
import io.github.minecraftgui.models.attributes.AttributeGroupCursor;
import io.github.minecraftgui.models.attributes.Position;
import io.github.minecraftgui.models.listeners.*;
import io.github.minecraftgui.models.shapes.*;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Samuel on 2015-10-22.
 */
public abstract class Component implements Updatable, Drawable {

    private final String id;
    protected Component parent;
    protected CopyOnWriteArrayList<Component> children;
    private final CopyOnWriteArrayList<OnClickListener> onClickListeners;
    private final CopyOnWriteArrayList<OnDoubleClickListener> onDoubleClickListeners;
    private final CopyOnWriteArrayList<OnMouseOverListener> onMouseOverClickListeners;
    private final CopyOnWriteArrayList<OnMouseButtonDownListener> onMouseButtonDownListeners;
    private final CopyOnWriteArrayList<OnMouseButtonUpListener> onMouseButtonUpListeners;
    private final CopyOnWriteArrayList<OnKeyPressedListener> onKeyPressedListeners;
    private final CopyOnWriteArrayList<OnInputListener> onInputListeners;
    private final CopyOnWriteArrayList<OnBlurListener> onBlurListeners;
    private final CopyOnWriteArrayList<OnFocusListener> onFocusListeners;
    private final CopyOnWriteArrayList<OnPasteListener> onPasteListeners;
    private final CopyOnWriteArrayList<OnCopyListener> onCopyListeners;
    private final CopyOnWriteArrayList<OnRemoveListener> onRemoveListeners;
    protected Shape shape;
    protected KeyBoard keyBoard = null;
    private Mouse mouse = null;
    private boolean isFocus = false;
    private final Position.X x;
    private final Position.Y y;
    private AttributeGroupCursor cursor;
    private Visibility visibility =  Visibility.VISIBLE;
    private State state = State.NORMAL;

    public Component(String id, Class<? extends Shape> shape) {
        this.id = id;
        this.children = new CopyOnWriteArrayList<>();
        this.onClickListeners = new CopyOnWriteArrayList<>();
        this.onDoubleClickListeners = new CopyOnWriteArrayList<>();
        this.onMouseButtonDownListeners = new CopyOnWriteArrayList<>();
        this.onMouseButtonUpListeners = new CopyOnWriteArrayList<>();
        this.onMouseOverClickListeners = new CopyOnWriteArrayList<>();
        this.onKeyPressedListeners = new CopyOnWriteArrayList<>();
        this.onInputListeners = new CopyOnWriteArrayList<>();
        this.onBlurListeners = new CopyOnWriteArrayList<>();
        this.onFocusListeners = new CopyOnWriteArrayList<>();
        this.onPasteListeners = new CopyOnWriteArrayList<>();
        this.onCopyListeners = new CopyOnWriteArrayList<>();
        this.onRemoveListeners = new CopyOnWriteArrayList<>();
        this.x = new Position.X(this);
        this.y = new Position.Y(this);
        this.cursor = new AttributeGroupCursor(this);
        this.shape = getShapeByClass(shape);
    }

    public String getId() {
        return id;
    }

    public void addOnClickListener(OnClickListener listener){
        onClickListeners.add(listener);
    }

    public void addOnRemoveListener(OnRemoveListener listener){
        onRemoveListeners.add(listener);
    }

    public void addOnDoubleClickListener(OnDoubleClickListener listener){
        onDoubleClickListeners.add(listener);
    }

    public void addOnMouseButtonDownListener(OnMouseButtonDownListener listener){
        onMouseButtonDownListeners.add(listener);
    }

    public void addOnMouseButtonUpListener(OnMouseButtonUpListener listener){
        onMouseButtonUpListeners.add(listener);
    }

    public void addOnMouseOverClickListener(OnMouseOverListener listener){
        onMouseOverClickListeners.add(listener);
    }

    public void addOnKeyPressedListener(OnKeyPressedListener listener){
        onKeyPressedListeners.add(listener);
    }

    public void addOnInputListener(OnInputListener listener){
        onInputListeners.add(listener);
    }

    public void addOnBlurListener(OnBlurListener listener){
        onBlurListeners.add(listener);
    }

    public void addOnFocusListener(OnFocusListener listener){
        onFocusListeners.add(listener);
    }

    public void addOnCopyListener(OnCopyListener listener){
        onCopyListeners.add(listener);
    }

    public void addOnPasteListener(OnPasteListener listener){
        onPasteListeners.add(listener);
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

    public Attribute<Double> getAttributeHeight(){
        return shape.getAttributeHeight();
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

    public void add(final Component component){
        component.remove();

        this.addOnRemoveListener(new OnRemoveListener() {
            @Override
            public void onRemove(Component comp) {
                component.remove();
            }
        });
        this.children.add(component);
        component.parent = this;
    }

    public void remove(){
        if(this.parent != null) {
            for(OnRemoveListener listener : onRemoveListeners)
                listener.onRemove(this);

            parent.remove(this);
            parent = null;
        }
    }

    protected void remove(Component component){
        this.children.remove(component);
    }

    public void setCursor(State state, Mouse.Cursor cursor) {
        this.cursor.getAttribute(state).setValue(cursor);
    }

    public Mouse.Cursor getCursor(){
        return cursor.getValue();
    }

    public Position.X getPositionX() {
        return x;
    }

    public Position.Y getPositionY() {
        return y;
    }

    public double getX(){
        return x.getValue();
    }

    public double getY(){
        return y.getValue();
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
        cursor.update(updateId);
        y.update(updateId);
        x.update(updateId);
        shape.update(updateId);
        updateKeyBoard();
        updateMouse();

        if(state == State.HOVER)
            onMouseOver();
    }

    private final void onMouseOver(){
        for(OnMouseOverListener mouseEvent : onMouseOverClickListeners)
            mouseEvent.onMouseOver(this, mouse);
    }
    private final void onMouseButtonDown(Mouse mouse, Mouse.Button button){
        for(OnMouseButtonDownListener mouseEvent : onMouseButtonDownListeners)
            mouseEvent.onMouseButtonDown(this, mouse, button);
    }

    private final void onMouseButtonUp(Mouse mouse, Mouse.Button button){
        for(OnMouseButtonUpListener mouseEvent : onMouseButtonUpListeners)
            mouseEvent.onMouseButtonUp(this, mouse, button);
    }

    private final void onFocus(){
        for(OnFocusListener stateChange : onFocusListeners)
            stateChange.onFocus(this);
    }

    private final void onBlur(){
        for(OnBlurListener stateChange : onBlurListeners)
            stateChange.onBlur(this);
    }

    private final void onClick(){
        for(OnClickListener mouseEvent : onClickListeners)
            mouseEvent.onClick(this, mouse);
    }

    private final void onDoubleClick(){
        for(OnDoubleClickListener mouseEvent : onDoubleClickListeners)
            mouseEvent.onDoubleClick(this);
    }

    private final void onInput(char input){
        for(OnInputListener keyBoardEvent : onInputListeners)
            keyBoardEvent.onInput(this, input);
    }

    private final void onKeyPressed(KeyBoard keyBoard){
        for(OnKeyPressedListener keyBoardEvent : onKeyPressedListeners)
            keyBoardEvent.onKeyPressed(this, keyBoard);
    }

    private final void onCopy(){
        for(OnCopyListener onCopyListener : onCopyListeners)
            onCopyListener.onCopy(this);
    }

    private final void onPaste(){
        for(OnPasteListener onPasteListener : onPasteListeners)
            onPasteListener.onPaste(this);
    }

    private void updateMouse(){
        if(mouse != null) {
            if (mouse.isClick())
                onClick();
            if (mouse.isDoubleClick())
                onDoubleClick();

            if (mouse.isLeftPressed())
                onMouseButtonDown(mouse, Mouse.Button.LEFT);
            else if (mouse.isLeftPressedLastUpdate())
                onMouseButtonUp(mouse, Mouse.Button.LEFT);

            if (mouse.isRightPressed())
                onMouseButtonDown(mouse, Mouse.Button.RIGHT);
            else if (mouse.isRightPressedLastUpdate())
                onMouseButtonUp(mouse, Mouse.Button.RIGHT);

            if (mouse.isMiddlePressed())
                onMouseButtonDown(mouse, Mouse.Button.MIDDLE);
            else if (mouse.isMiddlePressedLastUpdate())
                onMouseButtonUp(mouse, Mouse.Button.MIDDLE);
        }
    }

    private void updateKeyBoard(){
        if(keyBoard != null){
            if(keyBoard.getInput() != 0){
                switch(keyBoard.getInput()){
                    case 3: onCopy(); break;
                    case 22: onPaste(); break;
                    default: onInput(keyBoard.getInput()); break;
                }
            }

            if(keyBoard.isKeyPressed())
                onKeyPressed(keyBoard);
        }
    }

    @Override
    public void draw(Render render) {
        shape.draw(render);
    }

    protected Shape getShapeByClass(Class<? extends Shape> shape){
        if(shape == EllipseColor.class)
            return new EllipseColor(this);
        else if(shape == PolygonColor.class)
            return new PolygonColor(this);
        else if(shape == RectangleColor.class)
            return new RectangleColor(this);
        else if(shape == RectangleImage.class)
            return new RectangleImage(this);

        return null;
    }

}
