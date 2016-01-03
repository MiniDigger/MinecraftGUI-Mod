package io.github.minecraftgui.models.network.packets;

import org.json.JSONObject;

/**
 * Created by Samuel on 2015-12-29.
 */
public class PacketInitConnection extends PacketOut {
    @Override
    public JSONObject toJSON() {
        return new JSONObject();
    }
}
