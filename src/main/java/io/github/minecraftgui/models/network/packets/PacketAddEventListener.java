package io.github.minecraftgui.models.network.packets;

import io.github.minecraftgui.controllers.KeyBoard;
import io.github.minecraftgui.controllers.MainController;
import io.github.minecraftgui.controllers.Mouse;
import io.github.minecraftgui.models.components.Component;
import io.github.minecraftgui.models.components.ComponentValuable;
import io.github.minecraftgui.models.listeners.*;
import io.github.minecraftgui.models.network.NetworkInterface;
import org.json.JSONObject;

/**
 * Created by Samuel on 2015-12-06.
 */
public class PacketAddEventListener extends PacketIn {

    public PacketAddEventListener(JSONObject jsonObject, MainController mainController, final NetworkInterface networkInterface) {
        super(jsonObject, mainController, networkInterface);
        String componentId = jsonObject.getString(NetworkInterface.COMPONENT_ID);
        String event = jsonObject.getString(NetworkInterface.EVENT);
        Component component = mainController.getComponent(componentId);

        if(component != null) {
            switch (event) {
                case NetworkInterface.ON_BLUR_LISTENER:
                    component.addOnBlurListener(new OnBlurListener() {
                        @Override
                        public void onBlur(Component component) {
                            networkInterface.sendPacket(new PacketComponentEvent.OnBlur(component.getId()));
                        }
                    });
                    break;
                case NetworkInterface.ON_CLICK_LISTENER:
                    component.addOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(Component component, Mouse mouse) {
                            networkInterface.sendPacket(new PacketComponentEvent.OnClick(component.getId()));
                        }
                    });
                    break;
                case NetworkInterface.ON_DOUBLE_CLICK_LISTENER:
                    component.addOnDoubleClickListener(new OnDoubleClickListener() {
                        @Override
                        public void onDoubleClick(Component component) {
                            networkInterface.sendPacket(new PacketComponentEvent.OnDoubleClick(component.getId()));
                        }
                    });
                    break;
                case NetworkInterface.ON_FOCUS_LISTENER:
                    component.addOnFocusListener(new OnFocusListener() {
                        @Override
                        public void onFocus(Component component) {
                            networkInterface.sendPacket(new PacketComponentEvent.OnFocus(component.getId()));
                        }
                    });
                    break;
                case NetworkInterface.ON_INPUT_LISTENER:
                    component.addOnInputListener(new OnInputListener() {
                        @Override
                        public void onInput(Component component, char input) {
                            networkInterface.sendPacket(new PacketComponentEvent.OnInput(component.getId(), input));
                        }
                    });
                    break;
                case NetworkInterface.ON_KEY_PRESSED_LISTENER:
                    component.addOnKeyPressedListener(new OnKeyPressedListener() {
                        @Override
                        public void onKeyPressed(Component component, KeyBoard keyBoard) {
                            for (int keyCode : keyBoard.getKeyListeners()) {
                                if (keyBoard.getKeyListener(keyCode).isPressed())
                                    networkInterface.sendPacket(new PacketComponentEvent.OnKeyPressed(component.getId(), keyCode));
                            }
                        }
                    });
                    break;
                case NetworkInterface.ON_REMOVE_LISTENER:
                    component.addOnRemoveListener(new OnRemoveListener() {
                        @Override
                        public void onRemove(Component component) {
                            networkInterface.sendPacket(new PacketComponentEvent.OnRemove(component.getId()));
                        }
                    });
                    break;
                case NetworkInterface.ON_MOUSE_ENTER_LISTENER:
                    component.addOnMouseEnterListener(new OnMouseEnterListener() {
                        @Override
                        public void onMouseEnter(Component component) {
                            networkInterface.sendPacket(new PacketComponentEvent.OnMouseEnter(component.getId()));
                        }
                    });
                    break;
                case NetworkInterface.ON_MOUSE_LEAVE_LISTENER:
                    component.addOnMouseLeaveListener(new OnMouseLeaveListener() {
                        @Override
                        public void onMouseLeave(Component component) {
                            networkInterface.sendPacket(new PacketComponentEvent.OnMouseLeave(component.getId()));
                        }
                    });
                    break;
                case NetworkInterface.ON_VALUE_CHANGE_LISTENER:
                    if(component instanceof ComponentValuable) {
                        final ComponentValuable valuable = (ComponentValuable) component;

                        valuable.addOnValueChangeListener(new OnValueChangeListener() {
                            @Override
                            public void onValueChange(ComponentValuable component) {
                                networkInterface.sendPacket(new PacketComponentEvent.OnValueChange(component.getId(), component.getValue()));
                            }
                        });
                    }
                    break;
            }
        }
    }

}
