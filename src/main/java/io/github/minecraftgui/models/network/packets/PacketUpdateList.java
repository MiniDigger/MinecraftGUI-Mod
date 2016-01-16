package io.github.minecraftgui.models.network.packets;

import io.github.minecraftgui.controllers.MainController;
import io.github.minecraftgui.models.components.List;
import io.github.minecraftgui.models.network.NetworkInterface;
import org.json.JSONObject;

/**
 * Created by Samuel on 2016-01-16.
 */
public class PacketUpdateList extends PacketIn {

    public PacketUpdateList(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
        super(jsonObject, mainController, networkInterface);
        List list = (List) mainController.getComponent(jsonObject.getString(NetworkInterface.COMPONENT_ID));
        list.updateLists();
    }

}
