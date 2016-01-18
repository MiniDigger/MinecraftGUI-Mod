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
import io.github.minecraftgui.models.shapes.Margin;

/**
 * Created by Samuel on 2015-10-24.
 */
public class AttributeGroupDouble extends AttributeGroup<Double> {

    public AttributeGroupDouble(Component component) {
        super(0.0, component);

        attributes.put(State.NORMAL, new AttributeVariableDouble(null, this));
        attributes.put(State.HOVER, new AttributeVariableDouble(null, this));
        attributes.put(State.ACTIVE, new AttributeVariableDouble(null, this));
        attributes.put(State.FOCUS, new AttributeVariableDouble(null, this));
    }

    @Override
    public AttributeVariableDouble getAttribute(State state) {
        return (AttributeVariableDouble) super.getAttribute(state);
    }

    @Override
    public Double getValue() {
        return super.getValue();
    }

    public static class Width extends AttributeGroupDouble{

        public Width(Component component) {
            super(component);
        }

        @Override
        public Double getValue() {
            return super.getValue()-component.getShape().getAttributeMargin(Margin.LEFT).getValue()-component.getShape().getAttributeMargin(Margin.RIGHT).getValue();
        }

    }

    public static class Height extends AttributeGroupDouble{

        public Height(Component component) {
            super(component);
        }

        @Override
        public Double getValue() {
            return super.getValue()-component.getShape().getAttributeMargin(Margin.TOP).getValue()-component.getShape().getAttributeMargin(Margin.BOTTOM).getValue();
        }

    }
}
