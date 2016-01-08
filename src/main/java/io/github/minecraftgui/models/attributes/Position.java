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
import io.github.minecraftgui.models.shapes.Margin;
import io.github.minecraftgui.models.components.State;

import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Position extends Attribute<Double> {

    protected CopyOnWriteArrayList<Attribute<Double>> relativeToAttributes;
    protected AttributeGroupDouble relative;
    protected Component component;
    protected double valueRelativeAttribute;
    private long lastUpdateId = 0;

    protected abstract double updateValue();

    public Position(Component component, Attribute<Double> relativeToAttributes[]) {
        super(0.0);
        this.relativeToAttributes = new CopyOnWriteArrayList<>(relativeToAttributes);
        this.relative = new AttributeGroupDouble(component);
        this.component = component;
        this.relative.getAttribute(State.NORMAL).setAttribute(new AttributeDouble(0.0));
    }

    public double getRelative() {
        return relative.getValue();
    }

    public Attribute<Double> getRelative(State state) {
        return this.relative.getAttribute(state);
    }

    public CopyOnWriteArrayList<Attribute<Double>> getRelativeToAttributes() {
        return relativeToAttributes;
    }

    @Override
    public void update(long updateId) {
        if(updateId != lastUpdateId) {
            relative.update(updateId);
            valueRelativeAttribute = 0;

            for (Attribute<Double> relativeToAttribute : relativeToAttributes) {
                relativeToAttribute.update(updateId);
                valueRelativeAttribute += relativeToAttribute.getValue();
            }
            lastUpdateId = updateId;
            this.value = updateValue();
        }
    }

    public static class Y extends Position {

        public Y(Component component, Attribute<Double>... relativeToAttributes) {
            super(component, relativeToAttributes);
        }

        @Override
        protected double updateValue() {
            return this.value = component.getParent().getY()+component.getShape().getMargin(Margin.TOP)+valueRelativeAttribute + relative.getValue();
        }
    }

    public static class X extends Position {

        public X(Component component, Attribute<Double>... relativeToAttributes) {
            super(component, relativeToAttributes);
        }

        @Override
        protected double updateValue() {
            return this.value = component.getParent().getX()+component.getShape().getMargin(Margin.LEFT)+valueRelativeAttribute + relative.getValue();
        }
    }

}
