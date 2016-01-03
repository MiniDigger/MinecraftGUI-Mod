package io.github.minecraftgui.models.network.packets;

import io.github.minecraftgui.models.network.NetworkInterface;
import org.json.JSONObject;

/**
 * Created by Samuel on 2015-12-09.
 */
public abstract class PacketComponentEvent extends PacketOut {

    protected final String componentId;

    public PacketComponentEvent(String componentId) {
        this.componentId = componentId;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(NetworkInterface.COMPONENT_ID, componentId);

        return jsonObject;
    }

    public static class OnBlur extends PacketComponentEvent {

        public OnBlur(String componentId) {
            super(componentId);
        }

    }

    public static class OnFocus extends PacketComponentEvent {

        public OnFocus(String componentId) {
            super(componentId);
        }

    }

    public static class OnClick extends PacketComponentEvent {

        public OnClick(String componentId) {
            super(componentId);
        }

    }

    public static class OnDoubleClick extends PacketComponentEvent {

        public OnDoubleClick(String componentId) {
            super(componentId);
        }

    }

    public static class OnRemove extends PacketComponentEvent {

        public OnRemove(String componentId) {
            super(componentId);
        }

    }

    public static class OnInput extends PacketComponentEvent {

        private final char input;

        public OnInput(String componentId, char input) {
            super(componentId);
            this.input = input;
        }

        @Override
        public JSONObject toJSON() {
            JSONObject jsonObject = super.toJSON();
            jsonObject.put(NetworkInterface.INPUT, input);

            return jsonObject;
        }

    }

    public static class OnKeyPressed extends PacketComponentEvent {

        private final int key;

        public OnKeyPressed(String componentId, int key) {
            super(componentId);
            this.key = key;
        }

        @Override
        public JSONObject toJSON() {
            JSONObject jsonObject = super.toJSON();
            jsonObject.put(NetworkInterface.KEY, key);

            return jsonObject;
        }

    }

    public static class OnValueChange extends PacketComponentEvent {

        private final Object value;

        public OnValueChange(String componentId, Object value) {
            super(componentId);
            this.value = value;
        }

        @Override
        public JSONObject toJSON() {
            JSONObject jsonObject = super.toJSON();
            jsonObject.put(NetworkInterface.VALUE, value);

            return jsonObject;
        }

    }

}
