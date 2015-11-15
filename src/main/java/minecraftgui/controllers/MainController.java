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

package minecraftgui.controllers;

import minecraftgui.models.attributes.*;
import minecraftgui.models.components.*;
import minecraftgui.models.components.Component;
import minecraftgui.models.fonts.UnicodeFont;
import minecraftgui.models.shapes.RectangleColor;
import minecraftgui.views.ModInterface;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public class MainController {

    private Root root;
    private ModInterface modInterface;
    private Screen screen;
    private Render render;
    private Mouse mouse;
    private boolean canPlayerInteractWithGUI = false;
    private boolean keyboardAssigned = false;
    private KeyBoard keyBoard;
    private long updateId = Long.MIN_VALUE;
    private Component componentHoveredByTheMouse = null;
    private Component componentFocused = null;

    public MainController(ModInterface modInterface) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        this.modInterface = modInterface;
        this.screen = new Screen();
        this.render = new Render(modInterface);
        this.mouse = new Mouse(screen);
        this.keyBoard = new KeyBoard();
        this.root = new Root();
    }

    public long getUpdateId() {
        return updateId;
    }

    public void setCanPlayerInteractWithGUI(boolean canPlayerInteractWithGUI) {
        this.canPlayerInteractWithGUI = canPlayerInteractWithGUI;
    }

    public void setScreen(int width, int height, int scaleFactor){
        screen.setHeight(height);
        screen.setWidth(width);
        screen.setScaleFactor(scaleFactor);
    }

    public void update(){
        if(updateId == Long.MIN_VALUE){
            UnicodeFont font = new UnicodeFont(screen, Font.decode("Arial"));
            font.generate(12, Color.RED);
            font.generate(12, Color.BLUE);

            Input div = null;
            try {
                div = new Input("", root, RectangleColor.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }

            div.setxRelative(State.NORMAL, new PositionRelative.XAxis(div, 2, div.getShape().getAttributePadding(Padding.LEFT)));
            div.setyRelative(State.NORMAL, new PositionRelative.YAxis(div, 2));
            div.setFont(State.NORMAL, new AttributeFont(font));
            div.setFontSize(State.NORMAL, new AttributeDouble(12));
            div.setFontColor(State.NORMAL, new AttributeColor(Color.RED));
            div.setFont(State.HOVER, new AttributeFont(font));
            div.setFontSize(State.HOVER, new AttributeDouble(12));
            div.setFontColor(State.HOVER, new AttributeColor(Color.BLUE));
            div.getShape().setWidth(State.NORMAL, new AttributeDouble(50));
            div.getShape().setHeight(State.NORMAL, new AttributeDouble(50));
            div.getShape().setPadding(State.NORMAL, Padding.LEFT, new AttributeDouble(8));
            div.getShape().setPadding(State.NORMAL, Padding.TOP, new AttributeDouble(8));
            div.setText("Alloasdjhask"+((char) 10)+"dajsdhakjsdh");
            div.getShape().setBorder(State.NORMAL, Border.TOP, new AttributeDouble(50));
            div.getShape().setBackground(State.NORMAL, new AttributeColor(Color.WHITE));
            div.getShape().setBackground(State.FOCUS, new AttributeColor(Color.BLACK));
            div.getShape().setBackground(State.HOVER, new AttributeColor(Color.GREEN));
            div.getShape().setBackground(State.ACTIVE, new AttributeColor(Color.RED));
        }

        updateId++;
        root.setHeight(screen.getHeight());
        root.setWidth(screen.getWidth());

        if(canPlayerInteractWithGUI) {
            if (keyboardAssigned)
                keyBoard.update(updateId);

            mouse.update(updateId);

            if (!mouse.isGrabbed()) {
                setComponentHoveredByTheMouse();
                setComponentFocusMouseAndKeyBoard();
            } else
                resetComponentHoveredAndFocused();
        }
        else
            resetComponentHoveredAndFocused();

        for(Component component : root.getChildren())
            update(component);
    }

    private void update(Component component){
        component.update(updateId);

        for(Component child : component.getChildren())
            update(child);
    }

    //Le component peut être autant hover ou active
    private void setComponentHoveredByTheMouse(){
        if(componentHoveredByTheMouse != null) {
            componentHoveredByTheMouse.setState(State.NORMAL);

            if(componentHoveredByTheMouse.getParent() != null)
                componentHoveredByTheMouse.getParent().setState(State.NORMAL);

            componentHoveredByTheMouse = null;
        }

        for(Component child : root.getChildren())
            setComponentHoveredByTheMouse(child);

        if(componentHoveredByTheMouse != null) {
            if(mouse.isLeftPressed() || mouse.isRightPressed())
                componentHoveredByTheMouse.setState(State.ACTIVE);
            else
                componentHoveredByTheMouse.setState(State.HOVER);

            mouse.setCursor(componentHoveredByTheMouse.getCursor());

            if(componentHoveredByTheMouse.getParent() != null)
                componentHoveredByTheMouse.getParent().setState(State.HOVER);
        }
        else
            mouse.setCursor(Mouse.Cursor.NORMAL);
    }

    private void resetComponentHoveredAndFocused(){
        if(componentHoveredByTheMouse != null) {
            componentHoveredByTheMouse.setState(State.NORMAL);

            if(componentHoveredByTheMouse.getParent() != null)
                componentHoveredByTheMouse.getParent().setState(State.NORMAL);

            componentHoveredByTheMouse = null;
            mouse.setCursor(Mouse.Cursor.NORMAL);
        }

        if(componentFocused != null) {
            componentFocused.setFocus(false);
            componentFocused.setMouse(null);
            componentFocused.setKeyBoard(null);
            keyboardAssigned = false;
        }
    }

    //Pour setter le component si il est focus et donner ou enlever acces au clavier et a la souris
    private void setComponentFocusMouseAndKeyBoard(){
        if((mouse.isLeftPressed() || mouse.isMiddlePressed() || mouse.isRightPressed()) && !(mouse.isMiddlePressedLastUpdate() || mouse.isLeftPressedLastUpdate() || mouse.isRightPressedLastUpdate())){
            if(componentHoveredByTheMouse != componentFocused){
                if(componentFocused != null) {
                    componentFocused.setFocus(false);
                    componentFocused.setMouse(null);
                    componentFocused.setKeyBoard(null);
                    keyboardAssigned = false;
                }

                if(componentHoveredByTheMouse != null){
                    componentFocused = componentHoveredByTheMouse;
                    componentFocused.setFocus(true);
                    componentFocused.setMouse(mouse);
                    componentFocused.setKeyBoard(keyBoard);
                    keyboardAssigned = true;
                }
                else
                    componentFocused = null;
            }
            else if(componentFocused != null)
                componentFocused.setMouse(mouse);
        }
    }

    private void setComponentHoveredByTheMouse(Component component){
        if(component.getShape().isLocationInside(mouse.getX(), mouse.getY()))
            componentHoveredByTheMouse = component;

        for(Component child : component.getChildren())
            setComponentHoveredByTheMouse(child);
    }

    public void draw(){
        for(Component component : root.getChildren())
            draw(component);
    }

    private void draw(Component component){
        component.draw(render);

        for(Component child : component.getChildren())
            draw(child);
    }

}
