package io.github.minecraftgui.models.network.packets;

import org.json.JSONObject;

/**
 * Created by Samuel on 2016-01-02.
 */
public abstract class PacketGuiEvent extends PacketOut {

    @Override
    public JSONObject toJSON() {
        return new JSONObject();
    }

    public static class OnOpen extends PacketGuiEvent{}

    public static class OnClose extends PacketGuiEvent{}

}
