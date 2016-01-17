package io.github.minecraftgui.views;

import io.github.minecraftgui.controllers.MainController;
import io.github.minecraftgui.models.network.NetworkInterface;
import io.github.minecraftgui.models.network.packets.*;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.json.JSONObject;

/**
 * Created by Samuel on 2015-12-02.
 */
public class PacketHandler implements IMessageHandler<PacketHandler.Packet, IMessage>, NetworkInterface {

    private final SimpleNetworkWrapper simpleNetworkWrapper;
    private final MainController mainController;

    public PacketHandler(MainController mainController) {
        this.mainController = mainController;
        simpleNetworkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(MINECRAFT_GUI_CHANNEL);
        simpleNetworkWrapper.registerMessage(this, Packet.class, 0, Side.CLIENT);
    }

    @Override
    public IMessage onMessage(Packet packet, MessageContext ctx) {
        try{
            int id = packet.jsonObject.getInt(PACKET_ID);
            JSONObject content = packet.jsonObject.getJSONObject(CONTENT);

            switch (id){
                case PACKET_ADD_EVENT: new PacketAddEventListener(content, mainController, this); break;
                case PACKET_INIT_CLIENT: new PacketInitClient(content, mainController, this); break;
                case PACKET_INIT_INTERFACE: new PacketInitInterface(content, mainController, this); break;
                case PACKET_CREATE_COMPONENT: new PacketCreateComponent(content, mainController, this); break;
                case PACKET_DELETE_COMPONENT: new PacketDeleteComponent(content, mainController, this); break;
                case PACKET_SET_ATTRIBUTE_BACKGROUND_COLOR: new PacketSetAttribute.BackgroundColor(content, mainController, this); break;
                case PACKET_SET_ATTRIBUTE_BACKGROUND_IMAGE: new PacketSetAttribute.BackgroundImage(content, mainController, this); break;
                case PACKET_SET_ATTRIBUTE_WIDTH: new PacketSetAttribute.Width(content, mainController, this); break;
                case PACKET_SET_ATTRIBUTE_HEIGHT: new PacketSetAttribute.Height(content, mainController, this); break;
                case PACKET_ADD_COMPONENT_RELATIVE_TO_X: new PacketSetAttribute.XRelativeTo(content, mainController, this); break;
                case PACKET_ADD_COMPONENT_RELATIVE_TO_Y: new PacketSetAttribute.YRelativeTo(content, mainController, this); break;
                case PACKET_SET_RELATIVE_X: new PacketSetAttribute.XRelative(content, mainController, this); break;
                case PACKET_SET_RELATIVE_Y: new PacketSetAttribute.YRelative(content, mainController, this); break;
                case PACKET_SET_ATTRIBUTE_BORDER_BOTTOM: new PacketSetAttribute.BorderBottom(content, mainController, this); break;
                case PACKET_SET_ATTRIBUTE_BORDER_LEFT: new PacketSetAttribute.BorderLeft(content, mainController, this); break;
                case PACKET_SET_ATTRIBUTE_BORDER_RIGHT: new PacketSetAttribute.BorderRight(content, mainController, this); break;
                case PACKET_SET_ATTRIBUTE_BORDER_TOP: new PacketSetAttribute.BorderTop(content, mainController, this); break;
                case PACKET_SET_ATTRIBUTE_BORDER_BOTTOM_COLOR: new PacketSetAttribute.BorderBottomColor(content, mainController, this); break;
                case PACKET_SET_ATTRIBUTE_BORDER_LEFT_COLOR: new PacketSetAttribute.BorderLeftColor(content, mainController, this); break;
                case PACKET_SET_ATTRIBUTE_BORDER_RIGHT_COLOR: new PacketSetAttribute.BorderRightColor(content, mainController, this); break;
                case PACKET_SET_ATTRIBUTE_BORDER_TOP_COLOR: new PacketSetAttribute.BorderTopColor(content, mainController, this); break;
                case PACKET_SET_ATTRIBUTE_PADDING_BOTTOM: new PacketSetAttribute.PaddingBottom(content, mainController, this); break;
                case PACKET_SET_ATTRIBUTE_PADDING_LEFT: new PacketSetAttribute.PaddingLeft(content, mainController, this); break;
                case PACKET_SET_ATTRIBUTE_PADDING_RIGHT: new PacketSetAttribute.PaddingRight(content, mainController, this); break;
                case PACKET_SET_ATTRIBUTE_PADDING_TOP: new PacketSetAttribute.PaddingTop(content, mainController, this); break;
                case PACKET_SET_ATTRIBUTE_MARGIN_BOTTOM: new PacketSetAttribute.MarginBottom(content, mainController, this); break;
                case PACKET_SET_ATTRIBUTE_MARGIN_LEFT: new PacketSetAttribute.MarginLeft(content, mainController, this); break;
                case PACKET_SET_ATTRIBUTE_MARGIN_RIGHT: new PacketSetAttribute.MarginRight(content, mainController, this); break;
                case PACKET_SET_ATTRIBUTE_MARGIN_TOP: new PacketSetAttribute.MarginTop(content, mainController, this); break;
                case PACKET_SET_CURSOR: new PacketSetAttribute.Cursor(content, mainController, this); break;
                case PACKET_SET_VISIBILITY: new PacketSetAttribute.Visibility(content, mainController, this); break;
                case PACKET_SET_ATTRIBUTE_FONT_SIZE: new PacketSetAttribute.FontSize(content, mainController, this); break;
                case PACKET_SET_ATTRIBUTE_FONT_COLOR: new PacketSetAttribute.FontColor(content, mainController, this); break;
                case PACKET_SET_ATTRIBUTE_FONT: new PacketSetAttribute.Font(content, mainController, this); break;
                case PACKET_SET_ATTRIBUTE_CURSOR_COLOR: new PacketSetAttribute.CursorColor(content, mainController, this); break;
                case PACKET_SET_VALUE: new PacketSetAttribute.Value(content, mainController, this); break;
                case PACKET_SET_TEXT_ALIGNMEMT: new PacketSetAttribute.TextAlignment(content, mainController, this); break;
                case PACKET_SET_POSITIONS: new PacketSetAttribute.Positions(content, mainController, this); break;
                case PACKET_SET_TEXT_NB_LINE: new PacketSetAttribute.TextNbLine(content, mainController, this); break;
                case PACKET_UPDATE_LIST: new PacketUpdateList(content, mainController, this); break;
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void sendPacket(PacketOut packet) {
        try{
            Class packetClass = packet.getClass();

            if(packetClass == PacketComponentEvent.OnClick.class) simpleNetworkWrapper.sendToServer(new Packet(generateJSONHeader(PACKET_EVENT_ON_CLICK, packet.toJSON())));
            else if(packetClass == PacketComponentEvent.OnDoubleClick.class) simpleNetworkWrapper.sendToServer(new Packet(generateJSONHeader(PACKET_EVENT_ON_DOUBLE_CLICK, packet.toJSON())));
            else if(packetClass == PacketComponentEvent.OnInput.class) simpleNetworkWrapper.sendToServer(new Packet(generateJSONHeader(PACKET_EVENT_ON_INPUT, packet.toJSON())));
            else if(packetClass == PacketComponentEvent.OnKeyPressed.class) simpleNetworkWrapper.sendToServer(new Packet(generateJSONHeader(PACKET_EVENT_ON_KEY_PRESSED, packet.toJSON())));
            else if(packetClass == PacketComponentEvent.OnValueChange.class) simpleNetworkWrapper.sendToServer(new Packet(generateJSONHeader(PACKET_EVENT_ON_VALUE_CHANGED, packet.toJSON())));
            else if(packetClass == PacketComponentEvent.OnBlur.class) simpleNetworkWrapper.sendToServer(new Packet(generateJSONHeader(PACKET_EVENT_ON_BLUR, packet.toJSON())));
            else if(packetClass == PacketComponentEvent.OnFocus.class) simpleNetworkWrapper.sendToServer(new Packet(generateJSONHeader(PACKET_EVENT_ON_FOCUS, packet.toJSON())));
            else if(packetClass == PacketComponentEvent.OnRemove.class) simpleNetworkWrapper.sendToServer(new Packet(generateJSONHeader(PACKET_EVENT_ON_REMOVE, packet.toJSON())));
            else if(packetClass == PacketComponentEvent.OnMouseEnter.class) simpleNetworkWrapper.sendToServer(new Packet(generateJSONHeader(PACKET_EVENT_ON_MOUSE_ENTER, packet.toJSON())));
            else if(packetClass == PacketComponentEvent.OnMouseLeave.class) simpleNetworkWrapper.sendToServer(new Packet(generateJSONHeader(PACKET_EVENT_ON_MOUSE_LEAVE, packet.toJSON())));
            else if(packetClass == PacketGuiEvent.OnClose.class) simpleNetworkWrapper.sendToServer(new Packet(generateJSONHeader(PACKET_EVENT_ON_GUI_CLOSE, packet.toJSON())));
            else if(packetClass == PacketGuiEvent.OnOpen.class) simpleNetworkWrapper.sendToServer(new Packet(generateJSONHeader(PACKET_EVENT_ON_GUI_OPEN, packet.toJSON())));
            else if(packetClass == PacketClientInitiated.class) simpleNetworkWrapper.sendToServer(new Packet(generateJSONHeader(PACKET_CLIENT_INITIATED, packet.toJSON())));
            else if(packetClass == PacketInterfaceInitiated.class) simpleNetworkWrapper.sendToServer(new Packet(generateJSONHeader(PACKET_INTERFACE_INITIATED, packet.toJSON())));
            else if(packetClass == PacketInitConnection.class) simpleNetworkWrapper.sendToServer(new Packet(generateJSONHeader(PACKET_INIT_CONNECTION, packet.toJSON())));

        }catch (Exception e){}
    }

    private JSONObject generateJSONHeader(int id, JSONObject content){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(PACKET_ID, id);
        jsonObject.put(CONTENT, content);

        return jsonObject;
    }

    public static class Packet implements IMessage{

        private JSONObject jsonObject;

        public Packet() {}

        public Packet(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            jsonObject = new JSONObject(new String(buf.array()).trim());
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeBytes(jsonObject.toString().getBytes());
        }

    }
}
