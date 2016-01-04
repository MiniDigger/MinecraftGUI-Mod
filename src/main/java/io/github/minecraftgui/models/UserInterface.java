package io.github.minecraftgui.models;

import io.github.minecraftgui.controllers.*;
import io.github.minecraftgui.models.components.Component;
import io.github.minecraftgui.models.components.Root;
import io.github.minecraftgui.models.components.State;
import io.github.minecraftgui.models.components.Visibility;
import io.github.minecraftgui.models.listeners.OnRemoveListener;
import io.github.minecraftgui.models.repositories.FontRepository;
import io.github.minecraftgui.models.repositories.ImageRepository;

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

    public UserInterface(ImageRepository imageRepository, FontRepository fontRepository, Screen screen, Render render, Mouse mouse, KeyBoard keyBoard) {
        this.imageRepository = imageRepository;
        this.fontRepository = fontRepository;
        this.screen = screen;
        this.render = render;
        this.mouse = mouse;
        this.keyBoard = keyBoard;
        this.components = new HashMap<>();
        this.root = new Root();
        components.put(root.getId(), root);
    }

    private void test(){
        if(updateId == Long.MIN_VALUE){
            /*TextArea div = new TextArea("", RectangleColor.class, new Div("", RectangleColor.class), new Div("", RectangleColor.class));
            ((AttributeVariableDouble)div.getPositionX().getRelative(State.NORMAL)).setAttribute(new AttributeDouble(30.0));
            ((AttributeVariableDouble)div.getPositionY().getRelative(State.NORMAL)).setAttribute(new AttributeDouble(30.0));
            div.getShape().getWidth(State.NORMAL).setAttribute(new AttributeDouble(200.0));
            div.getShape().getHeight(State.NORMAL).setAttribute(new AttributeDouble(200.0));
            div.setFont(State.NORMAL, fontRepository.getFont("orange juice"));
            div.setFontSize(State.NORMAL, 24);
            div.setFontColor(State.NORMAL, Color.BLACK);
            div.setNbLinesToDisplay(4);
            div.getTextCursorColor(State.NORMAL).setAttribute(new AttributeColor(Color.WHITE));
            ((AttributeVariableColor) div.getShape().getBackground(State.NORMAL)).setAttribute(new AttributeColor(new Color(50, 180, 166, 125)));
            ((AttributeVariableColor) div.getShape().getBackground(State.NORMAL)).setTime(500);
            ((AttributeVariableColor) div.getShape().getBackground(State.HOVER)).setAttribute(new AttributeColor(new Color(149, 100, 166, 255)));
            ((AttributeVariableColor) div.getShape().getBackground(State.HOVER)).setTime(500);

            ((AttributeVariableDouble)div.getButtonLineBefore().getPositionY().getRelative(State.NORMAL)).setAttribute(div.getAttributeHeight());
            div.getButtonLineBefore().getShape().getWidth(State.NORMAL).setAttribute(div.getAttributeWidth());
            div.getButtonLineBefore().getShape().getWidth(State.NORMAL).setPercentage(0.5);
            div.getButtonLineBefore().getShape().getHeight(State.NORMAL).setAttribute(new AttributeDouble(10.0));
            ((AttributeVariableColor) div.getButtonLineBefore().getShape().getBackground(State.NORMAL)).setAttribute(new AttributeColor(new Color(46, 204, 113)));
            div.getButtonLineBefore().setCursor(State.NORMAL, Mouse.Cursor.HAND);

            ((AttributeVariableDouble)div.getButtonLineAfter().getPositionY().getRelative(State.NORMAL)).setAttribute(div.getAttributeHeight());
            ((AttributeVariableDouble)div.getButtonLineAfter().getPositionX().getRelative(State.NORMAL)).setAttribute(div.getAttributeWidth());
            ((AttributeVariableDouble)div.getButtonLineAfter().getPositionX().getRelative(State.NORMAL)).setPercentage(0.5);
            div.getButtonLineAfter().getShape().getWidth(State.NORMAL).setAttribute(div.getAttributeWidth());
            div.getButtonLineAfter().getShape().getWidth(State.NORMAL).setPercentage(0.5);
            div.getButtonLineAfter().getShape().getHeight(State.NORMAL).setAttribute(new AttributeDouble(10.0));
            ((AttributeVariableColor) div.getButtonLineAfter().getShape().getBackground(State.NORMAL)).setAttribute(new AttributeColor(new Color(52, 152, 219)));
            div.getButtonLineAfter().setCursor(State.NORMAL, Mouse.Cursor.HAND);
            div.setText("Bonjour peuple minecraftien! Je suis le nouveau maitre des devs!");
            root.add(div);*/


            /*List div = new List("", RectangleColor.class, new Div("", RectangleColor.class), new Div("", RectangleColor.class));
            root.add(div);

            ((AttributeVariableDouble)div.getPositionX().getRelative(State.NORMAL)).setAttribute(new AttributeDouble(30.0));
            ((AttributeVariableDouble)div.getPositionY().getRelative(State.NORMAL)).setAttribute(new AttributeDouble(30.0));
            div.getShape().getWidth(State.NORMAL).setAttribute(new AttributeDouble(60.0));
            div.getShape().getHeight(State.NORMAL).setAttribute(new AttributeDouble(70.0));
            ((AttributeVariableColor)div.getShape().getBackground(State.NORMAL)).setAttribute(new AttributeColor(new Color(149, 0, 166)));

            ((AttributeVariableDouble)div.getButtonListBefore().getPositionY().getRelative(State.NORMAL)).setAttribute(div.getAttributeHeight());
            div.getButtonListBefore().getShape().getWidth(State.NORMAL).setAttribute(div.getAttributeWidth());
            div.getButtonListBefore().getShape().getWidth(State.NORMAL).setPercentage(0.5);
            div.getButtonListBefore().getShape().getHeight(State.NORMAL).setAttribute(new AttributeDouble(10.0));
            ((AttributeVariableColor) div.getButtonListBefore().getShape().getBackground(State.NORMAL)).setAttribute(new AttributeColor(new Color(46, 204, 113)));
            div.getButtonListBefore().setCursor(State.NORMAL, Mouse.Cursor.HAND);

            ((AttributeVariableDouble)div.getButtonListAfter().getPositionY().getRelative(State.NORMAL)).setAttribute(div.getAttributeHeight());
            ((AttributeVariableDouble)div.getButtonListAfter().getPositionX().getRelative(State.NORMAL)).setAttribute(div.getAttributeWidth());
            ((AttributeVariableDouble)div.getButtonListAfter().getPositionX().getRelative(State.NORMAL)).setPercentage(0.5);
            div.getButtonListAfter().getShape().getWidth(State.NORMAL).setAttribute(div.getAttributeWidth());
            div.getButtonListAfter().getShape().getWidth(State.NORMAL).setPercentage(0.5);
            div.getButtonListAfter().getShape().getHeight(State.NORMAL).setAttribute(new AttributeDouble(10.0));
            ((AttributeVariableColor) div.getButtonListAfter().getShape().getBackground(State.NORMAL)).setAttribute(new AttributeColor(new Color(52, 152, 219)));
            div.getButtonListAfter().setCursor(State.NORMAL, Mouse.Cursor.HAND);

            Color colors[] = new Color[]{new Color(26, 188, 156), new Color(241, 196, 15), new Color(231, 76, 60), new Color(46, 204, 113), new Color(52, 152, 219), new Color(142, 68, 173)};

            for(int i = 0; i < 56; i++){
                Paragraph p = new Paragraph("", RectangleColor.class, new Div("", RectangleColor.class), new Div("", RectangleColor.class));
                div.add(p);

                p.setFont(State.NORMAL, fontRepository.getFont("orange juice"));
                p.setFontSize(State.NORMAL, 24);
                p.setFontColor(State.NORMAL, Color.BLACK);
                p.setText("- " + i);
                p.getShape().getWidth(State.NORMAL).setAttribute(div.getAttributeWidth());
                p.getShape().getHeight(State.NORMAL).setAttribute(new AttributeDouble(10.0));
                ((AttributeVariableColor) p.getShape().getBackground(State.NORMAL)).setAttribute(new AttributeColor(colors[i % colors.length]));
                ((AttributeVariableColor) p.getShape().getBackground(State.HOVER)).setAttribute(new AttributeColor(Color.WHITE));
                p.setCursor(State.NORMAL, Mouse.Cursor.HAND);

                final List finalDiv = div;
                p.addOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(Component component, Mouse mouse) {
                        component.remove();
                        finalDiv.updateLists();
                    }
                });

                p.getButtonLineBefore().setVisibility(Visibility.INVISIBLE);
                p.getButtonLineAfter().setVisibility(Visibility.INVISIBLE);
            }
            div.setNbComponentPerList(7);
            div.updateLists();*/

            //Slider vrm cool
            /*final Slider.Horizontal div = new Slider.Horizontal("", RectangleColor.class, RectangleColor.class, new Div("", RectangleColor.class));
            //div.setButton(new Div("", RectangleColor.class));
            root.add(div);

            ((AttributeVariableDouble)div.getPositionX().getRelative(State.NORMAL)).setAttribute(new AttributeDouble(30.0));
            ((AttributeVariableDouble)div.getPositionY().getRelative(State.NORMAL)).setAttribute(new AttributeDouble(30.0));
            div.getShape().getWidth(State.NORMAL).setAttribute(new AttributeDouble(50.0));
            div.getShape().getHeight(State.NORMAL).setAttribute(new AttributeDouble(2.0));
            ((AttributeVariableColor)div.getShape().getBackground(State.NORMAL)).setAttribute(new AttributeColor(new Color(236, 240, 241, 125)));
            ((AttributeVariableColor)div.getProgressBar().getProgressShape().getBackground(State.NORMAL)).setAttribute(new AttributeColor(new Color(231, 76, 60, 255)));
            ((AttributeVariableColor)div.getButton().getShape().getBackground(State.NORMAL)).setAttribute(new AttributeColor(new Color(236, 240, 241, 255)));
            div.getButton().getShape().getWidth(State.NORMAL).setAttribute(new AttributeDouble(2.0));
            div.getButton().getShape().getHeight(State.NORMAL).setAttribute(new AttributeDouble(6.0));
            div.getButton().setCursor(State.NORMAL, Mouse.Cursor.HAND);
            div.getProgressBar().setCursor(State.NORMAL, Mouse.Cursor.HAND);
            div.getProgressBar().getProgressShape().getWidth(State.NORMAL).setTime(300);

            div.addOnClickListener(new OnClickListener() {
                @Override
                public void onClick(Component component, Mouse mouse) {
                    if (div.getBarPercentage() < 1.0)
                        div.setBarPercentage(1.0);
                    else if (div.getBarPercentage() == 1.0)
                        div.setBarPercentage(0.5);
                }
            });

            div.getButton().getPositionX().getRelativeToAttributes().add(new AttributeVariableDouble(div.getButton().getAttributeWidth(), null, -0.5));
            div.getButton().getPositionY().getRelativeToAttributes().add(new AttributeVariableDouble(div.getAttributeHeight(), null, 0.5));
            div.getButton().getPositionY().getRelativeToAttributes().add(new AttributeVariableDouble(div.getButton().getAttributeHeight(), null, -0.5));
            div.setBarPercentage(0.2);*/
        }
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
        System.out.println(components.size());
    }

    public void update(){
        test();
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
