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
 * Created by Samuel on 2015-09-30.
 */
public abstract class AttributeAnimation<V> extends AttributeRelative {

    private final AttributeGroup<V> attributeGroup;
    private double time = 1;//millisecond
    private long lastUpdateId = Long.MIN_VALUE;
    private long timeStarted = System.currentTimeMillis();
    protected V valueAtStartOfAnimation;
    protected double timePercentage = 0;

    public AttributeAnimation(Attribute<V> relativeTo, AttributeGroup<V> attributeGroup) {
        super(relativeTo);
        this.attributeGroup = attributeGroup;
    }

    public void setTime(long time) {
        this.time = time <= 0?1:time;
    }

    @Override
    public void update(long updateId){
        long currentTime = System.currentTimeMillis();

        if(lastUpdateId+1 != updateId) {
            timeStarted = currentTime;
            valueAtStartOfAnimation = (V) attributeGroup.getValue();
        }

        timePercentage = (currentTime - timeStarted)/time;

        if(1 < timePercentage)
            timePercentage = 1;

        lastUpdateId = updateId;
    }

}
