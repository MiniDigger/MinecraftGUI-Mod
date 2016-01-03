package io.github.minecraftgui.models.network.packets;

import io.github.minecraftgui.controllers.MainController;
import io.github.minecraftgui.models.network.NetworkInterface;
import org.json.JSONObject;

/**
 * Created by Samuel on 2015-12-06.
 */
public class PacketDeleteComponent extends PacketIn {

    public PacketDeleteComponent(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
        super(jsonObject, mainController, networkInterface);
        String componentId = jsonObject.getString(NetworkInterface.COMPONENT_ID);

        mainController.removeComponent(componentId);
    }

}
