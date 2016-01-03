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

package io.github.minecraftgui.models.attributes;

import io.github.minecraftgui.models.components.Component;
import io.github.minecraftgui.models.components.State;

import java.util.HashMap;

/**
 * Created by Samuel on 2015-10-22.
 */
public class AttributeGroup<V> extends Attribute {

    protected final HashMap<State, Attribute<V>> attributes;
    private final V defaultValue;
    protected Component component;

    public AttributeGroup(V defaultValue, Component component) {
        super(defaultValue);
        this.component = component;
        this.attributes = new HashMap<>();
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    public AttributeGroup(V defaultValue) {
        super(defaultValue);
        this.attributes = new HashMap<>();
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public Attribute<V> getAttribute(State state){
        return attributes.get(state);
    }

    @Override
    public void update(long updateId) {
        State state = getState();

        if(state != null) {
            attributes.get(state).update(updateId);
            value = attributes.get(state).getValue();
        }
        else
            value = defaultValue;
    }

    private State getState(){
        State state = component.getState();

        if(state == State.ACTIVE && attributes.get(State.ACTIVE).getValue() != null)
            return State.ACTIVE;
        if((state == State.HOVER  || state == State.ACTIVE) && attributes.get(State.HOVER).getValue() != null)
            return State.HOVER;
        if((component.isFocus() && (state == State.HOVER  || state == State.ACTIVE || state == State.NORMAL)) && attributes.get(State.FOCUS).getValue() != null)
            return State.FOCUS;
        else if(attributes.get(State.NORMAL).getValue() != null)
            return State.NORMAL;
        else
            return null;
    }

}
