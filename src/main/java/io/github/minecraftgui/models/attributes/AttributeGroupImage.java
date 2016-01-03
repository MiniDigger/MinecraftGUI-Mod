package io.github.minecraftgui.models.attributes;

import io.github.minecraftgui.models.components.Component;
import io.github.minecraftgui.models.components.State;
import io.github.minecraftgui.models.images.Image;

/**
 * Created by Samuel on 2015-12-13.
 */
public class AttributeGroupImage extends AttributeGroup<Image> {

    public AttributeGroupImage(Component component) {
        super(null, component);

        attributes.put(State.NORMAL, new AttributeImage(null));
        attributes.put(State.HOVER, new AttributeImage(null));
        attributes.put(State.ACTIVE, new AttributeImage(null));
        attributes.put(State.FOCUS, new AttributeImage(null));
    }

}