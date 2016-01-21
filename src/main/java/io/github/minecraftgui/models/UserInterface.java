package io.github.minecraftgui.models;

import io.github.minecraftgui.controllers.KeyBoard;
import io.github.minecraftgui.controllers.Mouse;
import io.github.minecraftgui.controllers.Render;
import io.github.minecraftgui.controllers.Screen;
import io.github.minecraftgui.models.components.Component;
import io.github.minecraftgui.models.components.Root;
import io.github.minecraftgui.models.components.State;
import io.github.minecraftgui.models.components.Visibility;
import io.github.minecraftgui.models.listeners.OnRemoveListener;
import io.github.minecraftgui.models.repositories.FontRepository;
import io.github.minecraftgui.models.repositories.ImageRepository;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Samuel on 2015-12-12.
 */
public class UserInterface {

    private final ImageRepository imageRepository;
    private final FontRepository fontRepository;
    private final HashMap<String, Component> components;
    private final Root root;
    private boolean keyboardAssigned = false;
    private boolean canPlayerInteractWithGUI = false;
    private long updateId = Long.MIN_VALUE;
    private Component componentHoveredByTheMouse = null;
    private Component componentFocused = null;
    private final Screen screen;
    private final Render render;
    private final Mouse mouse;
    private final KeyBoard keyBoard;
    private final ArrayList<Component> hoverList1;
    private final ArrayList<Component> hoverList2;
    private ArrayList<Component> hoverListToAdd;
    private ArrayList<Component> hoverListLastUpdate;

    public UserInterface(ImageRepository imageRepository, FontRepository fontRepository, Screen screen, Render render, Mouse mouse, KeyBoard keyBoard) {
        this.imageRepository = imageRepository;
        this.fontRepository = fontRepository;
        this.screen = screen;
        this.render = render;
        this.mouse = mouse;
        this.keyBoard = keyBoard;
        this.components = new HashMap<>();
        this.hoverList1 = new ArrayList<>();
        this.hoverList2 = new ArrayList<>();
        this.hoverListLastUpdate = hoverList1;
        this.hoverListToAdd = hoverList2;
        this.root = new Root();
        components.put(root.getId(), root);
    }

    public void setCanPlayerInteractWithGUI(boolean canPlayerInteractWithGUI) {
        this.canPlayerInteractWithGUI = canPlayerInteractWithGUI;
    }

    public Component getComponent(String id){
        return components.get(id);
    }

    public void addComponent(String parentId, Component component){
        components.put(component.getId(), component);
        components.get(parentId).add(component);
        component.addOnRemoveListener(new OnRemoveListener() {
            @Override
            public void onRemove(Component component) {
                components.remove(component.getId());
            }
        });
    }

    public void removeComponent(String componentId){
        components.get(componentId).remove();
    }

    public void update(){
        updateId++;
        root.setHeight(screen.getHeight());
        root.setWidth(screen.getWidth());

        if (canPlayerInteractWithGUI) {
            if (keyboardAssigned)
                keyBoard.update(updateId);

            mouse.update(updateId);

            if (!mouse.isGrabbed()) {
                setComponentHoveredByTheMouse();
                setComponentFocusMouseAndKeyBoard();
            } else
                resetComponentHoveredAndFocused();
        } else
            resetComponentHoveredAndFocused();

        for (Component component : root.getChildren())
            update(component);
    }

    private void update(Component component){
        if(component.getVisibility() == Visibility.VISIBLE) {
            component.update(updateId);

            for (Component child : component.getChildren())
                update(child);
        }
    }

    //Le component peut être autant hover ou active
    private void setComponentHoveredByTheMouse(){
        ArrayList<Component> temp = hoverListToAdd;
        hoverListLastUpdate.clear();

        hoverListToAdd = hoverListLastUpdate;
        hoverListLastUpdate = temp;

        if(componentHoveredByTheMouse != null) {
            componentHoveredByTheMouse.setState(State.NORMAL);

            componentHoveredByTheMouse = null;
        }

        for(Component child : root.getChildren())
            setComponentHoveredByTheMouse(child);

        if(componentHoveredByTheMouse != null) {
            Component parent = componentHoveredByTheMouse.getParent();

            while(parent != null){
                parent.setState(State.HOVER);
                hoverListToAdd.add(parent);
                parent = parent.getParent();
            }

            if(mouse.isLeftPressed() || mouse.isRightPressed())
                componentHoveredByTheMouse.setState(State.ACTIVE);
            else
                componentHoveredByTheMouse.setState(State.HOVER);

            mouse.setCursor(componentHoveredByTheMouse.getCursor());
        }
        else
            mouse.setCursor(Mouse.Cursor.NORMAL);

        for(Component component : hoverListLastUpdate)
            if(componentHoveredByTheMouse != component && !hoverListToAdd.contains(component))
                component.setState(State.NORMAL);
    }

    private void resetComponentHoveredAndFocused(){
        if(componentHoveredByTheMouse != null) {
            componentHoveredByTheMouse.setState(State.NORMAL);

            for(Component component : hoverListToAdd)
                component.setState(State.NORMAL);

            hoverListToAdd.clear();

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
        if(keyBoard.getKeyListener(Keyboard.KEY_ESCAPE).isPressed() && keyboardAssigned){
            componentFocused.setFocus(false);
            componentFocused.setMouse(null);
            componentFocused.setKeyBoard(null);
            keyboardAssigned = false;
            componentFocused = null;
        }
        else if((mouse.isLeftPressed() || mouse.isMiddlePressed() || mouse.isRightPressed()) && !(mouse.isMiddlePressedLastUpdate() || mouse.isLeftPressedLastUpdate() || mouse.isRightPressedLastUpdate())){
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
        if(component.getVisibility() == Visibility.VISIBLE) {
            if (component.getShape().isLocationInside(mouse.getX(), mouse.getY()))
                componentHoveredByTheMouse = component;

            for (Component child : component.getChildren())
                setComponentHoveredByTheMouse(child);
        }
    }

    public void draw(){
        for(Component component : root.getChildren())
            draw(component);
    }

    private void draw(Component component){
        if(component.getVisibility() == Visibility.VISIBLE) {
            component.draw(render);

            for (Component child : component.getChildren())
                draw(child);
        }
    }

}
