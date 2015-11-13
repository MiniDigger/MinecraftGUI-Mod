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

package minecraftgui.models.attributes;

import minecraftgui.models.components.Component;

/**
 * Created by Samuel on 2015-11-03.
 */
public abstract class PositionRelative extends Attribute<Double> {

    protected final Component component;
    protected Attribute<Double> relativeToAttributes[];
    protected double percentage = 1;
    protected double relative = 0;
    protected double valueRelativeAttribute;

    public PositionRelative(Component component, double relative, Attribute<Double> relativeToAttributes[]) {
        this.component = component;
        this.relativeToAttributes = relativeToAttributes;
        this.relative = relative;
    }

    public double getRelative() {
        return relative;
    }

    public void setRelative(double relative) {
        this.relative = relative;
    }

    @Override
    public void update(long updateId) {
        valueRelativeAttribute = 0;

        for(Attribute<Double> relativeToAttribute: relativeToAttributes)
            valueRelativeAttribute += relativeToAttribute.getValue();
    }

    public static class YAxis extends PositionRelative {

        public YAxis(Component component, double relative, Attribute<Double>... relativeToAttributes) {
            super(component, relative, relativeToAttributes);
        }

        @Override
        public void update(long updateId) {
            super.update(updateId);

            this.value = component.getParent().getY() + valueRelativeAttribute*percentage + relative;
        }
    }

    public static class XAxis extends PositionRelative {

        public XAxis(Component component, double relative, Attribute<Double>... relativeToAttributes) {
            super(component, relative, relativeToAttributes);
        }

        @Override
        public void update(long updateId) {
            super.update(updateId);

            this.value = component.getParent().getX() + valueRelativeAttribute*percentage + relative;
        }
    }

}
