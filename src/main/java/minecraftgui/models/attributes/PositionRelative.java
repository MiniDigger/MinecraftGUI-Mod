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

/**
 * Created by Samuel on 2015-11-03.
 */
public abstract class PositionRelative extends Attribute<Double> {

    protected Attribute<Double> relativeToAttributes[];
    protected double percentage = 1;
    protected double relative = 0;
    protected double valueRelativeAttribute;

    public PositionRelative(double relative, Attribute<Double> relativeToAttributes[]) {
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

        for(Attribute<Double> relativeToAttribute: relativeToAttributes) {
            relativeToAttribute.update(updateId);
            valueRelativeAttribute += relativeToAttribute.getValue();
        }
    }

    public static class YAxis extends PositionRelative {

        public YAxis(double relative, Attribute<Double>... relativeToAttributes) {
            super(relative, relativeToAttributes);
        }

        @Override
        public void update(long updateId) {
            super.update(updateId);

            this.value = valueRelativeAttribute*percentage + relative;
        }
    }

    public static class XAxis extends PositionRelative {

        public XAxis(double relative, Attribute<Double>... relativeToAttributes) {
            super(relative, relativeToAttributes);
        }

        @Override
        public void update(long updateId) {
            super.update(updateId);

            this.value = valueRelativeAttribute*percentage + relative;
        }
    }

}
