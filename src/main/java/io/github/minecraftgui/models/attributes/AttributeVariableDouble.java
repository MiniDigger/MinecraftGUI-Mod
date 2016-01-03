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

/**
 * Created by Samuel on 2015-10-22.
 */
public class AttributeVariableDouble extends AttributeVariable<Double> {

    public AttributeVariableDouble(Attribute<Double> value, AttributeGroup<Double> attributeGroup) {
        super(value, attributeGroup);
    }

    public AttributeVariableDouble(Attribute<Double> value, AttributeGroup<Double> attributeGroup, double percentage) {
        super(value, attributeGroup, percentage);
    }

    public AttributeVariableDouble(Attribute<Double> value, AttributeGroup<Double> attributeGroup, long time) {
        super(value, attributeGroup, time);
    }

    public AttributeVariableDouble(Attribute<Double> value, AttributeGroup<Double> attributeGroup, double percentage, long time) {
        super(value, attributeGroup, percentage, time);
    }

    @Override
    public void setAttribute(Attribute<Double> value) {
        super.setAttribute(value);
    }

    @Override
    protected Double getValue(Double value, double percentage) {
        return value * percentage;
    }

    @Override
    protected Double getValue(Double valueAtTheEnd, Double valueAtStart, double percentage, double timePercentage) {
        return valueAtStart + ((valueAtTheEnd*percentage - valueAtStart) * timePercentage);
    }
}
