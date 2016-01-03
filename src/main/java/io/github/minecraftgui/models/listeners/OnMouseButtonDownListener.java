package io.github.minecraftgui.models.listeners;

import io.github.minecraftgui.controllers.Mouse;
import io.github.minecraftgui.models.components.Component;

/**
 * Created by Samuel on 2015-11-21.
 */
public abstract class OnMouseButtonDownListener {

    public abstract void onMouseButtonDown(Component component, Mouse mouse, Mouse.Button button);

}
