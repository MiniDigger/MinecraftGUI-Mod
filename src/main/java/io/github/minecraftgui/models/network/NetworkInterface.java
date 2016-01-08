package io.github.minecraftgui.models.network;

import io.github.minecraftgui.models.network.packets.PacketOut;

import java.util.UUID;

/**
 * Created by Samuel on 2015-12-09.
 */
public interface NetworkInterface {

    String MINECRAFT_GUI_CHANNEL = "MinecraftGUI";
    UUID ROOT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    int PACKET_INIT_CONNECTION = 0;
    int PACKET_INIT_CLIENT = 1;
    int PACKET_CLIENT_INITIATED = 2;
    int PACKET_INIT_INTERFACE = 3;
    int PACKET_INTERFACE_INITIATED = 4;
    int PACKET_CREATE_COMPONENT = 5;
    int PACKET_DELETE_COMPONENT = 6;
    int PACKET_ADD_EVENT = 7;

    int PACKET_EVENT_ON_CLICK = 100;
    int PACKET_EVENT_ON_DOUBLE_CLICK = 102;
    int PACKET_EVENT_ON_INPUT = 103;
    int PACKET_EVENT_ON_KEY_PRESSED = 104;
    int PACKET_EVENT_ON_VALUE_CHANGED = 105;
    int PACKET_EVENT_ON_BLUR = 106;
    int PACKET_EVENT_ON_FOCUS = 107;
    int PACKET_EVENT_ON_REMOVE = 108;
    int PACKET_EVENT_ON_GUI_OPEN = 109;
    int PACKET_EVENT_ON_GUI_CLOSE = 110;
    int PACKET_EVENT_ON_MOUSE_ENTER = 111;
    int PACKET_EVENT_ON_MOUSE_LEAVE = 112;

    int PACKET_SET_ATTRIBUTE_BACKGROUND_COLOR = 1000;
    int PACKET_SET_ATTRIBUTE_WIDTH = 1001;
    int PACKET_SET_ATTRIBUTE_HEIGHT = 1002;
    int PACKET_ADD_COMPONENT_RELATIVE_TO_X = 1003;
    int PACKET_ADD_COMPONENT_RELATIVE_TO_Y = 1004;
    int PACKET_SET_RELATIVE_X = 1005;
    int PACKET_SET_RELATIVE_Y = 1006;
    int PACKET_SET_ATTRIBUTE_BORDER_TOP = 1007;
    int PACKET_SET_ATTRIBUTE_BORDER_LEFT = 1008;
    int PACKET_SET_ATTRIBUTE_BORDER_RIGHT = 1009;
    int PACKET_SET_ATTRIBUTE_BORDER_BOTTOM = 1010;
    int PACKET_SET_ATTRIBUTE_BORDER_TOP_COLOR = 1011;
    int PACKET_SET_ATTRIBUTE_BORDER_LEFT_COLOR = 1012;
    int PACKET_SET_ATTRIBUTE_BORDER_RIGHT_COLOR = 1013;
    int PACKET_SET_ATTRIBUTE_BORDER_BOTTOM_COLOR = 1014;
    int PACKET_SET_ATTRIBUTE_PADDING_TOP = 1015;
    int PACKET_SET_ATTRIBUTE_PADDING_LEFT = 1016;
    int PACKET_SET_ATTRIBUTE_PADDING_RIGHT = 1017;
    int PACKET_SET_ATTRIBUTE_PADDING_BOTTOM = 1018;
    int PACKET_SET_ATTRIBUTE_MARGIN_TOP = 1019;
    int PACKET_SET_ATTRIBUTE_MARGIN_LEFT = 1020;
    int PACKET_SET_ATTRIBUTE_MARGIN_RIGHT = 1021;
    int PACKET_SET_ATTRIBUTE_MARGIN_BOTTOM = 1022;
    int PACKET_SET_CURSOR = 1023;
    int PACKET_SET_VISIBILITY = 1024;
    int PACKET_SET_ATTRIBUTE_BACKGROUND_IMAGE = 1025;
    int PACKET_SET_ATTRIBUTE_FONT = 1026;
    int PACKET_SET_ATTRIBUTE_FONT_COLOR = 1027;
    int PACKET_SET_ATTRIBUTE_FONT_SIZE = 1028;
    int PACKET_SET_ATTRIBUTE_CURSOR_COLOR = 1029;
    int PACKET_SET_VALUE = 1030;
    int PACKET_SET_TEXT_ALIGNMEMT = 1031;
    int PACKET_SET_POSITIONS = 1032;
    int PACKET_SET_TEXT_NB_LINE = 1034;
    int PACKET_UPDATE_LIST = 1035;

    String CONTENT = "content";
    String PACKET_ID = "packetId";

    String EVENT = "event";
    String ON_BLUR_LISTENER = "onBlurListener";
    String ON_FOCUS_LISTENER = "onFocusListener";
    String ON_CLICK_LISTENER = "onClickListener";
    String ON_DOUBLE_CLICK_LISTENER = "onDoubleClickListener";
    String ON_INPUT_LISTENER = "onInputListener";
    String ON_KEY_PRESSED_LISTENER = "onKeyPressedListener";
    String ON_REMOVE_LISTENER = "onRemoveListener";
    String ON_VALUE_CHANGE_LISTENER = "onValueChangeListener";
    String ON_MOUSE_ENTER_LISTENER = "onMouseEnterListener";
    String ON_MOUSE_LEAVE_LISTENER = "onMouseLeaveListener";

    String X = "x";
    String Y = "y";
    String RELATIVE_X = "xRelative";
    String RELATIVE_Y = "yRelative";
    String COMPONENT_RELATIVE_TO_X = "componentRelativeToX";
    String COMPONENT_RELATIVE_TO_Y = "componentRelativeToY";
    String R = "r";
    String G = "g";
    String B = "b";
    String A = "a";
    String URL = "url";
    String NAME = "name";
    String SIZE = "size";
    String FONTS = "fonts";
    String IMAGES = "images";
    String FONTS_TO_GENERATE = "fontsToGenerate";
    String TIME = "time";
    String PERCENTAGE = "percentage";
    String ATTRIBUTE = "attribute";
    String STATE = "state";
    String VALUE = "value";
    String KEY = "key";
    String COMPONENT = "component";
    String COMPONENT_ID = "componentId";
    String TYPE = "type";
    String PARENT_ID = "parentId";
    String SHAPE = "shape";
    String BUTTON_LIST_BEFORE = "buttonListBefore";
    String SLIDER_BUTTON = "sliderButton";
    String BUTTON_LIST_AFTER = "buttonListAfter";
    String BUTTON_LINE_BEFORE = "buttonLineBefore";
    String BUTTON_LINE_AFTER = "buttonLineAfter";
    String CHECKBOX = "checkBox";
    String DIV = "div";
    String LIST = "list";
    String PARAGRAPH = "paragraph";
    String INPUT = "input";
    String PROGRESS_BAR_VERTICAL = "progressbarVertical";
    String PROGRESS_BAR_HORIZONTAL = "progressbarHorizontal";
    String SLIDER_VERTICAL = "sliderVertical";
    String SLIDER_HORIZONTAL = "sliderHorizontal";
    String TEXT_AREA = "textArea";
    String SHAPE_NORMAL = "shapeNormal";
    String SHAPE_ON_VALUE_TRUE = "shapeOnValueTrue";
    String SHAPE_ON_VALUE_FALSE = "shapeOnValueFalse";
    String SHAPE_ON_PROGRESS = "shapeOnProgress";
    String TEXT = "text";
    String WIDTH = "WIDTH";
    String HEIGHT = "HEIGHT";
    String BORDER_TOP = "BORDER_TOP";
    String BORDER_LEFT = "BORDER_LEFT";
    String BORDER_RIGHT = "BORDER_RIGHT";
    String BORDER_BOTTOM = "BORDER_BOTTOM";
    String MARGIN_TOP = "MARGIN_TOP";
    String MARGIN_LEFT = "MARGIN_LEFT";
    String MARGIN_RIGHT = "MARGIN_RIGHT";
    String MARGIN_BOTTOM = "MARGIN_BOTTOM";
    String PADDING_TOP = "PADDING_TOP";
    String PADDING_LEFT = "PADDING_LEFT";
    String PADDING_RIGHT = "PADDING_RIGHT";
    String PADDING_BOTTOM = "PADDING_BOTTOM";
    String VISIBILITY = "VISIBILITY";
    String CURSOR = "CURSOR";
    String BACKGROUND_IMAGE = "BACKGROUND_IMAGE";
    String BACKGROUND_COLOR = "BACKGROUND_COLOR";
    String BORDER_TOP_COLOR = "BORDER_TOP_COLOR";
    String BORDER_LEFT_COLOR = "BORDER_LEFT_COLOR";
    String BORDER_RIGHT_COLOR = "BORDER_RIGHT_COLOR";
    String BORDER_BOTTOM_COLOR = "BORDER_BOTTOM_COLOR";
    String POLYGON_POSITIONS = "POLYGON_POSITIONS";
    String ELLIPSE_COLOR = "ellipseColor";
    String POLYGON_COLOR = "polygonColor";
    String RECTANGLE_COLOR = "rectangleColor";
    String RECTANGLE_IMAGE = "rectangleImage";

    void sendPacket(PacketOut packet);

}
