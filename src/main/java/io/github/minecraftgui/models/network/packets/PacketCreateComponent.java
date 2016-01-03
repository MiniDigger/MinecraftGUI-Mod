package io.github.minecraftgui.models.network.packets;

import io.github.minecraftgui.controllers.MainController;
import io.github.minecraftgui.models.components.*;
import io.github.minecraftgui.models.network.NetworkInterface;
import io.github.minecraftgui.models.shapes.*;
import org.json.JSONObject;

/**
 * Created by Samuel on 2015-12-06.
 */
public class PacketCreateComponent extends PacketIn {

    public PacketCreateComponent(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
        super(jsonObject, mainController, networkInterface);
        String parentId = jsonObject.getString(NetworkInterface.PARENT_ID);
        Component component = generateComponent(jsonObject.getJSONObject(NetworkInterface.COMPONENT));

        if(component != null)
            mainController.addComponent(parentId, component);
    }

    private Component generateComponent(JSONObject comp){
        String componentId = comp.getString(NetworkInterface.COMPONENT_ID);
        String type = comp.getString(NetworkInterface.TYPE);
        Class<? extends Shape> shape = getShapeClassByName(comp.getString(NetworkInterface.SHAPE));

        switch (type){
            case NetworkInterface.CHECKBOX: return new CheckBox(componentId, getShapeClassByName(comp.getString(NetworkInterface.SHAPE_ON_VALUE_FALSE)), getShapeClassByName(comp.getString(NetworkInterface.SHAPE_ON_VALUE_TRUE)));
            case NetworkInterface.DIV: return new Div(componentId, shape);
            case NetworkInterface.INPUT: return new Input(componentId, (Class<? extends Rectangle>) shape);
            case NetworkInterface.PROGRESS_BAR_VERTICAL: return new ProgressBar.Vertical(componentId, (Class<? extends Rectangle>) shape, (Class<? extends Rectangle>) getShapeClassByName(comp.getString(NetworkInterface.SHAPE_ON_PROGRESS)));
            case NetworkInterface.PROGRESS_BAR_HORIZONTAL: return new ProgressBar.Horizontal(componentId, (Class<? extends Rectangle>) shape, (Class<? extends Rectangle>) getShapeClassByName(comp.getString(NetworkInterface.SHAPE_ON_PROGRESS)));
            case NetworkInterface.LIST: return new List(componentId, (Class<? extends Rectangle>) shape, generateComponent(comp.getJSONObject(NetworkInterface.BUTTON_LIST_BEFORE)), generateComponent(comp.getJSONObject(NetworkInterface.BUTTON_LIST_AFTER)));
            case NetworkInterface.PARAGRAPH: return new Paragraph(componentId, (Class<? extends Rectangle>) shape, generateComponent(comp.getJSONObject(NetworkInterface.BUTTON_LINE_BEFORE)), generateComponent(comp.getJSONObject(NetworkInterface.BUTTON_LINE_AFTER)));
            case NetworkInterface.SLIDER_VERTICAL: return new Slider.Vertical(componentId, (Class<? extends Rectangle>) shape, (Class<? extends Rectangle>) getShapeClassByName(comp.getString(NetworkInterface.SHAPE_ON_PROGRESS)), generateComponent(comp.getJSONObject(NetworkInterface.SLIDER_BUTTON)));
            case NetworkInterface.SLIDER_HORIZONTAL: return new Slider.Horizontal(componentId, (Class<? extends Rectangle>) shape, (Class<? extends Rectangle>) getShapeClassByName(comp.getString(NetworkInterface.SHAPE_ON_PROGRESS)), generateComponent(comp.getJSONObject(NetworkInterface.SLIDER_BUTTON)));
            case NetworkInterface.TEXT_AREA: return new TextArea(componentId, (Class<? extends Rectangle>) shape, generateComponent(comp.getJSONObject(NetworkInterface.BUTTON_LINE_BEFORE)), generateComponent(comp.getJSONObject(NetworkInterface.BUTTON_LINE_AFTER)));
        }

        return null;
    }

    private Class<? extends Shape> getShapeClassByName(String shapeName){
        switch (shapeName){
            case NetworkInterface.ELLIPSE_COLOR: return EllipseColor.class;
            case NetworkInterface.POLYGON_COLOR: return PolygonColor.class;
            case NetworkInterface.RECTANGLE_COLOR: return RectangleColor.class;
            case NetworkInterface.RECTANGLE_IMAGE: return RectangleImage.class;
            default: return RectangleColor.class;
        }
    }

}
