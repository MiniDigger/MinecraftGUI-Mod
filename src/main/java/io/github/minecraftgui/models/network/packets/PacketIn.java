package io.github.minecraftgui.models.network.packets;

import io.github.minecraftgui.controllers.MainController;
import io.github.minecraftgui.models.network.NetworkInterface;
import org.json.JSONObject;

/**
 * Created by Samuel on 2015-12-11.
 */
public abstract class PacketIn {

    protected final JSONObject jsonObject;

    public PacketIn(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
        this.jsonObject = jsonObject;
    }

}
